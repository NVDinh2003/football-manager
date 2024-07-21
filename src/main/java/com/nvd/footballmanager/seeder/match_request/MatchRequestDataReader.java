//package com.nvd.footballmanager.seeder.match_request;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.MatchRequest;
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.model.enums.MatchRequestStatus;
//import com.nvd.footballmanager.model.enums.MatchType;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
//@Component
//public class MatchRequestDataReader implements GenericDataReader<MatchRequest> {
//
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//    private final TeamRepository teamRepository;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String STADIUMS = "stadiums_cache";
//
//    public MatchRequestDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                                  TeamRepository teamRepository,
//                                  RedisTemplate<String, Object> redisTemplate) {
//        this.taskExecutor = taskExecutor;
//        this.teamRepository = teamRepository;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
//    public void init() {
//
//        List<String> stadiums = Arrays.asList(
//                "Wembley Stadium", "Camp Nou", "Old Trafford", "Santiago Bernabéu Stadium",
//                "Anfield", "San Siro", "Maracanã", "Allianz Arena", "Signal Iduna Park",
//                "Stamford Bridge", "Etihad Stadium", "Giuseppe Meazza Stadium", "Emirates Stadium",
//                "Louis II Stadium", "Parc des Princes", "Estadio Azteca", "Veltins-Arena",
//                "Luzhniki Stadium", "Westfalenstadion", "Vodafone Park", "Red Bull Arena"
//        );
//        redisTemplate.opsForValue().set(STADIUMS, stadiums);
//
//    }
//
//    @Override
//    public List<MatchRequest> generateDataBatch(int batchSize) {
//        List<Future<MatchRequest>> futures = new ArrayList<>();
//        List<Team> teams = teamRepository.findAll();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(teams)));
//        }
//
//        List<MatchRequest> matchRequests = new ArrayList<>(batchSize);
//        for (Future<MatchRequest> future : futures) {
//            try {
//                matchRequests.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//                System.err.println("Error generating data: " + e.getMessage());
//            }
//        }
//        return matchRequests;
//    }
//
//
//    public MatchRequest generateData(List<Team> teams) {
//        Faker faker = fakerThreadLocal.get();
//        MatchRequest matchRequest = new MatchRequest();
//        matchRequest.setId(UUID.randomUUID());
//
//        LocalDateTime now = LocalDateTime.now();  // 2 thang truoc
//        LocalDateTime randomPastDate = now.minusDays(faker.number().numberBetween(1, 60));
//        // 2 thang toi
//        LocalDateTime randomFutureDate = now.plusDays(faker.number().numberBetween(1, 60));
//        // randon giua +- 2 thang
//        LocalDateTime randomDateTime = faker.bool().bool() ? randomPastDate : randomFutureDate;
//        matchRequest.setTime(randomDateTime);
//
//        List<String> stadiums = (List<String>) redisTemplate.opsForValue().get(STADIUMS);
//        String randomStadium = stadiums.get(faker.random().nextInt(stadiums.size()));
//        matchRequest.setVenue(randomStadium);
//        matchRequest.setLocationDetails(faker.address().fullAddress());
//        matchRequest.setMatchType(MatchType.values()[faker.number().numberBetween(0, MatchType.values().length)]);
//        matchRequest.setNote(faker.lorem().sentence());
//        matchRequest.setStatus(MatchRequestStatus.values()[faker.number().numberBetween(0, MatchRequestStatus.values().length)]);
//        matchRequest.setTeam(teams.get(faker.random().nextInt(teams.size())));
//
//        return matchRequest;
//    }
//
//    @Override
//    public MatchRequest generateData() {
//        return null;
//    }
//}
