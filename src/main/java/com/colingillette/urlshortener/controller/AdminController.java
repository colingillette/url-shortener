package com.colingillette.urlshortener.controller;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.service.HitService;
import com.colingillette.urlshortener.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private HitService hitService;

    @GetMapping("/sites/all")
    public ResponseEntity<List<Site>> getSites() {
        List<Site> sites = siteService.getAllSites();
        return ResponseEntity.ok(sites);
    }

    @GetMapping("/hits/all")
    public ResponseEntity<List<Hit>> getHits() {
        List<Hit> hits = hitService.getAllHits();
        return ResponseEntity.ok(hits);
    }
}
