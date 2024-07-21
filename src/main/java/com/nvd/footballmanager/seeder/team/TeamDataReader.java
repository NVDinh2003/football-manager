//package com.nvd.footballmanager.seeder.team;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.Team;
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
//public class TeamDataReader implements GenericDataReader<Team> {
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//
//    public TeamDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor) {
//        this.taskExecutor = taskExecutor;
//    }
//
//    @Override
//    public List<Team> generateDataBatch(int batchSize) {
//        List<Future<Team>> futures = new ArrayList<>();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(this::generateData));
//        }
//        List<Team> teams = new ArrayList<>(batchSize);
//        for (Future<Team> future : futures) {
//            try {
//                teams.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return teams;
//    }
//
//    @Override
//    public Team generateData() {
//        Faker faker = fakerThreadLocal.get();
//        Team team = new Team();
//        team.setId(UUID.randomUUID());
//        team.setName("FC " + faker.team().name());
//        team.setAddress(faker.address().fullAddress());
//        team.setRankPoints(faker.number().numberBetween(0, 1000));
//        team.setCreatedAt(faker.date().past(1000, TimeUnit.DAYS).toInstant());
//        team.setUpdatedAt(faker.date().between(Date.from(team.getCreatedAt()),
//                Date.from(team.getCreatedAt().plus(30, ChronoUnit.DAYS))).toInstant());
//        return team;
//    }
//
//}
//
