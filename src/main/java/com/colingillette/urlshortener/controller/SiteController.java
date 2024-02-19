package com.colingillette.urlshortener.controller;

import com.colingillette.urlshortener.service.HitService;
import com.colingillette.urlshortener.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class SiteController {

    @Autowired
    SiteService siteService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getUrlFromShortPath(@RequestParam String shortUrl) {
        Optional<String> result;

        try {
            result = siteService.getLongUrlByShortUrl(shortUrl);
        } catch (IllegalArgumentException | InternalError e) {
            log.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Uncaught exception in getUrlFromShortPath: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        }
    }
}
