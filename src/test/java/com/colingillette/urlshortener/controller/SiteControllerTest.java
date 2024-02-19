package com.colingillette.urlshortener.controller;

import com.colingillette.urlshortener.service.SiteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SiteControllerTest {

    @InjectMocks
    SiteController siteController;

    @Mock
    SiteService siteService;

    @Test
    @DisplayName("getUrlFromShortPath - Successful find is status OK")
    void getUrlFromShortPath_success() {
        when(siteService.getLongUrlByShortUrl(anyString())).thenReturn(Optional.of("http://google.com"));

        ResponseEntity<String> result = siteController.getUrlFromShortPath("google");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    @DisplayName("getUrlFromShortPath - 404 Not Found on empty Long URL result")
    void getUrlFromShortPath_notFound() {
        when(siteService.getLongUrlByShortUrl(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> result = siteController.getUrlFromShortPath("notFound");

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    @DisplayName("getUrlFromShortPath - Expected Exception is 500 Internal Server Error")
    void getUrlFromShortPath_internalError() {
        when(siteService.getLongUrlByShortUrl(anyString())).thenThrow(new InternalError());

        ResponseEntity<String> result = siteController.getUrlFromShortPath("failExpected");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    @DisplayName("getUrlFromShortPath - Unexpected Exception is 500 Internal Server Error")
    void getUrlFromShortPath_genericException() {
        when(siteService.getLongUrlByShortUrl(anyString())).thenThrow(new InternalError());

        ResponseEntity<String> result = siteController.getUrlFromShortPath("failUnexpected");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}
