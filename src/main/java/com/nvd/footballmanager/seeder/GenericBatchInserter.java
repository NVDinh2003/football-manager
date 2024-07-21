package com.nvd.footballmanager.seeder;

import java.util.List;

public interface GenericBatchInserter<T> {
    void batchInsert(List<T> entities);
}