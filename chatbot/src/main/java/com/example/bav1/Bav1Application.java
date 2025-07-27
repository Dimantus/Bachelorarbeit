package com.example.bav1;

import com.example.bav1.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(EnvConfig.class)
public class Bav1Application {

    public static void main(String[] args) {
        SpringApplication.run(Bav1Application.class, args);
    }

}
