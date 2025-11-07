package com.github.gianlucampos.springbootmediaserver.models;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;

public record MediaResource(ResourceRegion region, MediaType mediaType) {
}
