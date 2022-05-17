package com.university.platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(schema = "course_management", name = "t_attachment")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Attachment {

    @Id
    @Column(name = "attachment_id")
    @SequenceGenerator(name = "attachment_id_generator", sequenceName = "course_management.seq_t_attachment", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_id_generator")
    private Long id;

    @Column(name = "attachment_path")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public Attachment(String path, Chapter chapter) {
        this.path = path;
        this.chapter = chapter;
    }
}
