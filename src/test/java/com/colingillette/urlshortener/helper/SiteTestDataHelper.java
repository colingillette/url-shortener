package com.colingillette.urlshortener.helper;

import com.colingillette.urlshortener.entity.Site;
import jakarta.annotation.Nullable;

import java.time.Instant;

public class SiteTestDataHelper {

    public static Site getSampleSite() {
        Site site = new Site();
        site.setCreateEmail("test@example.com");
        site.setCreateUtc(String.valueOf(Instant.now()));
        site.setLongUrl("http://www.google.com");
        site.setShortUrl("GOOGLE");
        site.setRevisionUtc(String.valueOf(Instant.now()));

        return site;
    }

    public static Site getSampleSite(String id, String shortUrl) {
        Site site = new Site();
        site.setId(id);
        site.setCreateEmail("test@example.com");
        site.setCreateUtc(String.valueOf(Instant.now()));
        site.setLongUrl("http://www.google.com");
        site.setShortUrl(shortUrl);
        site.setRevisionUtc(String.valueOf(Instant.now()));

        return site;
    }

    public static Site getSampleSiteModifyLongUrl(@Nullable String input) {
        Site site = new Site();
        site.setCreateEmail("test@example.com");
        site.setCreateUtc(String.valueOf(Instant.now()));
        site.setLongUrl(input);
        site.setShortUrl("GOOGLE");
        site.setRevisionUtc(String.valueOf(Instant.now()));

        return site;
    }
}
