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
import org.springframework.dao.OptimisticLockingFailureException;

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

    @Test
    @DisplayName("addSite - successful add; confirm uppercase short url")
    void addSite_test() {
        when(siteRepository.save(any(Site.class))).thenReturn(SiteTestDataHelper.getSampleSite());
        Site expected = SiteTestDataHelper.getSampleSite();

        Site result = siteService.addSite(SiteTestDataHelper.getSampleSite(null, "gOogLe"));

        verify(siteRepository, times(1)).save(any(Site.class));
        assertEquals(result.getShortUrl(), expected.getShortUrl());
        assertEquals(result.getLongUrl(), expected.getLongUrl());
        assertEquals(result.getCreateEmail(), expected.getCreateEmail());
    }

    @Test
    @DisplayName("addSite - failValidation; siteId present")
    void addSite_siteIdPresent() {
        assertThrows(IllegalArgumentException.class,
                () -> siteService.addSite(
                        SiteTestDataHelper.getSampleSite("test-invalid-12345", "gOogLe")
                )
        );
        verify(siteRepository, never()).save(any(Site.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("addSite - failValidation; shortUrl null or empty")
    void addSite_shortUrl(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> siteService.addSite(
                        SiteTestDataHelper.getSampleSite(null, input)
                )
        );
        verify(siteRepository, never()).save(any(Site.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("addSite - failValidation; shortUrl null or empty")
    void addSite_longUrl(String input) {
        Site request = SiteTestDataHelper.getSampleSite(null, input);
        request.setLongUrl(input);

        assertThrows(IllegalArgumentException.class, () -> siteService.addSite(request));
        verify(siteRepository, never()).save(any(Site.class));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("addSite - failValidation; email null or empty")
    void addSite_email(String input) {
        Site request = SiteTestDataHelper.getSampleSite(null, input);
        request.setCreateEmail(input);

        assertThrows(IllegalArgumentException.class, () -> siteService.addSite(request));
        verify(siteRepository, never()).save(any(Site.class));
    }

    @Test
    @DisplayName("addSite - fail to save is InternalError")
    void addSite_failedSave() {
        when(siteRepository.save(any(Site.class))).thenThrow(OptimisticLockingFailureException.class);
        assertThrows(InternalError.class,
                () -> siteService.addSite(SiteTestDataHelper.getSampleSite(null, "TestFail")));
    }
}
