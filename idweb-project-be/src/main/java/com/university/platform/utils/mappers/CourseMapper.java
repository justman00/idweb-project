package com.university.platform.utils.mappers;

import com.university.platform.api.dto.ChapterToCourseResponseDto;
import com.university.platform.api.dto.CourseRequestDto;
import com.university.platform.api.dto.CourseResponseDto;
import com.university.platform.model.Chapter;
import com.university.platform.model.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.university.platform.utils.mappers.ChapterMapper.mapChapterToChapterToCourseResponseDto;
import static org.apache.commons.lang3.StringUtils.SPACE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseMapper {

    public static final Function<Course, CourseResponseDto> mapCourseToCourseResponseDto = course ->
            CourseResponseDto.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .thumbnail(course.getThumbnail())
                    .authorName(
                            course.getAuthor().getFirstName()
                                    .concat(SPACE)
                                    .concat(course.getAuthor().getLastName()))
                    .authorId(course.getAuthor().getId())
                    .description(course.getDescription())
                    .thumbnail(course.getThumbnail())
                    .date(course.getLastUpdateDate())
                    .status(course.getCourseStatus().getStatusTitle())
                    .chapters(getChapters(course.getChapterSet()))
                    .build();

    public static final Function<CourseRequestDto, Course> mapCourseRequestDtoToCourse = courseRequestDto ->
            Course.builder()
                    .title(courseRequestDto.getCourseTitle())
                    .description(courseRequestDto.getCourseDescription())
                    .build();


    private static Set<ChapterToCourseResponseDto> getChapters(Set<Chapter> chapters) {
        if (chapters != null) {
            return chapters.stream()
                    .map(mapChapterToChapterToCourseResponseDto)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
