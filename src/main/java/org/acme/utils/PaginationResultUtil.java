package org.acme.utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationResultUtil<T> {

    private List<T> content;
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public PaginationResultUtil(List<T> content, int page, int limit, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = page + 1;
        this.totalPages = (int) Math.ceil((double) totalElements / limit);
        this.hasNext = page < totalPages - 1;
        this.hasPrevious = page > 0;
    }
}
