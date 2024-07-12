package com.nvd.footballmanager.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedFilter extends BaseFilter {
    private UUID userId;
    private String title;
    private String content;
}
