package com.colingillette.urlshortener.service;

import com.colingillette.urlshortener.entity.Hit;
import com.colingillette.urlshortener.helper.SiteTestDataHelper;
import com.colingillette.urlshortener.repository.HitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HitServiceTest {

    @InjectMocks
    HitService hitService;

    @Mock
    HitRepository hitRepository;

    @Test
    @DisplayName("saveHit - successful save of traffic to site")
    void saveHit_test() {
        hitService.saveHit(SiteTestDataHelper.getSampleSite());
        verify(hitRepository, times(1)).save(any(Hit.class));
    }

    @Test
    @DisplayName("saveHit - failure to save Hit does not break process")
    void saveHit_fail() {
        when(hitRepository.save(any(Hit.class))).thenThrow(OptimisticLockingFailureException.class);
        assertAll(() -> hitService.saveHit(SiteTestDataHelper.getSampleSite()));
        verify(hitRepository, times(1)).save(any(Hit.class));
    }
}
