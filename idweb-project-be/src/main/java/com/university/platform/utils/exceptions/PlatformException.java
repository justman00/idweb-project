package com.university.platform.utils.exceptions;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

public class PlatformException extends RuntimeException {

    @Getter
    private final ExceptionType exceptionType;

    public PlatformException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    private PlatformException(ExceptionType exceptionType, Object... messageArgs) {
        super(MessageFormatter.arrayFormat(exceptionType.getMessage(), messageArgs).getMessage());
        this.exceptionType = exceptionType;
    }

    public static PlatformException of(ExceptionType type, Exception cause) {
        return new PlatformException(type, cause);
    }

    public static PlatformException of(ExceptionType type) {
        return new PlatformException(type);
    }

    public static PlatformException of(ExceptionType type, Object... messageArgs) {
        return new PlatformException(type, messageArgs);
    }
}
