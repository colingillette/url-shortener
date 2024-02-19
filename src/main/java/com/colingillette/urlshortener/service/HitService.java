package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.HitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HitService {

    @Autowired
    HitRepository hitRepository;

    public void saveHit(Site site) {
        try {
            Hit hit = new Hit(site.getId());
            hitRepository.save(hit);
            log.info("Hit logged: {}", hit);
        } catch (Exception e) {
            log.error("Error saving hit for site: {}", site);
        }
    }
}
