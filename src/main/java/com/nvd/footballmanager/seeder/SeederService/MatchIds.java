package com.nvd.footballmanager.seeder.SeederService;

import org.springframework.beans.factory.annotation.Value;

public interface MatchIds {
    @Value("#{target.id}")
    String getid();
}
