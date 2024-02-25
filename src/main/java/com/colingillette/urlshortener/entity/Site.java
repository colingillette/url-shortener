package com.colingillette.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Site {

    @Id
    @UuidGenerator
    private String id;
    @Column(unique = true)
    private String shortUrl;
    private String longUrl;
    private String createEmail;
    private String createUtc;
    private String revisionUtc;
}
