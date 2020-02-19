package com.kry.pms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author Louis
 *
 */
@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = { "com.kry.pms" })
@EnableJpaRepositories(basePackages = { "com.kry.pms.dao" })
@EnableTransactionManagement
@EntityScan(basePackages = { "com.kry.pms.model" })
@EnableCaching
public class ApiApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApiApplication.class, args);
	}
}
