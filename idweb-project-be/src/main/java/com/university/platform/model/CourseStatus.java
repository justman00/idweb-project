package com.university.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "course_management", name = "t_course_status")
@Builder(toBuilder = true)
public class CourseStatus {

    @Id
    @Column(name = "course_status_id")
    @SequenceGenerator(name = "course_status_id_generator", sequenceName = "course_management.seq_t_course_status", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_status_id_generator")
    private Long id;

    @Column(name = "course_status_title")
    private String statusTitle;
}
