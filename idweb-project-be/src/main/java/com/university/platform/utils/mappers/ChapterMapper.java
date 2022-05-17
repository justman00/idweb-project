package com.university.platform.utils.mappers;

import com.university.platform.api.dto.AttachmentResponseDto;
import com.university.platform.api.dto.ChapterRequestDto;
import com.university.platform.api.dto.ChapterToCourseResponseDto;
import com.university.platform.model.Chapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChapterMapper {

    public static final Function<ChapterRequestDto, Chapter> mapChapterRequestDtoToChapter = chapterRequestDto ->
            Chapter.builder()
                    .id(chapterRequestDto.getChapterId())
                    .title(chapterRequestDto.getChapterTitle())
                    .description(chapterRequestDto.getChapterDescription())
                   // .attachments(getAttachments())
                    .build();

    public static final Function<Chapter, ChapterToCourseResponseDto> mapChapterToChapterToCourseResponseDto =
            chapter -> ChapterToCourseResponseDto.builder()
                    .id(chapter.getId())
                    .description(chapter.getDescription())
                    .title(chapter.getTitle())
                    .attachmentsPath(getAttachments(chapter))
                    .build();

    private static Set<AttachmentResponseDto> getAttachments(Chapter chapter) {
        if (chapter.getAttachments() != null) {
            return chapter.getAttachments()
                    .stream()
                    .map(attachment -> AttachmentResponseDto.builder()
                            .path(attachment.getPath())
                            .title(extractName(attachment.getPath()))
                            .build())
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }


    private static String extractName(final String path) {
        return StringUtils.substringAfterLast(path, "/");
    }
}
