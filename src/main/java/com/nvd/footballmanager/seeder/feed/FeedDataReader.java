//package com.nvd.footballmanager.seeder.feed;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.Feed;
//import com.nvd.footballmanager.model.entity.MatchRequest;
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.repository.MatchRequestRepository;
//import com.nvd.footballmanager.repository.UserRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class FeedDataReader implements GenericDataReader<Feed> {
//
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//    private final UserRepository userRepository;
//    private final MatchRequestRepository matchRequestRepository;
//
//    public FeedDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                          UserRepository userRepository,
//                          MatchRequestRepository matchRequestRepository) {
//        this.taskExecutor = taskExecutor;
//        this.userRepository = userRepository;
//        this.matchRequestRepository = matchRequestRepository;
//    }
//
//    @Override
//    public List<Feed> generateDataBatch(int batchSize) {
//        List<Future<Feed>> futures = new ArrayList<>();
//        List<User> users = userRepository.findRandomLimit(batchSize);
//        List<MatchRequest> matchRequests = matchRequestRepository.findRandomLimit(batchSize);
//
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(users, matchRequests)));
//        }
//
//        List<Feed> feeds = new ArrayList<>(batchSize);
//        for (Future<Feed> future : futures) {
//            try {
//                feeds.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return feeds;
//    }
//
//    public Feed generateData(List<User> users, List<MatchRequest> matchRequests) {
//        Faker faker = fakerThreadLocal.get();
//        Feed feed = new Feed();
//        feed.setId(UUID.randomUUID());
//        feed.setTitle(faker.lorem().sentence());
//        feed.setContent(faker.lorem().paragraph());
//
//        feed.setUser(users.get(faker.number().numberBetween(0, users.size())));
//
//        if (faker.bool().bool()) { // 50/50 gán MatchRequest
//            feed.setMatchRequest(matchRequests.get(faker.number().numberBetween(0, matchRequests.size())));
//        } else {
//            feed.setMatchRequest(null);
//        }
//
//        feed.setCreatedAt(faker.date().past(300, TimeUnit.DAYS).toInstant());
//        feed.setUpdatedAt(faker.date().between(Date.from(feed.getCreatedAt()),
//                Date.from(feed.getCreatedAt().plus(3, ChronoUnit.DAYS))).toInstant());
//
//        return feed;
//    }
//
//    @Override
//    public Feed generateData() {
//        return null;
//    }
//}
