package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.SiteRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class SiteService {

    @Autowired
    HitService hitService;

    @Autowired
    SiteRepository siteRepository;

    public Optional<String> getLongUrlByShortUrl(String shortUrl) throws InternalError, IllegalArgumentException {
        Optional<String> emptyResult = Optional.empty();

        if (null == StringUtils.trimToNull(shortUrl)) {
            throw new IllegalArgumentException("Short URL Path cannot be null");
        }

        try {
            Site site;
            Optional<Site> optionalSite = getSiteByShortUrl(shortUrl);

            if (optionalSite.isEmpty()) {
                log.warn("Site not found by Short URL: {}", shortUrl);
                return emptyResult;
            } else {
                site = optionalSite.get();
                log.info("Site found by Long URL: {}", site);
            }

            String longUrl = site.getLongUrl();
            if (null == StringUtils.trimToNull(longUrl)) {
                log.warn("Site found by Short URL did not have Long URL");
                return emptyResult;
            } else {
                hitService.saveHit(site);
            }

            return Optional.of(longUrl);
        } catch (NotFoundException nfe) {
            throw nfe;
        } catch (Exception e) {
            log.error("Error finding Long URL by Short URL", e);
            throw new InternalError();
        }
    }

    private Optional<Site> getSiteByShortUrl(@NotNull String shortUrl) {
        String shortUrlCaps = shortUrl.toUpperCase(Locale.ROOT);
        return siteRepository.findByShortUrl(shortUrlCaps);
    }
}
