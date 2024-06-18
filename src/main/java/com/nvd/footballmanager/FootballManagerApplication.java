package com.nvd.footballmanager;

import com.nvd.footballmanager.config.RSAKeyProperties;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.model.enums.UserRole;
import com.nvd.footballmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableConfigurationProperties(RSAKeyProperties.class)
@SpringBootApplication
public class FootballManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballManagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByRole(UserRole.ADMIN)) {
                User adminUser = new User();
                adminUser.setUsername("admin123");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setName("Admin User");
                adminUser.setEmail("admin@example.com");
                adminUser.setRole(UserRole.ADMIN);
                adminUser.setEnabled(true);

                userRepository.save(adminUser);
                System.out.println("Admin user created successfully!");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}
