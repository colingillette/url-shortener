package com.colingillette.urlshortener.repository;

import com.colingillette.urlshortener.entity.Hit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitRepository extends CrudRepository<Hit, String> {
}
