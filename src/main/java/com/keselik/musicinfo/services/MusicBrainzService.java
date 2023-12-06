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
    @Autowired
    private WikidataService wikidataService;
    private final RestTemplate restTemplate;
    @Autowired
    public MusicBrainzService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MusicInfo fetchMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = MUSICBRAINZ_API_URL + mbid
                + "?&fmt=json&inc=url-rels+release-groups";

        MusicBrainzApiResponse response = null;
        try {
            response = restTemplate.getForObject(musicBrainzUrl, MusicBrainzApiResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            handleBadRequest(mbid);
        } catch (RestClientException e) {
            //TODO: handle this exception
            e.printStackTrace();
        }

        MusicInfo musicInfo = parseMusicBrainzResponse(response);
        return musicInfo;
    }


    private MusicInfo parseMusicBrainzResponse(MusicBrainzApiResponse response) {
        MusicInfo musicInfo = new MusicInfo();

        //Now I need to use the Title I got from WikiData to fetch the Description from WikiPedia
        String wikidataIdentifier = findWikidataIdentifier(response);
        String title = wikidataService.findEnglishWikiTitle(wikidataIdentifier);
        System.out.println(title);


        //TODO: Turn this loop into a method
        if (response != null && response.getReleaseGroups() != null) {
            List<Album> albums = new ArrayList<>();
            for (ReleaseGroup releaseGroup : response.getReleaseGroups()) {
                if (releaseGroup != null) {
                    Album album = new Album(releaseGroup.getTitle(), releaseGroup.getId(), "album-image-url");
                    // You may need to fetch the actual image URL
                    albums.add(album);
                }
            }
            musicInfo.setAlbums(albums);
        }
        return musicInfo;
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
    private void handleBadRequest(String mbid) {
        throw new ArtistNotFoundException("Artist not found for MBID: " + mbid);
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