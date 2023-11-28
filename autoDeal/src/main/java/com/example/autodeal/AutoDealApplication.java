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


	}

