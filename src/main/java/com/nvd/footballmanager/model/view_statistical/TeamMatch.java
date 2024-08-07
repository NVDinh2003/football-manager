package com.nvd.footballmanager.model.view_statistical;

import org.springframework.beans.factory.annotation.Value;

public interface TeamMatch {
    @Value("#{target.date}")
    String getDate();

    @Value("#{target.match_count}")
    Integer getMatchCount();
}
