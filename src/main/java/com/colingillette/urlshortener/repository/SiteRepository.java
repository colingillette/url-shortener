package com.colingillette.urlshortener.repository;

import com.colingillette.urlshortener.entity.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SiteRepository extends CrudRepository<Site, UUID> {

    Site findByShortUrl(String shortUrl);
}
