package com.nvd.footballmanager;

import com.nvd.footballmanager.config.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RSAKeyProperties.class)
@SpringBootApplication
public class FootballManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballManagerApplication.class, args);
    }

}
