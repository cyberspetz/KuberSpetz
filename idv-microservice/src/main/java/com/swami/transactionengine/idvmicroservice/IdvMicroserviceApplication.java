package com.swami.transactionengine.idvmicroservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class IdvMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdvMicroserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Beans loaded: ");
			Arrays.stream(ctx.getBeanDefinitionNames()).forEach(System.out::println);
		};
	}
}
