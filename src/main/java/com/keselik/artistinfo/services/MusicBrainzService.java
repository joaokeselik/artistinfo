package com.keselik.artistinfo.services;

import com.keselik.artistinfo.exception.ArtistNotFoundException;
import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class MusicBrainzService {

    private static final String MUSICBRAINZ_API_URL = "https://musicbrainz.org/ws/2/artist/";
    private static final int COVERART_MAX_PARALLEL_REQUESTS = 20;
    private final WikipediaService wikipediaService;
    private final CoverArtService coverArtService;
    private final RestTemplate restTemplate;

    @Autowired
    public MusicBrainzService(RestTemplate restTemplate,
                              WikipediaService wikipediaService,
                              CoverArtService coverArtService) {
        this.restTemplate = restTemplate;
        this.wikipediaService = wikipediaService;
        this.coverArtService = coverArtService;
    }

    @Cacheable("artistInfoCache")
    @Async
    public CompletableFuture<ArtistInfo> fetchMusicBrainzResponseAsync(String mbid) {
        return CompletableFuture.completedFuture(fetchMusicBrainzResponse(mbid));
    }

    public ArtistInfo fetchMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = MUSICBRAINZ_API_URL + mbid
                + "?&fmt=json&inc=url-rels+release-groups";

        MusicBrainzApiResponse response = null;
        try {
            response = restTemplate.getForObject(musicBrainzUrl, MusicBrainzApiResponse.class);
        } catch (HttpClientErrorException e) {
            handleBadRequest(mbid);
        }

        return parseMusicBrainzResponse(response, mbid);
    }

    private ArtistInfo parseMusicBrainzResponse(MusicBrainzApiResponse response, String mbid) {
        ArtistInfo artistInfo = new ArtistInfo();
        String description = fetchDescription(response);
        List<Album> albums = fetchAlbums(response);
        artistInfo.setDescription(description);
        artistInfo.setAlbums(albums);
        artistInfo.setMbid(mbid);

        return artistInfo;
    }

    private void handleBadRequest(String mbid) {
        throw new ArtistNotFoundException("Artist not found for MBID: " + mbid);
    }

    private String fetchDescription(MusicBrainzApiResponse response) {
        return wikipediaService.fetchWikipediaDescription(response);
    }

    private List<Album> fetchAlbums(MusicBrainzApiResponse response) {
        if (response.getReleaseGroups() != null) {
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(COVERART_MAX_PARALLEL_REQUESTS);

            List<CompletableFuture<Album>> albumFutures = response.getReleaseGroups()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(releaseGroup -> CompletableFuture.supplyAsync(() -> fetchAlbum(releaseGroup), executor))
                    .collect(Collectors.toList());

            return albumFutures
                    .stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

        }
        return new ArrayList<>();
    }

    private Album fetchAlbum(ReleaseGroup releaseGroup) {
        String albumImageUrl = coverArtService.fetchAlbumImageURL(releaseGroup.getId());
        return new Album(releaseGroup.getTitle(), releaseGroup.getId(), albumImageUrl);
    }

}