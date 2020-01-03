package com.kry.pms.ext.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.kry.pms" })
@EnableCaching
public class ExtApiApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ExtApiApplication.class, args);
	}

}