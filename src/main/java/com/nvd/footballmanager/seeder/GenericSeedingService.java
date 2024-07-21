package com.nvd.footballmanager.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class GenericSeedingService<T> {

    private final GenericDataReader<T> dataReader;
    private final GenericBatchInserter<T> batchInserter;

    @Transactional
    public void seedEntities(int totalEntities, int batchSize) {
        Instant start = Instant.now();

        int batches = totalEntities / batchSize;

        List<T> allEntities = new ArrayList<>();

        for (int i = 0; i < batches; i++) {
            Instant begin = Instant.now();
            allEntities.addAll(dataReader.generateDataBatch(batchSize));
            log.info("Generated batch {} of {}", i + 1, batches);
            log.info("Completed CREATE {} entities in {} millis", allEntities.size(), Duration.between(begin, Instant.now()).toMillis());
        }
        log.info("Completed CREATE ALL entities in {} millis", Duration.between(start, Instant.now()).toMillis());

        Instant beginInsert = Instant.now();
        log.info("Inserting all batches");
        batchInserter.batchInsert(allEntities);
        log.info("Inserted all batches in {} millis", Duration.between(beginInsert, Instant.now()).toMillis());

        log.info("Completed seeding {} entities in {} millis", totalEntities, Duration.between(start, Instant.now()).toMillis());
    }
}

