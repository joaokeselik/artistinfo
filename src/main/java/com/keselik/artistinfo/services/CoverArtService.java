package com.keselik.artistinfo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.keselik.artistinfo.model.CoverArtApiResponse;

import java.util.Arrays;

@Service
public class CoverArtService {

    private static final String COVERART_API_URL = "https://coverartarchive.org/release-group/";
    private static final String IMAGE_NOT_FOUND = "Image not found";
    private final RestTemplate restTemplate;

    @Autowired
    public CoverArtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("coverArtCache")
    public String fetchAlbumImageURL(String releaseGroupId) {

        //String releaseGroup = "c171986d-83d9-4659-9644-ce9dc2b30836";
        String coverArtUrl = COVERART_API_URL + releaseGroupId;
        System.out.println(releaseGroupId);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    coverArtUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            System.out.println(responseEntity.toString());

            HttpHeaders headersResponse = responseEntity.getHeaders();
            MediaType contentType = headersResponse.getContentType();
            CoverArtApiResponse response = restTemplate.getForObject(coverArtUrl, CoverArtApiResponse.class);
            return response.getImages().get(0).getImage();
        } catch (HttpClientErrorException ex) {
                 return IMAGE_NOT_FOUND;
        }
    }
}
