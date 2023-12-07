package com.keselik.artistinfo.controllers;

import com.keselik.artistinfo.model.ArtistInfo;
import com.keselik.artistinfo.services.ArtistInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musicinfo")
public class ArtistInfoController {

    @Autowired
    private ArtistInfoService artistInfoService;

    @GetMapping("/{mbid}")
    public ArtistInfo getMusicInfo(@PathVariable String mbid) {
        return artistInfoService.getArtistInfo(mbid);
    }
}