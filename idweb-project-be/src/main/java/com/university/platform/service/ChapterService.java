package com.university.platform.service;

import com.university.platform.api.dto.ChapterRequestDto;
import com.university.platform.model.Chapter;
import com.university.platform.repository.ChapterRepository;
import com.university.platform.utils.exceptions.ExceptionType;
import com.university.platform.utils.exceptions.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public Set<Chapter> checkCourseChapters(Set<ChapterRequestDto> chapters) {
        return chapters.stream()
                .map(chapterRequestDto -> chapterRepository.findById(chapterRequestDto.getChapterId())
                        .orElseThrow(() -> {
                            log.warn("Chapter with id=[{}] was not found in the database",
                                    chapterRequestDto.getChapterId());
                            throw PlatformException
                                    .of(ExceptionType.CHAPTER_NOT_FOUND, chapterRequestDto.getChapterId());
                        }))
                .collect(Collectors.toSet());
    }


    public Chapter checkCourseChapter(ChapterRequestDto chapterRequestDto) {
        return chapterRepository.findById(chapterRequestDto.getChapterId())
                .orElseThrow(() -> {
                    log.warn("Chapter with id=[{}] was not found in the database",
                            chapterRequestDto.getChapterId());
                    throw PlatformException
                            .of(ExceptionType.CHAPTER_NOT_FOUND, chapterRequestDto.getChapterId());
                });
    }

    public void saveChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }
}
