package com.dreamhousesystem.dreamhouse.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class FileUrlBuilder {

    public String buildUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        // Construit automatiquement l’URL complete en fonction du domaine/port
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(filename)
                .toUriString();
    }
}
