package com.colingillette.urlshortener.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class SiteRequest {

    private String shortUrl;
    private String longUrl;
    private String createEmail;

}
