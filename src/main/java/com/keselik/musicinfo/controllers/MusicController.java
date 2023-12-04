package com.keselik.musicinfo.controllers;


import com.keselik.musicinfo.model.ArtistNotFoundException;
import com.keselik.musicinfo.model.MusicInfo;
import com.keselik.musicinfo.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/musicinfo")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping("/{mbid}")
    public MusicInfo getMusicInfo(@PathVariable String mbid) {
        return musicService.getMusicInfo(mbid);
    }

    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleArtistNotFoundException(ArtistNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}