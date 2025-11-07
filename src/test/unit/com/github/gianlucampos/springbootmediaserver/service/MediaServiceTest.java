package com.github.gianlucampos.springbootmediaserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import com.github.gianlucampos.springbootmediaserver.utils.StreamUtils;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

class MediaServiceTest {

    private MediaService mediaService;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mediaService = new MediaService();
        headers = new HttpHeaders();
    }

    @Test
    @DisplayName("getMedia returns media when file exists")
    void getMedia_returnsMediaResource_whenFileExists() throws IOException {
        File tempFile = Files.createTempFile("test", ".mp4").toFile();
        tempFile.deleteOnExit();

        ReflectionTestUtils.setField(mediaService, "mediaDir", tempFile.getParent() + "/");

        Optional<?> result = mediaService.getMedia(tempFile.getName(), headers);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("getMedia returns empty when file doest not exists")
    void getMedia_returnsEmpty_whenFileDoesNotExist() {
        ReflectionTestUtils.setField(mediaService, "mediaDir", "/fake/path/");
        Optional<?> result = mediaService.getMedia("missing.mp4", headers);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getMedia throws UncheckedIOException when StreamUtils fails")
    void getMedia_shouldThrowUncheckedIOException_whenStreamUtilsFails() throws IOException {
        Path tempDir = Files.createTempDirectory("mediaserver-test");
        Files.createFile(tempDir.resolve("video.mp4"));

        ReflectionTestUtils.setField(mediaService, "mediaDir", tempDir + File.separator);

        try (MockedStatic<StreamUtils> mocked = mockStatic(StreamUtils.class)) {
            mocked.when(() -> StreamUtils.resourceRegion(any(), eq(headers), anyLong()))
                .thenThrow(new IOException("simulated error"));

            UncheckedIOException exception = assertThrows(UncheckedIOException.class,
                () -> mediaService.getMedia("video.mp4", headers)
            );

            assertThat(exception.getMessage()).contains("video.mp4");
        } finally {
            try (Stream<Path> paths = Files.walk(tempDir)) {
                paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::deleteOnExit);
            }
        }
    }
}
