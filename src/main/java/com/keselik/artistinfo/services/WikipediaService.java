package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.MusicBrainzApiResponse;
import com.keselik.artistinfo.model.Page;
import com.keselik.artistinfo.model.WikipediaApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WikipediaService {

    private static final String WIKIPEDIA_API_URL = "https://en.wikipedia.org/w/api.php";

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

        for (Page page : response.getQuery().getPages().values()) {
            if (page.getTitle().equals(title)) {
                String extract = page.getExtract();
                return extract;
            }

        }

        return "";
    }
}
