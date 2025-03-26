package com.sudhir003.spring_security;

import com.sudhir003.spring_security.model.Role;
import com.sudhir003.spring_security.model.User;
import com.sudhir003.spring_security.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run(UserService service){
		return args -> {
			service.saveRole(new Role(null,"ROLE_USER"));
			service.saveRole(new Role(null,"ROLE_MANAGER"));
			service.saveRole(new Role(null,"ROLE_ADMIN"));
			service.saveRole(new Role(null,"ROLE_SUPER_ADMIN"));

			service.saveUser(new User(null,"Sudhir","sudhir03","1234",new ArrayList<>()));
			service.saveUser(new User(null,"Kumaer","kumar1","1234",new ArrayList<>()));

			service.addRoleToUser("sudhir03","ROLE_USER");
			service.addRoleToUser("kumar1","ROLE_ADMIN");
		};
	}

}

