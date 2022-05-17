package com.university.platform.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageableDto<V> {
    private Collection<V> data;
    private int page;
    private long totalElementsCount;
    private int totalPageCount;
}
