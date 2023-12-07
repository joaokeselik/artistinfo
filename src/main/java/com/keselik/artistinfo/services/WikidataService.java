package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WikidataService {

    private static final String WIKIDATA_API_URL = "https://www.wikidata.org/w/api.php";
    private static final String ENGLISH_WIKI_SITE_LINK = "enwiki";
    private final RestTemplate restTemplate;

    @Autowired
    public WikidataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String findEnglishWikiTitle(String entityId) {
        WikidataApiResponse wikidataApiResponse = fetchWikidataResponse(entityId);
        Map<String, WikidataEntity> entities= wikidataApiResponse.getEntities();
        String title = "";
        if (entities != null) {
            title = entities.get(entityId).getSitelinks().get(ENGLISH_WIKI_SITE_LINK).getTitle();
        } else {
            handleBadRequest(entityId);
        }

        return title;
    }

    @Cacheable("wikidataCache")
    public WikidataApiResponse fetchWikidataResponse(String entityId) {
        String wikidataUrl = WIKIDATA_API_URL +
                "?action=wbgetentities" +
                "&ids=" + entityId +
                "&format=json" +
                "&props=sitelinks";
        WikidataApiResponse response = null;
        response = restTemplate.getForObject(wikidataUrl, WikidataApiResponse.class);

        return response;
    }

    private void handleBadRequest(String entityId) {
        throw new WikidataNotFoundException("Wikidata not found for entityId: " + entityId);
    }
}