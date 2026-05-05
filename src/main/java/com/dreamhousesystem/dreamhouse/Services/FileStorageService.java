package com.dreamhousesystem.dreamhouse.Services;

import com.dreamhousesystem.dreamhouse.exception.UnsupportedFileTypeException;
import com.dreamhousesystem.dreamhouse.fileManager.FileFilter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir;
    private final FileFilter fileFilter;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir, FileFilter fileFilter) {
        this.uploadDir = uploadDir;
        this.fileFilter = fileFilter;
    }

    public String storeFile(MultipartFile file) throws IOException, UnsupportedFileTypeException {
        String originalName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalName);

        // Verifie si le type est supporte
        fileFilter.determineContentType(originalName);

        // Genere un nom unique pour eviter les collisions
        String newName = UUID.randomUUID() + "." + extension;

        Path targetPath = Paths.get(uploadDir).resolve(newName).normalize();
        Files.createDirectories(targetPath.getParent());
        Files.write(targetPath, file.getBytes());

        return newName;
    }

    public byte[] loadFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        return Files.readAllBytes(filePath);
    }
}
