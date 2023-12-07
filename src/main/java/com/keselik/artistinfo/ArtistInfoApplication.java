package com.keselik.artistinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ArtistInfoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ArtistInfoApplication.class, args);
	}
}
