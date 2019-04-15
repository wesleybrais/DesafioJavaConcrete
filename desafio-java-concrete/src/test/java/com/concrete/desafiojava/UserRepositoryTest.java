package com.concrete.desafiojava;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void whenFindingUserById_thenCorrect() {
		userRepository.save(new User("Wesley", "wesley@com", "1234", Arrays.asList()));
		assertThat(userRepository.findById(1L)).isInstanceOf(Optional.class);
	}
	
	@Test
	public void whenSavingUser_thenCorrect() {
		userRepository.save(new User("Joao", "joao@com", "898", Arrays.asList()));
		User user = userRepository.findById(3L).orElseGet(() -> new User("Danilo", "danilo@email", "3456", Arrays.asList()));
		assertThat(user.getName()).isEqualTo("Joao");
	}
	
	@Test
	public void whenFindByEmail_thenCorrect() {
		userRepository.save(new User("Maria", "maria@com", "234", Arrays.asList()));
		User user = userRepository.findByEmail("maria@com").orElseGet(() -> new User("Danilo", "danilo@email", "3456", Arrays.asList()));
		assertThat(user.getName()).isEqualTo("Maria");
	}
	
}
