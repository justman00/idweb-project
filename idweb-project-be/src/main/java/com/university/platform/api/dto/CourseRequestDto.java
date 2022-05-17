package com.university.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CourseRequestDto {

    @NotNull(message = "Course should have title")
    private String courseTitle;
    @NotNull(message = "Course should have description")
    private String courseDescription;
    private MultipartFile thumbnail;
}
