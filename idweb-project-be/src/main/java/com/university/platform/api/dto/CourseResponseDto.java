package com.university.platform.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CourseResponseDto {

    private Long id;
    private String title;
    private String description;
    private String thumbnail;
    private LocalDate date;
    private String authorName;
    private Long authorId;
    private String status;
    private Set<ChapterToCourseResponseDto> chapters;
}
