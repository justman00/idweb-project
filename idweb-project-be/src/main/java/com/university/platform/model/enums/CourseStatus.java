package com.university.platform.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CourseStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED");

    @Getter
    private final String value;
}
