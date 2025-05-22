package com.real.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class MovieSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieSpringBootApplication.class, args);
	}

}
