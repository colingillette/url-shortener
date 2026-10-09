package com.colingillette.urlshortener.repository;

import com.colingillette.urlshortener.entity.Hit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HitRepository extends CrudRepository<Hit, UUID> {
}
