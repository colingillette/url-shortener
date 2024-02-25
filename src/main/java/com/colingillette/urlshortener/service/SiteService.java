package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.model.AdminRequest;
import com.colingillette.urlshortener.repository.SiteRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class SiteService {

    @Autowired
    HitService hitService;

    @Autowired
    SiteRepository siteRepository;

    /**
     * Use short-form URL to retrieve associated real URL.
     *
     * @param shortUrl - String short representation of a URL
     * @return Optional String. Will return empty if a non-critical error occurred.
     * @throws InternalError - Thrown if an unexpected critical exception was caught during processing.
     * @throws IllegalArgumentException - Thrown if there's an issue with the supplied input.
     */
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

    public Site addSite(AdminRequest request) throws IllegalArgumentException, InternalError {
        try {
            boolean isValid = validateAddSiteRequest(request);

            if (isValid) {
                Site newSite = getNewSiteFromRequest(request);
                return siteRepository.save(newSite);
            } else {
                throw new IllegalArgumentException("Could not validate request");
            }
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            log.error("Error when saving new site", e);
            throw new InternalError("Unexpected error encountered when saving new site");
        }
    }

    // TODO: need real validation around true long URLs, short URL max length, and valid emails
    private boolean validateAddSiteRequest(AdminRequest request) {
        boolean isValid = true;

        if (null != StringUtils.trimToNull(request.getSiteId())) {
            log.error("Create request should not specify ID");
            isValid = false;
        }

        if (null == StringUtils.trimToNull(request.getShortUrl())) {
            // TODO: generate shortUrl if one is not specified
            log.error("Short URL cannot be null");
            isValid = false;
        }

        if (null == StringUtils.trimToNull(request.getLongUrl())) {
            log.error("Real URL cannot be null");
            isValid = false;
        }

        if (null == StringUtils.trimToNull(request.getCreateEmail())) {
            log.error("Creator email cannot be null");
            isValid = false;
        }

        return isValid;
    }

    private Site getNewSiteFromRequest(AdminRequest request) {
        Site site = new Site();
        String currentUtc = String.valueOf(Instant.now());
        String shortUrl = StringUtils.upperCase(StringUtils.trim(request.getShortUrl()));
        String longUrl = StringUtils.trim(request.getLongUrl());
        String email = StringUtils.trim(request.getCreateEmail());

        site.setShortUrl(shortUrl);
        site.setLongUrl(longUrl);
        site.setCreateEmail(email);
        site.setCreateUtc(currentUtc);
        site.setRevisionUtc(currentUtc);

        return site;
    }
}
