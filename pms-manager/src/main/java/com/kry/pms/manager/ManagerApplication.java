package com.kry.pms.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = { "com.kry.pms" })
@EnableJpaRepositories(basePackages = { "com.kry.pms.dao" })
@EnableTransactionManagement
@EntityScan(basePackages = { "com.kry.pms.model" })
@EnableCaching
public class ManagerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
