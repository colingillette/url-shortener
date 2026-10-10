package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.SiteRepository;
import org.apache.logging.log4j.util.InternalException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private SiteService siteService;

    @Test
    void findByShortUrl_returnsSiteFromRepository() {
        Site site = new Site();
        site.setShortUrl("abc123");

        when(siteRepository.findByShortUrl("abc123")).thenReturn(site);

        Site result = siteService.findByShortUrl("abc123");

        assertThat(result).isSameAs(site);
        verify(siteRepository).findByShortUrl("abc123");
    }

    @Test
    void findByShortUrl_returnsNullWhenRepositoryThrows() {
        when(siteRepository.findByShortUrl("abc123")).thenThrow(new RuntimeException("database failed"));

        Site result = siteService.findByShortUrl("abc123");

        assertThat(result).isNull();
    }

    @Test
    void save_newSite_setsAuditTimestampsAndPersists() {
        Site site = new Site();
        site.setShortUrl("abc123");
        site.setLongUrl("https://example.com/long");
        site.setCreateEmail("creator@example.com");

        when(siteRepository.findByShortUrl("abc123")).thenReturn(null);
        when(siteRepository.save(any(Site.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Site saved = siteService.save(site);

        assertThat(saved).isSameAs(site);
        assertThat(saved.getCreateUtc()).isNotNull();
        assertThat(saved.getRevisionUtc()).isNotNull();
        verify(siteRepository).save(site);
    }

    @Test
    void save_existingSite_updatesExistingEntityAndSaves() {
        Site existingSite = new Site();
        existingSite.setShortUrl("abc123");
        existingSite.setLongUrl("https://old.example.com");
        existingSite.setCreateEmail("old@example.com");

        Site incomingSite = new Site();
        incomingSite.setShortUrl("abc123");
        incomingSite.setLongUrl("https://new.example.com");
        incomingSite.setCreateEmail("new@example.com");

        when(siteRepository.findByShortUrl("abc123")).thenReturn(existingSite);
        when(siteRepository.save(existingSite)).thenReturn(existingSite);

        Site saved = siteService.save(incomingSite);

        assertThat(saved).isSameAs(existingSite);
        assertThat(existingSite.getLongUrl()).isEqualTo("https://new.example.com");
        assertThat(existingSite.getCreateEmail()).isEqualTo("new@example.com");
        verify(siteRepository).save(existingSite);
    }

    @Test
    void save_returnsNullWhenRepositorySaveThrows() {
        Site site = new Site();
        site.setShortUrl("abc123");
        site.setLongUrl("https://example.com");
        site.setCreateEmail("creator@example.com");

        when(siteRepository.findByShortUrl("abc123")).thenReturn(null);
        when(siteRepository.save(any(Site.class))).thenThrow(new RuntimeException("save failed"));

        Site saved = siteService.save(site);

        assertThat(saved).isNull();
    }

    @Test
    void delete_delegatesToRepositoryAndThrowsInternalExceptionOnFailure() {
        Site site = new Site();
        site.setShortUrl("abc123");

        doThrow(new RuntimeException("delete failed")).when(siteRepository).delete(site);

        assertThatThrownBy(() -> siteService.delete(site))
                .isInstanceOf(InternalException.class)
                .hasMessageContaining("Error occurred while deleting site: abc123");

        verify(siteRepository).delete(site);
    }

    @Test
    void getAllSites_returnsAllSitesFromRepository() {
        List<Site> sites = List.of(new Site(), new Site());
        when(siteRepository.findAll()).thenReturn(sites);

        List<Site> result = siteService.getAllSites();

        assertThat(result).isSameAs(sites);
        verify(siteRepository).findAll();
    }
}
