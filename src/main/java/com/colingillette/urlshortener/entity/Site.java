package com.colingillette.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "site")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Site {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "short_url", unique = true, nullable = false)
    private String shortUrl;

    @Column(name = "long_url", nullable = false, length = 2048)
    private String longUrl;

    @Column(name = "create_email", nullable = false)
    private String createEmail;

    @Column(name = "create_utc", nullable = false)
    private Instant createUtc;

    @Column(name = "revision_utc", nullable = false)
    private Instant revisionUtc;
}
