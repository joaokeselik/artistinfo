package com.keselik.musicinfo.controllers;


import com.keselik.musicinfo.model.MusicInfo;
import com.keselik.musicinfo.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/musicinfo")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("/{mbid}")
    public MusicInfo getMusicInfo(@PathVariable String mbid) {
        // TODO: Implement logic to fetch information from external APIs
        // and construct the MusicInfo object

        return musicService.getMusicInfo(mbid);


    }

}