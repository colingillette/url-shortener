package com.colingillette.urlshortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Hit {

    @Id
    @UuidGenerator
    private String correlationId;
    private String siteId;
    private String hitUtc;

    public Hit(String siteId) {
        this.siteId = siteId;
        this.hitUtc = String.valueOf(Instant.now());
    }
}
