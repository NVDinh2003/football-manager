//package com.nvd.footballmanager.seeder.match;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.Match;
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class MatchDataReader implements GenericDataReader<Match> {
//
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//    private final TeamRepository teamRepository;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String STADIUMS = "stadiums_cache";
//
//    public MatchDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                           TeamRepository teamRepository,
//                           RedisTemplate<String, Object> redisTemplate) {
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
//    public List<Match> generateDataBatch(int batchSize) {
//        List<Future<Match>> futures = new ArrayList<>();
//        List<Team> teams = teamRepository.findAll();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData(teams)));
//        }
//
//        List<Match> matches = new ArrayList<>(batchSize);
//        for (Future<Match> future : futures) {
//            try {
//                matches.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return matches;
//    }
//
//    //    @Override
//    public Match generateData(List<Team> teams) {
//        Faker faker = fakerThreadLocal.get();
//        Match match = new Match();
//        match.setId(UUID.randomUUID());
//        match.setTeam1Scored(faker.number().numberBetween(0, 8));
//        match.setTeam2Scored(faker.number().numberBetween(0, 8));
//
//        match.setTime(Instant.now().minus(faker.number().numberBetween(0, 450), ChronoUnit.DAYS));
//
//        List<String> stadiums = (List<String>) redisTemplate.opsForValue().get(STADIUMS);
//        String randomStadium = stadiums.get(faker.random().nextInt(stadiums.size()));
//        match.setVenue(randomStadium);
//        match.setConfirmed(faker.bool().bool());
//
//        Team team1 = teams.get(faker.random().nextInt(teams.size()));
//        Team team2 = teams.get(faker.random().nextInt(teams.size()));
//        while (team1.equals(team2)) {
//            team2 = teams.get(faker.random().nextInt(teams.size()));
//        }
//
//        match.setTeam1(team1);
//        match.setTeam2(team2);
//
//        match.setCreatedAt(faker.date().past(200, TimeUnit.DAYS).toInstant());
//        match.setUpdatedAt(faker.date().between(Date.from(match.getCreatedAt()),
//                Date.from(match.getCreatedAt().plus(5, ChronoUnit.DAYS))).toInstant());
//
//        return match;
//    }
//
//    @Override
//    public Match generateData() {
//        return null;
//    }
//}
