package com.colingillette.urlshortener.config;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.HitRepository;
import com.colingillette.urlshortener.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Seeder {

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    HitRepository hitRepository;

    @Autowired
    public void run(ApplicationArguments args) {
        Site site = new Site();
        site.setCreateEmail("test@example.com");
        site.setCreateUtc(String.valueOf(Instant.now()));
        site.setLongUrl("http://www.google.com");
        site.setShortUrl("GOOGLE");
        site.setRevisionUtc(String.valueOf(Instant.now()));
        siteRepository.save(site);

        Hit hit = new Hit();
        hit.setSiteId(site.getId());
        hit.setHitUtc(String.valueOf(Instant.now()));
        hitRepository.save(hit);
    }
}
