package com.keselik.artistinfo.services;

import com.keselik.artistinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistInfoService {

    private MusicBrainzService musicBrainzService;

    @Autowired
    public ArtistInfoService(MusicBrainzService musicBrainzService) {
        this.musicBrainzService = musicBrainzService;
    }

    public ArtistInfo getArtistInfo(String mbid) {
        return musicBrainzService.fetchMusicBrainzResponse(mbid);
    }
}
