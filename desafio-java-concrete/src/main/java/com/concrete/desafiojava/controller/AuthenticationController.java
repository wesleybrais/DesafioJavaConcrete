package com.concrete.desafiojava.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concrete.desafiojava.exception.AppException;
import com.concrete.desafiojava.model.Role;
import com.concrete.desafiojava.model.RoleName;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.payload.ApiResponse;
import com.concrete.desafiojava.payload.LoginRequest;
import com.concrete.desafiojava.payload.RegisterRequest;
import com.concrete.desafiojava.payload.UserResponse;
import com.concrete.desafiojava.repository.RoleRepository;
import com.concrete.desafiojava.repository.UserRepository;
import com.concrete.desafiojava.security.TokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		
		if (!userRepository.existsByEmail(loginRequest.getEmail())) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Usuário e/ou senha inválidos"), HttpStatus.UNAUTHORIZED);
		}

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		User user = userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new AppException("Couldn't find Email or password"));

		// String jwt = tokenProvider.generateToken(user.getId());

		return ResponseEntity.ok(new UserResponse(user.getId(), user.getCreated(), user.getModified(),
				user.getLastLogin(), user.getAccessToken()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new ApiResponse("The email already exists"));
		}

		User user = new User(registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPassword(),
				registerRequest.getPhones());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role role = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("Could not set role"));

		user.setRoles(Collections.singleton(role));

		String jwt = tokenProvider.generateToken(registerRequest.getEmail());

		user.setAccessToken(jwt);

		userRepository.saveAndFlush(user);

		return ResponseEntity.ok(new UserResponse(user.getId(), user.getCreated(), user.getModified(),
				user.getLastLogin(), user.getAccessToken()));
	}

}
