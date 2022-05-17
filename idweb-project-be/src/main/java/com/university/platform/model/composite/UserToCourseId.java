package com.university.platform.model.composite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserToCourseId implements Serializable {

    private Long userId;
    private Long courseId;
}
