package com.concrete.desafiojava;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.concrete.desafiojava.model.Role;
import com.concrete.desafiojava.model.RoleName;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.RoleRepository;

@SpringBootApplication
@EntityScan(basePackageClasses= { DesafioJavaConcreteApplication.class })
@EnableJpaAuditing
public class DesafioJavaConcreteApplication {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@PostConstruct
	public void init() {
		roleRepository.save(new Role(RoleName.ROLE_USER));
		roleRepository.save(new Role(RoleName.ROLE_ADMIN));
	}

	public static void main(String[] args) {
		SpringApplication.run(DesafioJavaConcreteApplication.class, args);
	}
	
	@Bean
	public AuditorAware<User> auditorProvider() {
		return new UserAuditorAware();
	}

}
