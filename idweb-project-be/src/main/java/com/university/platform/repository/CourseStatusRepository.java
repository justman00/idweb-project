package com.university.platform.repository;

import com.university.platform.model.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseStatusRepository extends JpaRepository<CourseStatus, Long> {
    CourseStatus getCourseStatusByStatusTitleEquals(String equals);
}
