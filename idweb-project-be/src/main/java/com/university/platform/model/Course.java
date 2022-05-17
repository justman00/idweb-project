package com.university.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "course_management", name = "t_course")
@Builder(toBuilder = true)
public class Course {

    @Id
    @Column(name = "course_id")
    @SequenceGenerator(name = "course_id_generator", sequenceName = "course_management.seq_t_course", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_generator")
    private Long id;

    @Column(name = "course_title")
    private String title;

    @Column(name = "course_description")
    private String description;

    @Column(name = "course_thumbnail_path")
    private String thumbnail;

    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_status_id")
    private CourseStatus courseStatus;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Chapter> chapterSet;
}
