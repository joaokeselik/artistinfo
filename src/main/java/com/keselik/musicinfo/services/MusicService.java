package com.keselik.musicinfo.services;

import com.keselik.musicinfo.model.Album;
import com.keselik.musicinfo.model.MusicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        List<Album> albums = fetchAlbums(mbid);
        musicInfo.setAlbums(albums);

        return musicInfo;


    }

    private List<Album> fetchAlbums(String mbid) {
        // TODO: Implement logic to call MusicBrainz API to get album information
        // Use restTemplate to make HTTP requests

        // Example URL (replace with actual MusicBrainz API URL)
        String apiUrl = "https://musicbrainz.org/ws/2/artist/" + mbid + "?&fmt=json&inc=release-groups";

        // Example request to MusicBrainz API
        // MusicBrainz API response should be parsed to extract album information
        // restTemplate.getForObject(apiUrl, YourApiResponseClass.class);

        // For demonstration purposes, creating a dummy list of albums
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("Album1", "album1-id", "album1-image-url"));
        albums.add(new Album("Album2", "album2-id", "album2-image-url"));

        return albums;
    }

}
