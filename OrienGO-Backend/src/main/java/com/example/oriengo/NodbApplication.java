package com.example.oriengo;

import com.example.oriengo.config.RestTemplateConfig;
import com.example.oriengo.scrape.ScrapeTestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class,
                org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class
        },
        scanBasePackageClasses = {
                ScrapeTestController.class,
                RestTemplateConfig.class
        }
)
public class NodbApplication {
    public static void main(String[] args) {
        SpringApplication.run(NodbApplication.class, args);
    }
}
