package com.university.platform.repository;

import com.university.platform.model.UserToCourse;
import com.university.platform.model.composite.UserToCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToCourseRepository extends JpaRepository<UserToCourse, UserToCourseId> {

}
