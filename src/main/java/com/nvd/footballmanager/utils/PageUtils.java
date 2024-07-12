package com.nvd.footballmanager.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtils {
    public static PageRequest buildPageRequest(Integer pageNumber, Integer pageSize, Sort sort) {
        pageNumber = (pageNumber == null || pageNumber == 0)
                ? Constants.DEFAULT_PAGE_NUMBER : pageNumber - Constants.DEFAULT_PAGE_NUMBER;
        pageSize = (pageSize == null || pageSize == 0) ? Constants.DEFAULT_PAGE_SIZE : pageSize;
        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
