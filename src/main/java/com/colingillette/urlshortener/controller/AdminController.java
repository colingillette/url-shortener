package com.colingillette.urlshortener.controller;

import com.colingillette.urlshortener.entity.Site;
import com.colingillette.urlshortener.model.AdminRequest;
import com.colingillette.urlshortener.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminController {

    @Autowired
    SiteService siteService;

    @PostMapping("/add")
    public ResponseEntity<Site> addSite(@RequestBody AdminRequest request) {
        try {
            Site site = siteService.addSite(request);
            return new ResponseEntity<>(site, HttpStatus.CREATED);
        } catch (IllegalArgumentException iae) {
            log.error("Failed addSite with Argument exception", iae);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (InternalError ie) {
            log.error("Failed addSite with internal error", ie);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
