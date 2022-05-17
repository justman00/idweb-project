package com.university.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChapterToCourseResponseDto {

    private Long id;
    private String title;
    private String description;
    private Set<AttachmentResponseDto> attachmentsPath;
}
