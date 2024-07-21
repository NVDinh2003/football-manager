//package com.nvd.footballmanager.seeder.membership_request;
//
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.MembershipRequest;
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.model.enums.MembershipRequestStatus;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.repository.UserRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class MembershipRequestDataReader implements GenericDataReader<MembershipRequest> {
//
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//    private final UserRepository userRepository;
//    private final TeamRepository teamRepository;
//
//    public MembershipRequestDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                                       UserRepository userRepository,
//                                       TeamRepository teamRepository) {
//        this.taskExecutor = taskExecutor;
//        this.userRepository = userRepository;
//        this.teamRepository = teamRepository;
//    }
//
//    @Override
//    public List<MembershipRequest> generateDataBatch(int batchSize) {
//        List<Future<MembershipRequest>> futures = new ArrayList<>();
//        List<User> users = userRepository.findRandomLimit(batchSize);
//        List<Team> teams = teamRepository.findAll();
//
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(users, teams)));
//        }
//
//        List<MembershipRequest> requests = new ArrayList<>(batchSize);
//        for (Future<MembershipRequest> future : futures) {
//            try {
//                requests.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//                System.err.println("Error generating data: " + e.getMessage());
//            }
//        }
//        return requests;
//    }
//
//    public MembershipRequest generateData(List<User> users, List<Team> teams) {
//        Faker faker = fakerThreadLocal.get();
//
//        User randomUser = users.get(faker.random().nextInt(users.size()));
//        Team randomTeam = teams.get(faker.random().nextInt(teams.size()));
//
//        Instant requestDate = faker.date().past(100, TimeUnit.DAYS).toInstant();
//        Instant responseDate = faker.bool().bool() ? faker.date().future(10, TimeUnit.DAYS, Date.from(requestDate)).toInstant() : null;
//
//        return MembershipRequest.builder()
//                .id(UUID.randomUUID())
//                .status(MembershipRequestStatus.values()[faker.random().nextInt(MembershipRequestStatus.values().length)])
//                .requestDate(requestDate)
//                .responseDate(responseDate)
//                .user(randomUser)
//                .team(randomTeam)
//                .build();
//    }
//
//    @Override
//    public MembershipRequest generateData() {
//        return null;
//    }
//}
