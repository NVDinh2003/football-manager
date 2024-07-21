//package com.nvd.footballmanager.seeder.user;
//
//import com.nvd.footballmanager.model.entity.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Duration;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserSeedingService {
//
//    private final UserDataReader userDataReader;
//    private final UserBatchInserter userBatchInserter;
//
//    @Transactional
//    public void seedUsers(int totalUsers, int batchSize) {
//        Instant start = Instant.now();
//
//        int batches = totalUsers / batchSize;
//
//        List<User> allUsers = new ArrayList<>();
//
//        for (int i = 0; i < batches; i++) {
//            Instant begin = Instant.now();
//            allUsers.addAll(userDataReader.generateDataBatch(batchSize));
//            log.info("Generated batch {} of {}", i + 1, batches);
//            log.info("Completed CREATE {} users in {} millis", allUsers.size(), Duration.between(begin, Instant.now()).toMillis());
//        }
//        log.info("Completed CREATE ALL users in {} millis", Duration.between(start, Instant.now()).toMillis());
//
//        Instant beginInsert = Instant.now();
//        log.info("Inserting all batches");
//        userBatchInserter.batchInsert(allUsers);
//        log.info("Inserted all batches in {} millis", Duration.between(beginInsert, Instant.now()).toMillis());
//
//        log.info("Completed seeding {} users in {} millis", totalUsers, Duration.between(start, Instant.now()).toMillis());
//    }
//
//
////    @Transactional
////    public void seedUsers(int totalUsers, int batchSize) {
////        Instant start = Instant.now();
////
////        int batches = totalUsers / batchSize;
////        int remainder = totalUsers % batchSize;
////
////        List<User> allUsers = new ArrayList<>();
////
////        for (int i = 0; i < batches; i++) {
////            Instant begin = Instant.now();
////            allUsers.addAll(userDataReader.generateDataBatch(batchSize));
////            log.info("Generated batch {} of {}", i + 1, batches);
////            log.info("Completed CREATE {} users in {} millis", allUsers.size(), Duration.between(begin, Instant.now()).toMillis());
////        }
////        log.info("Completed CREATE ALL users in {} millis", Duration.between(start, Instant.now()).toMillis());
////
////        if (remainder > 0) {
////            allUsers.addAll(userDataReader.generateDataBatch(remainder));
////            log.info("Generated final batch of {} millis", remainder);
////        }
////        Instant beginInsert = Instant.now();
////        log.info("Inserting all batches");
////        userBatchInserter.batchInsert(allUsers);
////        log.info("Inserted all batches in {} millis", Duration.between(beginInsert, Instant.now()).toMillis());
////
////        log.info("Completed seeding {} users in {} millis", totalUsers, Duration.between(start, Instant.now()).toMillis());
////    }
////
//}