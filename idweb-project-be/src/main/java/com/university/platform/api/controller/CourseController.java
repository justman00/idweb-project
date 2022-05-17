package com.university.platform.api.controller;

import com.university.platform.api.dto.ChapterRequestDto;
import com.university.platform.api.dto.CourseRequestDto;
import com.university.platform.api.dto.CourseResponseDto;
import com.university.platform.api.exchange.Response;
import com.university.platform.service.CourseService;
import com.university.platform.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Response<List<CourseResponseDto>>> getAllActiveCourses() {
        return ok(Response.build(courseService.getAllPublishedCourses()));
    }

    @GetMapping(path = "/users")
    public ResponseEntity<Response<List<CourseResponseDto>>> getCourseByUser() {
        return ok(Response.build(courseService.getAllCoursesByUser()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Response<CourseResponseDto>> getCourseById(@PathVariable Long id) {
        return ok(Response.build(courseService.getCourseById(id)));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Response<String>> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok(Response.build(courseService.deleteCourseById(id)));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Response<CourseResponseDto>> createCourse(
            @RequestPart("thumbnail") @NotNull(message = "Course should have thumbnail") MultipartFile thumbnail,
            @RequestPart("course") @Valid CourseRequestDto courseRequestDto, Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }

        return ResponseEntity.ok(Response.build(courseService.createCourse(courseRequestDto, thumbnail)));
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<Response<CourseResponseDto>> updateCourse(@PathVariable Long id,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("course") @Valid CourseRequestDto courseRequestDto, Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity
                .ok(Response.build(courseService.updateCourse(id, courseRequestDto, thumbnail)));
    }

    @PutMapping(path = "/{id}/chapters", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<Response<CourseResponseDto>> updateCourseChapter(@PathVariable Long id,
            @RequestPart("chapter") @Valid ChapterRequestDto chapterRequestDto,
            @RequestPart("chapterAttachments") MultipartFile[] chapterAttachments, Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }

        return ResponseEntity
                .ok(Response.build(courseService.updateCourseChapters(id, chapterRequestDto, chapterAttachments)));
    }

    @PostMapping(path = "/{id}/users")
    public ResponseEntity<Response<String>> enrollUserToCourse(@PathVariable Long id) {
        return ResponseEntity.ok(Response.build(courseService.joinCourse(id)));
    }

    @DeleteMapping(path = "/{id}/users")
    public ResponseEntity<Response<String>> unrollUserToCourse(@PathVariable Long id) {
        return ResponseEntity.ok(Response.build(courseService.unrollCourse(id)));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Response<CourseResponseDto>> changeCourseStatus(@PathVariable Long id,
            @RequestParam(value = "status") String status) {
        return ResponseEntity.ok(Response.build(courseService.changeCourseStatus(id, status)));
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileService.getResource(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileService.getContentType(request, resource)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
