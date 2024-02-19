package com.colingillette.urlshortener.repository;

import com.colingillette.urlshortener.entity.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site, String> {

    Optional<Site> findByShortUrl(String shortUrl);
}
