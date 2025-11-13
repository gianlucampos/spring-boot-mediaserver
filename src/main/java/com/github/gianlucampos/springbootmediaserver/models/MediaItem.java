package com.github.gianlucampos.springbootmediaserver.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaItem {

    private String name;
    private String path;
    private boolean directory;
}
