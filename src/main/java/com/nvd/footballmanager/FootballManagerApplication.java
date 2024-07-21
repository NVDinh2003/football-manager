package com.nvd.footballmanager;

import com.nvd.footballmanager.config.RSAKeyProperties;
import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.seeder.GenericSeedingService;
import com.nvd.footballmanager.utils.Constants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(RSAKeyProperties.class)
@RequiredArgsConstructor
@SpringBootApplication
public class FootballManagerApplication {
    private static final Logger logger = LoggerFactory.getLogger(FootballManagerApplication.class);
    @Autowired
    private GenericSeedingService<Comment> entitySeedingService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRepository userRepository;
    private static final String USER_ID_CACHE = Constants.USER_ID_CACHE;

//    private UserSeedingService userSeedingService;


    public static void main(String[] args) {
        SpringApplication.run(FootballManagerApplication.class, args);
    }

    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
    public void init() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(USER_ID_CACHE))) {
            List<User> users = userRepository.findRandomLimit(200_000);

            List<String> user_ids = users.stream()
                    .map(user -> user.getId().toString())
                    .collect(Collectors.toList());

            redisTemplate.opsForValue().set(USER_ID_CACHE, user_ids);
        }

    }

    @Bean
    public CommandLineRunner seedData() {


        return args -> {
            logger.info("Starting data seeding process...");
            entitySeedingService.seedEntities(300_000, 30_000);
//            entitySeedingService.seedEntities(5, 5);
//            userSeedingService.seedUsers(100, 10);
            logger.info("Data seeding process completed.");
        };
    }

//    @Bean
//    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (!userRepository.existsByRole(UserRole.ADMIN)) {
//                User adminUser = new User();
//                adminUser.setUsername("admin123");
//                adminUser.setPassword(passwordEncoder.encode("admin123"));
//                adminUser.setName("Admin User");
//                adminUser.setEmail("admin@example.com");
//                adminUser.setRole(UserRole.ADMIN);
//                adminUser.setEnabled(true);
//
//                userRepository.save(adminUser);
//                System.out.println("Admin user created successfully!");
//            } else {
//                System.out.println("Admin user already exists.");
//            }
//        };
//    }
}
