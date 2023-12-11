package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class WikidataService {

    private static final String WIKIDATA_API_URL = "https://www.wikidata.org/w/api.php";
    private static final String ENGLISH_WIKI_SITE_LINK = "enwiki";
    private static final String WIKIDATA_RELATION_TYPE = "wikidata";
    private static final String WIKI_PATH_PREFIX = "/wiki/";
    private final RestTemplate restTemplate;

    @Autowired
    public WikidataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("wikidataCache")
    public String findEnglishWikiTitle(MusicBrainzApiResponse response) {
        String entityId = findWikidataIdentifier(response);
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
            int startIndex = path.lastIndexOf(WIKI_PATH_PREFIX) + WIKI_PATH_PREFIX.length();
            int endIndex = path.length();

            return path.substring(startIndex, endIndex);
        } catch (Exception e) {
            handleBadURL(url);
        }

        return "";
    }

    private void handleBadURL(String url) {
        throw new WikidataIdentifierException(String.format("Bad URL for Wikidata: %s", url));
    }
}