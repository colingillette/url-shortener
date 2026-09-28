package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.SiteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@Slf4j
public class SiteService {

    @Autowired
    SiteRepository siteRepository;

    /**
     * Retrieves a Site entity based on the provided short URL.
     * If the site is not found or an error occurs, it returns null.
     *
     * @param shortUrl the shortened version of the URL
     * @return Site entity associated with short URL
     */
    public Site findByShortUrl(String shortUrl) {
        try {
            return siteRepository.findByShortUrl(shortUrl);
        } catch (Exception e) {
            log.error("Error occurred while fetching site by short URL: {}", shortUrl, e);
            return null;
        }
    }

    /**
     * Create or update a Site entity.
     *
     * @param site Site to upsert
     * @return Site entity that was saved
     */
    public Site save(Site site) {
        try {
            Site existingSite = findByShortUrl(site.getShortUrl());
            if (existingSite != null) {
                existingSite.setLongUrl(site.getLongUrl());
                existingSite.setCreateEmail(site.getCreateEmail());
                site = existingSite;
            } else {
                site.setCreateUtc(Instant.now().atZone(ZoneOffset.UTC).toInstant());
                site.setRevisionUtc(Instant.now().atZone(ZoneOffset.UTC).toInstant());
            }
            return siteRepository.save(site);
        } catch (Exception e) {
            log.error("Error occurred while saving site: {}", site.getShortUrl(), e);
            return null;
        }
    }

    /**
     * Deletes a provided Site entity.
     *
     * @param site Site entity to delete
     */
    public void delete(Site site) {
        try {
            siteRepository.delete(site);
        } catch (Exception e) {
            log.error("Error occurred while deleting site: {}", site.getShortUrl(), e);
            throw new InternalException("Error occurred while deleting site: " + site.getShortUrl());
        }
    }
}
