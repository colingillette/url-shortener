package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.HitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HitService {

    @Autowired
    HitRepository hitRepository;

    /**
     * Save an instance of a short url being used to navigate to a
     * redirect long url.
     *
     * @param site Site being accessed
     */
    public void saveNewHit(Site site) {
        try {
            Hit hit = new Hit();
            hit.setSite(site);
            hit = hitRepository.save(hit);
            log.debug("hit save success: {}", hit);
        } catch (Exception e) {
            log.error("Error occurred while saving hit for site: {}", site, e);
        }
    }

    /**
     * Retrieves all hits in db. Used by AdminController only.
     *
     * @return List of all Hits
     */
    public List<Hit> getAllHits() {
        return (List<Hit>) hitRepository.findAll();
    }
}
