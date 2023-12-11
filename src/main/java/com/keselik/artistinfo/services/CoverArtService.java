package com.keselik.artistinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.keselik.artistinfo.model.CoverArtApiResponse;

import java.util.Arrays;

@Service
public class CoverArtService {

    private static final String COVERART_API_URL = "https://coverartarchive.org/release-group/";
    private static final String IMAGE_NOT_FOUND = "";
    private final RestTemplate restTemplate;

    @Autowired
    public CoverArtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("coverArtCache")
    public String fetchAlbumImageURL(String releaseGroupId) {
        String coverArtUrl = COVERART_API_URL + releaseGroupId;

        try {
            CoverArtApiResponse response = restTemplate.getForObject(coverArtUrl, CoverArtApiResponse.class);

            if (response != null && response.getImages() != null && !response.getImages().isEmpty()) {
                return response.getImages().get(0).getImage();
            } else {
                return IMAGE_NOT_FOUND;
            }
        } catch (HttpClientErrorException ex) {
            return IMAGE_NOT_FOUND;
        }
    }
}
