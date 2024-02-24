package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.helper.SiteTestDataHelper;
import com.colingillette.urlshortener.repository.SiteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SiteServiceTest {

    @InjectMocks
    SiteService siteService;

    @Mock
    SiteRepository siteRepository;

    @Mock
    HitService hitService;

    @Test
    @DisplayName("getLongUrlByShortUrl - successful return; confirm uppercase transform")
    void getLongUrlByShortUrl_test() {
        when(siteRepository.findByShortUrl("TEST1")).thenReturn(Optional.of(SiteTestDataHelper.getSampleSite()));

        Optional<String> result = siteService.getLongUrlByShortUrl("test1");

        assertTrue(result.isPresent());
        assertEquals(SiteTestDataHelper.getSampleSite().getLongUrl(), result.get());
        verify(siteRepository, times(1)).findByShortUrl("TEST1");
        verify(hitService, times(1)).saveHit(any(Site.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("getLongUrlByShortUrl - null and empty shortUrl are IllegalArgument Exception")
    void getLongUrlByShortUrl_badInput(String input) {
        assertThrows(IllegalArgumentException.class, () -> siteService.getLongUrlByShortUrl(input));
        verify(siteRepository, never()).findByShortUrl("TEST1");
        verify(hitService, never()).saveHit(any(Site.class));
    }

    @Test
    @DisplayName("getLongUrlByShortUrl - empty Optional when not found")
    void getLongUrlByShortUrl_notFound() {
        when(siteRepository.findByShortUrl("TEST1")).thenReturn(Optional.empty());

        Optional<String> result = siteService.getLongUrlByShortUrl("test1");

        assertEquals(Optional.empty(), result);
        verify(siteRepository, times(1)).findByShortUrl("TEST1");
        verify(hitService, never()).saveHit(any(Site.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("getLongUrlByShortUrl - null and empty longUrl return empty result")
    void getLongUrlByShortUrl_badLongUrl(String input) {
        when(siteRepository.findByShortUrl("TEST1"))
                .thenReturn(Optional.of(SiteTestDataHelper.getSampleSiteModifyLongUrl(input)));

        Optional<String> result = siteService.getLongUrlByShortUrl("test1");

        assertEquals(Optional.empty(), result);
        verify(siteRepository, times(1)).findByShortUrl("TEST1");
        verify(hitService, never()).saveHit(any(Site.class));
    }
}
