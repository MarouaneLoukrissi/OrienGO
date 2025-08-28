package com.example.oriengo;

import com.example.oriengo.scrape.ScrapeTestController;
import com.example.oriengo.config.RestTemplateConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// Exclut toute config DB/JPA/Flyway
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class
        }
)
// Ne scanne que ce qu’il faut pour le test (évite de charger AdminService, etc.)
@ComponentScan(basePackageClasses = {ScrapeTestController.class, RestTemplateConfig.class})
public class NodbApplication {
    public static void main(String[] args) {
        SpringApplication.run(NodbApplication.class, args);
    }
}