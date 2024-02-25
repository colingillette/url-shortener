package com.colingillette.urlshortener.helper;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.model.AdminRequest;
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

    public static Site getSampleSiteModifyLongUrl(@Nullable String input) {
        Site site = new Site();
        site.setCreateEmail("test@example.com");
        site.setCreateUtc(String.valueOf(Instant.now()));
        site.setLongUrl(input);
        site.setShortUrl("GOOGLE");
        site.setRevisionUtc(String.valueOf(Instant.now()));

        return site;
    }

    public static AdminRequest getAdminRequest(@Nullable String id, String shortUrl) {
        AdminRequest request = new AdminRequest();
        request.setSiteId(id);
        request.setShortUrl(shortUrl);
        request.setLongUrl("http://www.google.com");
        request.setCreateEmail("test@example.com");
        return request;
    }
}
