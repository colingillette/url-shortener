package com.colingillette.urlshortener.controller;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.model.SiteRequest;
import com.colingillette.urlshortener.service.HitService;
import com.colingillette.urlshortener.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@Slf4j
public class SiteController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private HitService hitService;

    @GetMapping("/{shortUrl}")
    public RedirectView redirect(@PathVariable String shortUrl) {
        Site site = siteService.findByShortUrl(shortUrl);
        if (site == null) {
            log.error("No site found with shortUrl {}", shortUrl);
            // TODO: use a real error page eventually
            return new RedirectView("https://colingillette.com");
        }

        hitService.saveNewHit(site);
        return new RedirectView(site.getLongUrl());
    }

    @PutMapping("/site")
    public ResponseEntity<Site> createOrUpdateSite(@RequestBody SiteRequest siteRequest) {
        if (siteRequest == null || siteRequest.getShortUrl() == null) {
            return ResponseEntity.badRequest().build();
        }

        Site site = new Site();
        site.setShortUrl(siteRequest.getShortUrl());
        site.setLongUrl(siteRequest.getLongUrl());
        site.setCreateEmail(siteRequest.getCreateEmail());

        boolean isUpdate = false;
        if (siteService.findByShortUrl(site.getShortUrl()) != null) {
            log.info("Updating existing site with shortUrl {}", site.getShortUrl());
            isUpdate = true;
        } else {
            log.info("Creating new site with shortUrl {}", site.getShortUrl());
        }
        Site savedSite = siteService.save(site);

        if (savedSite == null) {
            return ResponseEntity.internalServerError().build();
        }

        return isUpdate ? ResponseEntity.ok(savedSite) : ResponseEntity.created(URI.create("/" + savedSite.getShortUrl()))
                .body(savedSite);
    }
}
