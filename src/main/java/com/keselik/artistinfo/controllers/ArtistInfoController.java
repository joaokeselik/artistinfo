package com.keselik.artistinfo.controllers;

import com.keselik.artistinfo.model.ArtistInfo;
import com.keselik.artistinfo.services.MusicBrainzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artistinfo")
public class ArtistInfoController {

    @Autowired
    private MusicBrainzService musicBrainzService;

    @GetMapping("/{mbid}")
    public ArtistInfo getArtistInfo(@PathVariable String mbid) {
        return musicBrainzService.fetchMusicBrainzResponse(mbid);
    }
}