package com.colingillette.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "hit")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Hit {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "correlation_id", nullable = false, updatable = false)
    private UUID correlationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @CreationTimestamp
    @Column(name = "hit_utc", nullable = false, updatable = false)
    private Instant hitUtc;
}
