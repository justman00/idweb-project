package com.university.platform.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageConfiguration {

    @Getter
    @Setter
    private String uploadDir;
}
