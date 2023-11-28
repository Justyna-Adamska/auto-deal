package com.example.autodeal;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.model.UserRole;
import com.example.autodeal.user.repository.UserRepository;
import com.example.autodeal.user.repository.UserRoleRepository;
import com.example.autodeal.util.PasswordEncoderUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;


@SpringBootApplication
public class AutoDealApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoDealApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRoleRepository userRoleRepository, UserRepository userRepository) {
		return (args) -> {

			UserRole userRoleAdmin = new UserRole();
			userRoleAdmin.setName("ROLE_ADMIN");
			userRoleRepository.save(userRoleAdmin);

			UserRole userRoleUser = new UserRole();
			userRoleUser.setName("ROLE_USER");
			userRoleRepository.save(userRoleUser);

			UserModel user = new UserModel();
			user.setFirstName("Jan");
			user.setLastName("Kot");
			user.setEmail("jan.kot@gmail.com");
			user.setPhone("501-123-002");

			String rawPassword = "asd";
			String hashedPassword = PasswordEncoderUtil.encode(rawPassword);
			user.setPassword(hashedPassword);

			user.setRoles(Collections.singleton(userRoleUser));

			userRepository.save(user);
		};
	}
}
