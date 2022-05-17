package com.university.platform.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProgressStatus {
    ENROLLED("ENROLLED"),
    COMPLETED("COMPLETED");

    @Getter
    private final String value;
}
