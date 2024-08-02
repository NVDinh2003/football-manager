package com.nvd.footballmanager;

import com.nvd.footballmanager.config.RSAKeyProperties;
import com.nvd.footballmanager.model.entity.Match;
import com.nvd.footballmanager.model.entity.Team;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.TeamRepository;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.seeder.GenericSeedingService;
import com.nvd.footballmanager.seeder.SeederService.MemberMatchService;
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
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    private static final String USER_ID_CACHE = Constants.USER_ID_CACHE;
    private static final String TEAM_ID_CACHE = Constants.TEAM_ID_CACHE;
    private static final String MY_TEAM_ID_CACHE = Constants.MY_TEAM_ID_CACHE;

//    private UserSeedingService userSeedingService;

    @Autowired
    private GenericSeedingService<Match> entitySeedingService;

    @Autowired
    private MemberMatchService memberMatchService;

    public static void main(String[] args) {
        SpringApplication.run(FootballManagerApplication.class, args);
    }

    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
    public void init() {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(USER_ID_CACHE))) {
            List<User> users = userRepository.findRandomLimit(200);

            List<String> user_ids = users.stream()
                    .map(user -> user.getId().toString())
                    .collect(Collectors.toList());

            String user1 = userRepository.findByUsername("dinh367126").get().getId().toString();
            String user2 = userRepository.findByUsername("nguyen637007").get().getId().toString();
            String user3 = userRepository.findByUsername("tesstusername").get().getId().toString();
            user_ids.add(user1);
            user_ids.add(user2);
            user_ids.add(user3);

            redisTemplate.opsForValue().set(USER_ID_CACHE, user_ids);
        }

        String myTeamId = "c02a825b-2859-440c-8f77-94818d7de3d0";

        if (Boolean.FALSE.equals(redisTemplate.hasKey(TEAM_ID_CACHE))) {
            List<Team> teams = teamRepository.findRandomLimit(100);

            List<String> team_ids = teams.stream()
                    .map(team -> team.getId().toString())
                    .collect(Collectors.toList());

            team_ids.add(myTeamId);
            team_ids.add("56e0110a-229b-48ef-a066-07a086a3f72b");

            redisTemplate.opsForValue().set(TEAM_ID_CACHE, team_ids);
        }

        if (Boolean.FALSE.equals(redisTemplate.hasKey(MY_TEAM_ID_CACHE))) {
            redisTemplate.opsForValue().set(MY_TEAM_ID_CACHE, myTeamId);
        }

    }

    @Bean
    public CommandLineRunner seedData() {


        return args -> {
            logger.info("Starting data seeding process...");
//            entitySeedingService.seedEntities(300_000, 60_000);
//            entitySeedingService.seedEntities(150_000, 15000);
            memberMatchService.setupMemberMatchRelationships();
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
