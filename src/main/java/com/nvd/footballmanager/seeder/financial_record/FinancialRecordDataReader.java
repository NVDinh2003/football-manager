//package com.nvd.footballmanager.seeder.financial_record;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.FinancialRecord;
//import com.nvd.footballmanager.model.enums.FinancialRecordType;
//import com.nvd.footballmanager.repository.TeamRepository;
//import com.nvd.footballmanager.seeder.GenericDataReader;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class FinancialRecordDataReader implements GenericDataReader<FinancialRecord> {
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final TaskExecutor taskExecutor;
//    private final TeamRepository teamRepository;
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String TEAM_FEE_KEY = "team_fee_cache";
//
//    public FinancialRecordDataReader(@Qualifier("dataExecutor") TaskExecutor taskExecutor,
//                                     TeamRepository teamRepository,
//                                     RedisTemplate<String, Object> redisTemplate) {
//        this.taskExecutor = taskExecutor;
//        this.teamRepository = teamRepository;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
//    public void init() {
//
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(TEAM_FEE_KEY))) {
//            List<String> fees = Arrays.asList(
//                    "player_salaries", "coaching_salaries", "training_expenses", "equipment_costs",
//                    "medical_costs", "travel_expenses", "registration_fees", "stadium_costs",
//                    "admin_costs", "marketing_costs", "transfer_costs", "legal_costs"
//            );
//            redisTemplate.opsForValue().set(TEAM_FEE_KEY, fees);
//        }
//
////        cacheTeams();
////        cacheUsers();
//    }
//
//    @Override
//    public List<FinancialRecord> generateDataBatch(int batchSize) {
////        List<Team> teams = teamRepository.findAll();
//        List<Future<FinancialRecord>> futures = new ArrayList<>();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(() -> generateData()));
//        }
//        List<FinancialRecord> records = new ArrayList<>(batchSize);
//        for (Future<FinancialRecord> future : futures) {
//            try {
//                records.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return records;
//    }
//
//    @Override
//    public FinancialRecord generateData() {
//        Faker faker = fakerThreadLocal.get();
//
//        FinancialRecord record = new FinancialRecord();
//        record.setId(UUID.randomUUID());
//        record.setType(faker.options().option(FinancialRecordType.class));
//        List<String> notes = (List<String>) redisTemplate.opsForValue().get(TEAM_FEE_KEY);
//        String randomNote = notes.get(faker.random().nextInt(notes.size()));
//        record.setNote(randomNote);
//        int multiplier = faker.number().numberBetween(30, 211);
//        record.setAmount(multiplier * 5000);
//        record.setDate(faker.date().past(500, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
////        record.setTeam(teams.get(faker.random().nextInt(teams.size())));
//        record.setCreatedAt(faker.date().past(1000, TimeUnit.DAYS).toInstant());
//        record.setUpdatedAt(faker.date().between(Date.from(record.getCreatedAt()),
//                Date.from(record.getCreatedAt().plus(5, ChronoUnit.DAYS))).toInstant());
//
//        return record;
//    }
//
////    @Override
////    public FinancialRecord generateData() {
////        return null;
////    }
//}
