package com.kry.pms.ext.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.kry.pms" })
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})//不初始化连接池
public class ExtApiApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ExtApiApplication.class, args);
	}

}