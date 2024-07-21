//package com.nvd.footballmanager.seeder.user;
//
//import com.github.javafaker.Faker;
//import com.nvd.footballmanager.model.entity.User;
//import com.nvd.footballmanager.model.enums.UserRole;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.security.crypto.password.PasswordEncoder;
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
//public class UserDataReader
////        extends AbstractDataReader<User>
//{
//    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(Faker::new);
//    private final PasswordEncoder passwordEncoder;
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final TaskExecutor taskExecutor;
//    private final String encodedPassword;
//
//
//    private static final String CACHED_PASSWORD = "dinhhtvq123";
//
//    public UserDataReader(PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate,
//                          @Qualifier("dataExecutor") TaskExecutor taskExecutor) {
//        this.passwordEncoder = passwordEncoder;
//        this.redisTemplate = redisTemplate;
//        this.taskExecutor = taskExecutor;
//        this.encodedPassword = passwordEncoder.encode(CACHED_PASSWORD);
//    }
//
//    private static final String NAME_CACHE_KEY = "name_cache";
//    private static final int CACHE_SIZE = 100;
//
//
//    @PostConstruct
//    public void init() {
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(NAME_CACHE_KEY))) {
//            Set<String> names = new HashSet<>();
//            while (names.size() < CACHE_SIZE) {
//                names.add(fakerThreadLocal.get().name().fullName());
//            }
//            redisTemplate.opsForSet().add(NAME_CACHE_KEY, names.toArray());
//        }
//    }
//
//    public List<User> generateDataBatch(int batchSize) {
//        List<Future<User>> futures = new ArrayList<>();
//        for (int i = 0; i < batchSize; i++) {
//            futures.add(((ThreadPoolTaskExecutor) taskExecutor).submit(this::generateData));
//        }
//        List<User> users = new ArrayList<>(batchSize);
//        for (Future<User> future : futures) {
//            try {
//                users.add(future.get());
//            } catch (InterruptedException | ExecutionException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        return users;
//    }
//
//
//    //    @Override
//    public User generateData() {
//        Faker faker = fakerThreadLocal.get();
//        User user = new User();
//        user.setId(UUID.randomUUID());
//        user.setUsername(generateUniqueUsername(faker));
//        user.setPassword(encodedPassword);
//        user.setName(getOrGenerateName());
//        user.setDob(faker.date().birthday(15, 40).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//        user.setEmail(faker.internet().emailAddress());
//        user.setPhoneNumber("0" + faker.number().numberBetween(300000000, 999999999));
//        user.setRole(UserRole.USER);
//        user.setCreatedAt(faker.date().past(1000, TimeUnit.DAYS).toInstant());
//        user.setUpdatedAt(faker.date().between(Date.from(user.getCreatedAt()),
//                Date.from(user.getCreatedAt().plus(30, ChronoUnit.DAYS))).toInstant());
//        user.setEnabled(true);
//        return user;
//    }
//
//    private String getOrGenerateName() {
//        String name = (String) redisTemplate.opsForSet().randomMember(NAME_CACHE_KEY);
//        if (name == null) {
//            name = fakerThreadLocal.get().name().fullName();
//            redisTemplate.opsForSet().add(NAME_CACHE_KEY, name);
//        }
//        return name;
//    }
//
//    private String generateUniqueUsername(Faker faker) {
//        return new StringBuilder(faker.name().username())
//                .append(faker.number().randomNumber(5, true))
//                .toString();
//    }
//
////    public void printNameCache() {
////        Set<Object> names = redisTemplate.opsForSet().members(NAME_CACHE_KEY);
////        if (names != null) {
////            System.out.println("Names in cache:");
////            for (Object name : names) {
////                System.out.println(name);
////            }
////        } else {
////            System.out.println("Name cache is empty or doesn't exist.");
////        }
////    }
//}