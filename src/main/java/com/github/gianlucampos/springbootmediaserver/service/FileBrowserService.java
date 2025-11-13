package com.github.gianlucampos.springbootmediaserver.service;

import com.github.gianlucampos.springbootmediaserver.models.MediaItem;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileBrowserService {

    @Value("${app.media.directory}")
    private String mediaDir;

    public List<MediaItem> listFiles(String path) throws IOException {
        Path root = this.getRoot();
        Path dir = (path == null || path.isBlank()) ? root : root.resolve(path).normalize();

        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IOException("Invalid directory: " + dir);
        }

        List<MediaItem> items = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) {
                String relPath = root.relativize(p).toString().replace("\\", "/");
                items.add(new MediaItem(
                    p.getFileName().toString(),
                    relPath,
                    Files.isDirectory(p)
                ));
            }
        }

        items.sort(Comparator.comparing(MediaItem::isDirectory).reversed()
            .thenComparing(MediaItem::getName, String.CASE_INSENSITIVE_ORDER));

        return items;
    }

    //TODO create a helper class to reusage context
    public Path getRoot() {
        return Paths.get(mediaDir).toAbsolutePath().normalize();
    }

}
