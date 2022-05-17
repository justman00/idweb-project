package com.university.platform.service;

import com.university.platform.api.dto.ChapterRequestDto;
import com.university.platform.api.dto.CourseRequestDto;
import com.university.platform.api.dto.CourseResponseDto;
import com.university.platform.model.Attachment;
import com.university.platform.model.Chapter;
import com.university.platform.model.Course;
import com.university.platform.model.CourseStatus;
import com.university.platform.model.ProgressStatus;
import com.university.platform.model.UserToCourse;
import com.university.platform.model.composite.UserToCourseId;
import com.university.platform.repository.CourseRepository;
import com.university.platform.repository.CourseStatusRepository;
import com.university.platform.repository.ProgressStatusRepository;
import com.university.platform.repository.UserToCourseRepository;
import com.university.platform.utils.exceptions.ExceptionType;
import com.university.platform.utils.exceptions.PlatformException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.university.platform.model.enums.CourseStatus.DRAFT;
import static com.university.platform.model.enums.CourseStatus.PUBLISHED;
import static com.university.platform.model.enums.ProgressStatus.ENROLLED;
import static com.university.platform.utils.mappers.ChapterMapper.mapChapterRequestDtoToChapter;
import static com.university.platform.utils.mappers.CourseMapper.mapCourseRequestDtoToCourse;
import static com.university.platform.utils.mappers.CourseMapper.mapCourseToCourseResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseStatusRepository courseStatusRepository;
    private final UserToCourseRepository userToCourseRepository;
    private final ProgressStatusRepository progressStatusRepository;

    private final FileService fileService;
    private final ChapterService chapterService;
    private final UserService userService;
    private final AttachmentService attachmentService;


    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllPublishedCourses() {
        return courseRepository.findAllByCourseStatus(PUBLISHED.getValue())
                .stream()
                .map(mapCourseToCourseResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCoursesByUser() {
        return courseRepository.findAllByAuthorId(userService.getUser().getId())
                .stream()
                .map(mapCourseToCourseResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(final Long courseId) {
        Course course = checkCourse(courseId);

        return mapCourseToCourseResponseDto.apply(course);
    }

    @Transactional
    public String deleteCourseById(final Long courseId) {
        log.info("User with id=[{}] is trying to delete course with id=[{}]", userService.getUser().getId(), courseId);

        Course course = checkCourse(courseId);

        courseRepository.delete(course);

        log.info("Course with id=[{}] was deleted by user with id=[{}]", courseId, userService.getUser().getId());
        return String.format("Course with id=%s was successfully deleted", courseId);
    }

    @Transactional
    public CourseResponseDto createCourse(final CourseRequestDto courseRequestDto, MultipartFile thumbnail) {
        Course course = mapCourseRequestDtoToCourse.apply(courseRequestDto);

        course.setThumbnail(fileService.determineFilePath(thumbnail));
        course.setAuthor(userService.getUser());

        CourseStatus courseStatus = courseStatusRepository
                .getCourseStatusByStatusTitleEquals(DRAFT.getValue());

        course.setCourseStatus(courseStatus);
        course.setCreatedDate(LocalDate.now());
        course.setLastUpdateDate(LocalDate.now());

        courseRepository.save(course);

        return mapCourseToCourseResponseDto.apply(course);
    }

    @Transactional
    public CourseResponseDto updateCourse(final Long courseId, final CourseRequestDto courseRequestDto,
            MultipartFile thumbnail) {
        Course course = checkCourse(courseId);

        final Long loggedUserId = userService.getUser().getId();

        if (!course.getAuthor().getId().equals(loggedUserId)) {
            log.warn("User with id=[{}] tried to edit course with id=[{}] for which he is not an author", loggedUserId,
                    courseId);
            throw PlatformException.of(ExceptionType.ACCESS_VIOLATION, loggedUserId);
        }

        course.setThumbnail(fileService.determineFilePath(thumbnail));
        course.setDescription(courseRequestDto.getCourseDescription());
        course.setTitle(courseRequestDto.getCourseTitle());
        course.setLastUpdateDate(LocalDate.now());

        courseRepository.save(course);

        return mapCourseToCourseResponseDto.apply(checkCourse(courseId));
    }

    @Transactional
    public CourseResponseDto updateCourseChapters(final Long courseId, final ChapterRequestDto chapterRequestDto,
            final MultipartFile[] chapterAttachments) {
        Course course = checkCourse(courseId);

        final Long loggedUserId = userService.getUser().getId();

        if (!course.getAuthor().getId().equals(loggedUserId)) {
            log.warn("User with id=[{}] tried to edit course with id=[{}] for which he is not an author", loggedUserId,
                    courseId);
            throw PlatformException.of(ExceptionType.ACCESS_VIOLATION, loggedUserId);
        }

        course.setLastUpdateDate(LocalDate.now());

        Chapter chapter = mapChapterRequestDtoToChapter.apply(chapterRequestDto);

        if (chapter.getId() != null) {
            chapter = chapterService.checkCourseChapter(chapterRequestDto);
            chapter.setDescription(chapterRequestDto.getChapterDescription());
            chapter.setTitle(chapterRequestDto.getChapterTitle());
        }

        Set<Attachment> attachments = getAttachments(chapterAttachments, chapter);

        chapter.setAttachments(attachments);
        chapter.setCourse(course);

        attachmentService.saveAttachments(attachments);

        Set<Chapter> chapterSet = course.getChapterSet();
        chapterSet.add(chapter);

        course.setChapterSet(chapterSet);
        chapterService.saveChapter(chapter);
        course = courseRepository.save(course);

        return mapCourseToCourseResponseDto.apply(course);
    }

    @Transactional
    public String joinCourse(final Long courseId) {
        Course course = checkCourse(courseId);

        final Long loggedUserId = userService.getUser().getId();

        if (course.getAuthor().getId().equals(loggedUserId)) {
            log.warn("User with id=[{}] can't join course with id=[{}] for which he is an author", loggedUserId,
                    courseId);
            throw PlatformException.of(ExceptionType.ACCESS_VIOLATION, loggedUserId);
        }

        ProgressStatus progressStatus = progressStatusRepository
                .getProgressStatusByDescriptionEquals(ENROLLED.getValue());

        UserToCourse userToCourse = new UserToCourse(loggedUserId, courseId, progressStatus);
        userToCourseRepository.save(userToCourse);

        log.info("Course with id=[{}] was joined by user with id=[{}]", courseId, loggedUserId);
        return String.format("Course with id=%s was successfully joined", courseId);
    }

    @Transactional
    public String unrollCourse(final Long courseId) {
        Course course = checkCourse(courseId);

        final Long loggedUserId = userService.getUser().getId();

        if (course.getAuthor().getId().equals(loggedUserId)) {
            log.warn("User with id=[{}] can't unroll course with id=[{}] for which he is an author", loggedUserId,
                    courseId);
            throw PlatformException.of(ExceptionType.ACCESS_VIOLATION, loggedUserId);
        }

        UserToCourseId userToCourseId = new UserToCourseId(loggedUserId, courseId);

        UserToCourse userToCourse = userToCourseRepository.findById(userToCourseId)
                .orElseThrow(() -> {
                    log.warn("User with id=[{}] can't be unrolled for course with id=[{}]", loggedUserId,
                            courseId);
                    throw PlatformException.of(ExceptionType.USER_TO_COURSE_NOT_FOUND, loggedUserId, courseId);
                });

        userToCourseRepository.delete(userToCourse);

        log.info("User with id=[{}] was unrolled from course with id=[{}]", loggedUserId, courseId);
        return String.format("User was successfully unrolled from course with id=%s", courseId);
    }

    public CourseResponseDto changeCourseStatus(final Long courseId, String status) {
        Course course = checkCourse(courseId);
        CourseStatus courseStatus = courseStatusRepository.getCourseStatusByStatusTitleEquals(status.toUpperCase());

        if (courseStatus == null) {
            throw PlatformException.of(ExceptionType.COURSE_STATUS_NOT_FOUND, status);
        }

        course.setCourseStatus(courseStatus);
        course = courseRepository.save(course);

        return mapCourseToCourseResponseDto.apply(course);
    }

    private Course checkCourse(final Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> {
                    log.warn("Course with id=[{}] was not found in the database", courseId);
                    throw PlatformException.of(ExceptionType.COURSE_NOT_FOUND, courseId);
                }
        );
    }

    private Set<Attachment> getAttachments(MultipartFile[] chapterAttachments, Chapter chapter) {
        return Arrays.stream(chapterAttachments)
                .map(fileService::determineFilePath)
                .map(path -> new Attachment(path, chapter))
                .collect(Collectors.toSet());
    }
}
