package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.repository.HitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HitServiceTest {

    @Mock
    private HitRepository hitRepository;

    @InjectMocks
    private HitService hitService;

    @Test
    void saveNewHit_persistsHitLinkedToGivenSite() {
        Site site = new Site();
        ArgumentCaptor<Hit> hitCaptor = ArgumentCaptor.forClass(Hit.class);

        when(hitRepository.save(any(Hit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        hitService.saveNewHit(site);

        verify(hitRepository).save(hitCaptor.capture());
        Hit savedHit = hitCaptor.getValue();
        assertThat(savedHit.getSite()).isSameAs(site);
    }

    @Test
    void saveNewHit_handlesRepositoryErrorsWithoutThrowing() {
        Site site = new Site();
        when(hitRepository.save(any(Hit.class))).thenThrow(new RuntimeException("save failed"));

        assertThatCode(() -> hitService.saveNewHit(site)).doesNotThrowAnyException();
    }

    @Test
    void getAllHits_returnsAllHitsFromRepository() {
        List<Hit> hits = List.of(new Hit(), new Hit());
        when(hitRepository.findAll()).thenReturn(hits);

        List<Hit> result = hitService.getAllHits();

        assertThat(result).isSameAs(hits);
        verify(hitRepository).findAll();
    }
}
