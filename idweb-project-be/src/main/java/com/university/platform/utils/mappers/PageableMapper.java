package com.university.platform.utils.mappers;

import com.university.platform.api.dto.PageableDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class PageableMapper {

    public static <T, V> PageableDto<V> mapPageToPageableDto(Page<T> page, Function<T, V> contentMapper) {
        return PageableDto.<V>builder()
                .data(page.get().map(contentMapper).collect(Collectors.toList()))
                .page(page.getNumber())
                .totalElementsCount(page.getTotalElements())
                .totalPageCount(page.getTotalPages())
                .build();
    }
}