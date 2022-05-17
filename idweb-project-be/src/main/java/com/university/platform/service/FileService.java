package com.university.platform.service;

import com.university.platform.utils.FileStorageConfiguration;
import com.university.platform.utils.exceptions.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.university.platform.utils.exceptions.ExceptionType.FILE_NOT_FOUND;
import static com.university.platform.utils.exceptions.ExceptionType.INVALID_PATH_SEQUENCE;
import static com.university.platform.utils.exceptions.ExceptionType.UNABLE_TO_CREATE_DIRECTORY;
import static com.university.platform.utils.exceptions.ExceptionType.UNABLE_TO_STORE_FILE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.StringUtils.cleanPath;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final Path fileStorageLocation;

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Autowired
    public FileService(final FileStorageConfiguration fileStorageConfiguration) {
        this.fileStorageLocation = Paths.get(fileStorageConfiguration.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new PlatformException(UNABLE_TO_CREATE_DIRECTORY);
        }
    }

    public String storeFile(final MultipartFile file) {
        // Normalize file name
        String fileName = cleanPath(requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw PlatformException.of(INVALID_PATH_SEQUENCE, fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw PlatformException.of(UNABLE_TO_STORE_FILE, fileName);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw PlatformException.of(FILE_NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            throw PlatformException.of(FILE_NOT_FOUND);
        }
    }

    public Resource getResource(String fileName) {
        return loadFileAsResource(fileName);
    }

    public String getContentType(final HttpServletRequest request, final Resource resource) {
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return contentType;
    }

    public String determineFilePath(final MultipartFile thumbnail) {
        String fileName = storeFile(thumbnail);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/courses/downloadFile/")
                .path(fileName)
                .toUriString();
    }
}


