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
public class MusicInfoService {

    private MusicInfo musicInfo;
    private MusicBrainzService musicBrainzService;

    @Autowired
    public MusicInfoService(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    public MusicInfo getMusicInfo(String mbid) {
        musicInfo = musicBrainzService.fetchMusicBrainzResponse(mbid);
        musicInfo.setMbid(mbid);

        return musicInfo;
    }
}
