package com.university.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "course_management", name = "t_progress_status")
@Builder(toBuilder = true)
public class ProgressStatus {

    @Id
    @Column(name = "progress_status_id")
    @SequenceGenerator(name = "progress_status_id_generator", sequenceName = "course_management.seq_t_progress_status", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "progress_status_id_generator")
    private Long id;

    @Column(name = "progress_status_title")
    private String description;

    @OneToMany(mappedBy = "progressStatus", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserToCourse> progressStatusToUserToCourse;
}
