package com.university.platform.utils.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionType {
    INTERNAL_SERVER_ERROR("Internal server error", 500),
    NOT_FOUND_ERROR("Not found", 404),
    OPERATION_WAS_NOT_IMPLEMENTED_YET("Operation was not implemented yet", 501),
    INVALID_AUTHORIZATION("Invalid authorization token", 401),
    COURSE_NOT_FOUND("Course with ID={} was not found", 404),
    USER_NOT_FOUND("User with email={} was not found", 404),
    USER_ALREADY_EXISTS("User with email={} already exists", 409),
    UNABLE_TO_CREATE_DIRECTORY("Could not create the directory where the uploaded files will be stored.", 500),
    INVALID_PATH_SEQUENCE("Filename {} contains invalid path sequence", 500),
    UNABLE_TO_STORE_FILE("Could not store file with name {}", 500),
    ACCESS_VIOLATION("Action can't be performed, because the user doesn't have enough rights.", 403),
    CHAPTER_NOT_FOUND("Chapter with id={} was not found", 404),
    FILE_NOT_FOUND("File was not found", 404),
    USER_TO_COURSE_NOT_FOUND("User with id={} is not enrolled to course with id={}", 404),
    COURSE_STATUS_NOT_FOUND("Course status {} was not found", 404);

    private final String message;
    private final int statusCode;
}
