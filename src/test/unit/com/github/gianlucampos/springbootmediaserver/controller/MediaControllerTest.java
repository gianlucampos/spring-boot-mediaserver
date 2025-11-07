package com.github.gianlucampos.springbootmediaserver.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.gianlucampos.springbootmediaserver.models.MediaResource;
import com.github.gianlucampos.springbootmediaserver.service.MediaService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    @InjectMocks
    private MediaController mediaController;
    @Mock
    private MediaService mediaService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
    }

    @Test
    @DisplayName("getMedia returns 206 Partial Content when the file exists")
    void getMedia_returnsPartialContent_whenFileExists() {
        String filename = "video.mp4";
        MediaResource mockResource = new MediaResource(
            new ResourceRegion(mock(Resource.class), 0, 100),
            MediaType.valueOf("video/mp4")
        );

        when(mediaService.getMedia(filename, headers)).thenReturn(Optional.of(mockResource));

        ResponseEntity<ResourceRegion> response = mediaController.getMedia(filename, headers);

        verify(mediaService, times(1)).getMedia(filename, headers);

        assertEquals(HttpStatus.PARTIAL_CONTENT, response.getStatusCode());
        assertEquals(mockResource.region(), response.getBody());
        assertEquals(mockResource.mediaType(), response.getHeaders().getContentType());
    }

    @Test
    @DisplayName("getMedia returns 404 Not Found when the file does not exist")
    void getMedia_returnsNotFound_whenFileDoesNotExist() {
        String filename = "missing.mp4";

        when(mediaService.getMedia(filename, headers)).thenReturn(Optional.empty());

        ResponseEntity<ResourceRegion> response = mediaController.getMedia(filename, headers);

        verify(mediaService, times(1)).getMedia(filename, headers);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
