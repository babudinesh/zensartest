package com.zensar.zensartest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZensartestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZensartestApplication.class, args);
		System.out.println("Hello");
	}

	@Bean
   public RestTemplate getRestTemplate() {
      return new RestTemplate();
   }
}

