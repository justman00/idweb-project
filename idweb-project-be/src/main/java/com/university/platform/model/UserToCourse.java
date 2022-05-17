package com.university.platform.model;

import com.university.platform.model.composite.UserToCourseId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@IdClass(UserToCourseId.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "course_management", name = "t_user_to_course", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "user_id"})
})
@SQLDelete(sql = "UPDATE course_management.t_user_to_course SET progress_status_id = 2 WHERE user_id=? AND course_id=?")
@Where(clause = "progress_status_id=1")
@Builder(toBuilder = true)
public class UserToCourse {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_status_id")
    private ProgressStatus progressStatus;
}
