package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicBrainzService {

    private ArtistInfo artistInfo;
    private static final String MUSICBRAINZ_API_URL = "https://musicbrainz.org/ws/2/artist/";
    private static final String WIKIDATA_RELATION_TYPE = "wikidata";
    private final WikidataService wikidataService;
    private final WikipediaService wikipediaService;
    private final RestTemplate restTemplate;
    @Autowired
    public MusicBrainzService(RestTemplate restTemplate, WikidataService wikidataService,
                              WikipediaService wikipediaService,  ArtistInfo artistInfo) {
        this.restTemplate = restTemplate;
        this.wikidataService = wikidataService;
        this.wikipediaService = wikipediaService;
        this.artistInfo = artistInfo;
    }

    @Cacheable("artistInfoCache")
    public ArtistInfo fetchMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = MUSICBRAINZ_API_URL + mbid
                + "?&fmt=json&inc=url-rels+release-groups";

        MusicBrainzApiResponse response = null;
        try {
            response = restTemplate.getForObject(musicBrainzUrl, MusicBrainzApiResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(mbid);
        }

        artistInfo.setMbid(mbid);
        artistInfo = parseMusicBrainzResponse(response);
        return artistInfo;
    }

    private void handleBadRequest(String mbid) {
        throw new ArtistNotFoundException("Artist not found for MBID: " + mbid);
    }

    private ArtistInfo parseMusicBrainzResponse(MusicBrainzApiResponse response) {
        String description = fetchDescription(response);
        List<Album> albums = fetchAlbums(response);
        artistInfo.setDescription(description);
        artistInfo.setAlbums(albums);

        return artistInfo;
    }

    private String fetchDescription(MusicBrainzApiResponse response) {
        String wikidataIdentifier = findWikidataIdentifier(response);
        String title = wikidataService.findEnglishWikiTitle(wikidataIdentifier);
        return wikipediaService.fetchWikipediaDescription(title);
    }

    private List<Album> fetchAlbums(MusicBrainzApiResponse response) {
        List<Album> albums = new ArrayList<>();
        if (response.getReleaseGroups() != null) {
            for (ReleaseGroup releaseGroup : response.getReleaseGroups()) {
                if (releaseGroup != null) {
                    // TODO: String albumImageUrl = fetchAlbumImageURL();
                    Album album = new Album(releaseGroup.getTitle(), releaseGroup.getId(), "album-image-url");
                    albums.add(album);
                }
            }
        }
        return albums;
    }

    private String findWikidataIdentifier (MusicBrainzApiResponse response) {
        String wikidataIdentifier = "";
        if (response != null && response.getRelations() != null) {
            RelationUrl relationUrl;
            for (Relation relation : response.getRelations()) {
                if (relation != null && relation.getType().equals(WIKIDATA_RELATION_TYPE)) {
                    relationUrl = relation.getUrl();
                    wikidataIdentifier = extractWikidataIdentifier(relationUrl.getResource());
                    return wikidataIdentifier;
                }
            }
        }
        return wikidataIdentifier;
    }

    private String extractWikidataIdentifier(String url) {

        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            int startIndex = path.lastIndexOf("/wiki/") + "/wiki/".length();
            int endIndex = path.length();

            return path.substring(startIndex, endIndex);
        } catch (URISyntaxException e) {
            //TODO: I have to handle the exception properly here and return properly too
            e.printStackTrace();
        }

        return "";
    }
}