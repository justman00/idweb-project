package com.university.platform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChapterRequestDto {

    private Long chapterId;
    @NotNull(message = "Chapter should have title")
    private String chapterTitle;
    @NotNull(message = "Chapter should have description")
    private String chapterDescription;
    private Set<MultipartFile> chapterAttachments;
}
