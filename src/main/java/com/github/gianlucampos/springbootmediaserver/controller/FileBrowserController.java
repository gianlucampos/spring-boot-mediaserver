package com.github.gianlucampos.springbootmediaserver.controller;

import com.github.gianlucampos.springbootmediaserver.models.MediaItem;
import com.github.gianlucampos.springbootmediaserver.service.FileBrowserService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app/v1/media")
@AllArgsConstructor
public class FileBrowserController {

    private FileBrowserService service;

    @GetMapping("/files")
    public String listFiles(@RequestParam(required = false) String path, Model model) throws IOException {
        List<MediaItem> items = service.listFiles(path);
        Path root = service.getRoot();
        Path dir = (path == null || path.isBlank()) ? root : root.resolve(path).normalize();

        List<String> breadcrumbs = new ArrayList<>();
        Path relative = root.relativize(dir);
        for (int i = 0; i < relative.getNameCount(); i++) {
            breadcrumbs.add(relative.subpath(0, i + 1).toString());
        }

        List<String> breadcrumbNames = new ArrayList<>();
        for (String crumb : breadcrumbs) {
            String[] parts = crumb.split("/");
            breadcrumbNames.add(parts[parts.length - 1]);
        }

        model.addAttribute("items", items);
        model.addAttribute("dir", root.relativize(dir).toString().replace("\\", "/"));
        model.addAttribute("breadcrumbs", breadcrumbs);
        model.addAttribute("breadcrumbNames", breadcrumbNames);

        return "files";
    }

}
