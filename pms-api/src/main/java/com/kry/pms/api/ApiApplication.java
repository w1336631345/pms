package com.kry.pms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@ComponentScan(basePackages = { "com.kry.pms" })
@EnableJpaRepositories(basePackages = { "com.kry.pms.dao" })
@EntityScan(basePackages = { "com.kry.pms.model" })
public class ApiApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApiApplication.class, args);
	}

}
