package com.github.gianlucampos.springbootmediaserver.service;

import com.github.gianlucampos.springbootmediaserver.models.MediaResource;
import com.github.gianlucampos.springbootmediaserver.utils.StreamUtils;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MediaService {

    @Value("${spring.media.directory}")
    private String mediaDir;

    private static final long CHUNK_SIZE = (long) 1024 * 1024;

    public Optional<MediaResource> getMedia(String filename, HttpHeaders headers) {
        File file = new File(mediaDir.concat(filename));

        if (!file.exists() || !file.isFile()) {
            return Optional.empty();
        }

        try {
            FileSystemResource resource = new FileSystemResource(file);
            ResourceRegion region = StreamUtils.resourceRegion(resource, headers, CHUNK_SIZE);
            MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
            return Optional.of(new MediaResource(region, mediaType));

        } catch (IOException e) {
            log.error(e.getMessage(), e, "Failed to read media file: {}", filename);
            throw new UncheckedIOException("Error reading media file: ".concat(filename), e);
        }
    }
}
