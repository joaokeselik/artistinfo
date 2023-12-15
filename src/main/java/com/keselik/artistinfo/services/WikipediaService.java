package com.keselik.artistinfo.services;

import com.keselik.artistinfo.exception.WikipediaNotFoundException;
import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WikipediaService {

    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php";
    private static final String NONEXISTENT_PAGE = "-1";
    private final WikidataService wikidataService;
    private final RestTemplate restTemplate;

    @Autowired
    public WikipediaService(RestTemplate restTemplate,
                            WikidataService  wikidataService) {
        this.wikidataService = wikidataService;
        this.restTemplate = restTemplate;
    }

    @Cacheable("wikipediaCache")
    public String fetchWikipediaDescription(MusicBrainzApiResponse musicBrainzResponse) {
        String title = wikidataService.findEnglishWikiTitle(musicBrainzResponse);
        String wikipediaUrl = WIKIPEDIA_API_URL +
                "?action=query" +
                "&format=json" +
                "&prop=extracts" +
                "&exintro=true" +
                "&redirects=true" +
                "&titles=" + title;

        WikipediaApiResponse wikipediaResponse = restTemplate.getForObject(wikipediaUrl, WikipediaApiResponse.class);

        if (wikipediaResponse.getQuery().getPages().get(NONEXISTENT_PAGE) != null) {
            handleBadRequest(title);
        } else {
            return parseWikipediaResponse(wikipediaResponse, title);
        }
        return "";
    }

    private String parseWikipediaResponse(WikipediaApiResponse response, String title) {
        for (Page page : response.getQuery().getPages().values()) {
            if (page.getTitle().equals(title)) {
                return page.getExtract();
            }
        }
        return "";
    }

    private void handleBadRequest(String title) {
        throw new WikipediaNotFoundException("Wikipedia not found for title: " + title);
    }
}
