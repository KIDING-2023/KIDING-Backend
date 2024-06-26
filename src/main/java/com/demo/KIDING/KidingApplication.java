package com.demo.KIDING;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling  // 스케줄링 활성화
@SpringBootApplication
public class KidingApplication {

	public static void main(String[] args) {
		SpringApplication.run(KidingApplication.class, args);
	}

}
