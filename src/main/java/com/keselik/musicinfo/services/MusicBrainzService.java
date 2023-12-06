package com.keselik.musicinfo.services;

import com.keselik.musicinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicBrainzService {

    private static final String MUSICBRAINZ_API_URL = "https://musicbrainz.org/ws/2/artist/";
    private static final String WIKIDATA_RELATION_TYPE = "wikidata";
    private final WikidataService wikidataService;
    private final RestTemplate restTemplate;
    @Autowired
    public MusicBrainzService(RestTemplate restTemplate, WikidataService wikidataService) {
        this.restTemplate = restTemplate;
        this.wikidataService = wikidataService;
    }

    public MusicInfo fetchMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = MUSICBRAINZ_API_URL + mbid
                + "?&fmt=json&inc=url-rels+release-groups";

        MusicBrainzApiResponse response = null;
        try {
            response = restTemplate.getForObject(musicBrainzUrl, MusicBrainzApiResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(mbid);
        }
        MusicInfo musicInfo = parseMusicBrainzResponse(response);
        musicInfo.setMbid(mbid);
        return musicInfo;
    }

    private void handleBadRequest(String mbid) {
        throw new ArtistNotFoundException("Artist not found for MBID: " + mbid);
    }

    private MusicInfo parseMusicBrainzResponse(MusicBrainzApiResponse response) {
        MusicInfo musicInfo = new MusicInfo();

        //fetchDescription(response);
        //fetchAlbums(response);

        //put this below into a method called fetchDescription(); or something like that
        String wikidataIdentifier = findWikidataIdentifier(response);
        String title = wikidataService.findEnglishWikiTitle(wikidataIdentifier);
        System.out.println(title);
        //Now I need to use the Title I got from WikiData to fetch the Description from WikiPedia
        //Then
        //musicInfo.setDescription(description);

        List<Album> albums = parseAlbums(response);
        musicInfo.setAlbums(albums);

        return musicInfo;
    }

    private List<Album> parseAlbums(MusicBrainzApiResponse response) {
        List<Album> albums = new ArrayList<>();
        if (response.getReleaseGroups() != null) {
            for (ReleaseGroup releaseGroup : response.getReleaseGroups()) {
                if (releaseGroup != null) {
                    Album album = new Album(releaseGroup.getTitle(), releaseGroup.getId(), "album-image-url");
                    // You may need to fetch the actual image URL
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
        //TODO: I have to handle the exception properly here and return properly too
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            int startIndex = path.lastIndexOf("/wiki/") + "/wiki/".length();
            int endIndex = path.length();

            return path.substring(startIndex, endIndex);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return "";
    }
}