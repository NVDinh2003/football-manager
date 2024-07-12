package com.nvd.footballmanager.filters;

import com.nvd.footballmanager.utils.Constants;
import com.nvd.footballmanager.utils.PageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseFilter {
    private int pageSize = Constants.DEFAULT_PAGE_SIZE;
    private int pageNumber = Constants.DEFAULT_PAGE_NUMBER;
    private String s;
    private String sortBy; // name field
    private String sortDirection;   // ASC or DESC

    public Pageable getPageable() {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = Sort.by(Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC"), sortBy);
        }
        return PageUtils.buildPageRequest(this.pageNumber, this.pageSize, sort);
    }

}
