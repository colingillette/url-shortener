package com.colingillette.urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminRequest {

    private String siteId;
    private String shortUrl;
    private String longUrl;
    private String createEmail;
}
