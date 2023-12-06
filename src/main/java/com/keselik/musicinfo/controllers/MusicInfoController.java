package com.keselik.musicinfo.controllers;

import com.keselik.musicinfo.model.ArtistNotFoundException;
import com.keselik.musicinfo.model.MusicInfo;
import com.keselik.musicinfo.model.WikidataNotFoundException;
import com.keselik.musicinfo.services.MusicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/musicinfo")
public class MusicInfoController {

    @Autowired
    private MusicInfoService musicInfoService;

    @GetMapping("/{mbid}")
    public MusicInfo getMusicInfo(@PathVariable String mbid) {
        return musicInfoService.getMusicInfo(mbid);
    }
}