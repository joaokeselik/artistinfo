package com.keselik.musicinfo.services;

import com.keselik.musicinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MusicService {

    private final RestTemplate restTemplate;

    @Autowired
    public MusicService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MusicInfo getMusicInfo(String mbid) {


        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setMbid(mbid);

        // TODO: Implement logic to call external APIs and populate MusicInfo

        // Example: Fetching album information
        List<Album> albums = fetchMusicBrainzResponse(mbid);
        musicInfo.setAlbums(albums);

        return musicInfo;


    }

    private List<Album> fetchMusicBrainzResponse(String mbid) {

        String musicBrainzApiUrl = "https://musicbrainz.org/ws/2/artist/" + mbid + "?&fmt=json&inc=url-rels+release-groups";
        MusicBrainzApiResponse response = null;
        try {
            response = restTemplate.getForObject(musicBrainzApiUrl, MusicBrainzApiResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(mbid);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return parseMusicBrainzResponse(response);
    }

    private void handleBadRequest(String mbid) {
        throw new ArtistNotFoundException("Artist not found for MBID: " + mbid);
    }

    private List<Album> parseMusicBrainzResponse(MusicBrainzApiResponse response) {
        if (response != null && response.getReleaseGroups() != null) {
            List<Album> albums = new ArrayList<>();
            for (ReleaseGroup releaseGroup : response.getReleaseGroups()) {
                if (releaseGroup != null) {
                    Album album = new Album(releaseGroup.getTitle(), releaseGroup.getId(), "album-image-url");
                    // You may need to fetch the actual image URL
                    albums.add(album);
                }
            }
            return albums;
        }
        return Collections.emptyList();
    }
}
