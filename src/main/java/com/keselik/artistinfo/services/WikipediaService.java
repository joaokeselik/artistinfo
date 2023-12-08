package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WikipediaService {

    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php";

    private static final String NONEXISTENT_PAGE = "-1";

    private final RestTemplate restTemplate;

    @Autowired
    public WikipediaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("wikipediaCache")
    public String fetchWikipediaDescription(String title) {
        String wikipediaUrl = WIKIPEDIA_API_URL +
                "?action=query" +
                "&format=json" +
                "&prop=extracts" +
                "&exintro=true" +
                "&redirects=true" +
                "&titles=" + title;

        WikipediaApiResponse response = restTemplate.getForObject(wikipediaUrl, WikipediaApiResponse.class);

        if (response.getQuery().getPages().get(NONEXISTENT_PAGE) != null) {
            handleBadRequest(title);
        } else {
            return parseWikipediaResponse(response, title);
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

    private void handleBadRequest(String tile) {
        throw new WikipediaNotFoundException("Wikipedia not found for title: " + tile);
    }
}
