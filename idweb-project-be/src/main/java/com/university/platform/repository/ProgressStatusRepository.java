package com.university.platform.repository;

import com.university.platform.model.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProgressStatusRepository extends JpaRepository<ProgressStatus, Long> {
    ProgressStatus getProgressStatusByDescriptionEquals(String equals);

}
