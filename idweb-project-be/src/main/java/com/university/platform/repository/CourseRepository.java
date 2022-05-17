package com.university.platform.repository;

import com.university.platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c FROM Course c JOIN c.courseStatus cs WHERE cs.statusTitle = :courseStatus")
    List<Course> findAllByCourseStatus(@Param("courseStatus") String courseStatus);

    @Query(value = "SELECT c FROM Course c WHERE  c.author.id = :authorId")
    List<Course> findAllByAuthorId(@Param("authorId") Long authorId);

}
