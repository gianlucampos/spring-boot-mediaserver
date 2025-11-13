package com.github.gianlucampos.springbootmediaserver.controller;

import com.github.gianlucampos.springbootmediaserver.service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/app/v1/media")
@AllArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/{*filename}")
    public ResponseEntity<ResourceRegion> getMedia(@PathVariable String filename, @RequestHeader HttpHeaders headers) {
        return mediaService.getMedia(filename, headers)
            .map(resource -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(resource.mediaType())
                .body(resource.region()))
            .orElse(ResponseEntity.notFound().build());
    }
}
