package com.nvd.footballmanager.seeder;

import java.util.List;

public interface GenericDataReader<T> {
    List<T> generateDataBatch(int batchSize);

    T generateData();
}