package com.tiktok.project;

import com.tiktok.project.entity.Role;
import com.tiktok.project.entity.User;
import com.tiktok.project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class TiktokApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiktokApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveRole(new Role(1, "ROLE_USER"));
//			userService.saveRole(new Role(2, "ROLE_SHOP"));
//			userService.saveRole(new Role(3, "ROLE_ADMIN"));
//
//			userService.saveUser(new User("admin-user", "admin"));
//			userService.saveUser(new User("admin-shop", "admin"));
//			userService.saveUser(new User("admin", "admin"));
//
//			userService.addToUser("admin-user", "ROLE_USER");
//			userService.addToUser("admin-shop", "ROLE_SHOP");
//			userService.addToUser("admin", "ROLE_ADMIN");
//
//		};
//	}

}
