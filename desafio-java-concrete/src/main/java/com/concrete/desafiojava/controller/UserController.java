package com.concrete.desafiojava.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concrete.desafiojava.exception.AppException;
import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.payload.ApiResponse;
import com.concrete.desafiojava.payload.UserResponse;
import com.concrete.desafiojava.repository.UserRepository;
import com.concrete.desafiojava.util.JwtUtils;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	@RequestMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id, HttpServletRequest request) {

		User user = userRepository.findById(id).orElseThrow(() -> new AppException("User doesn't exists"));

		String jwt = JwtUtils.getJwtFromRequest(request);

		if (user.getAccessToken().equals(jwt)) {
			return ResponseEntity.ok(new UserResponse(user.getId(), user.getCreated(), user.getModified(),
					user.getLastLogin(), user.getAccessToken()));
		}
		
		return new ResponseEntity<ApiResponse>(new ApiResponse("Não Autorizado"), HttpStatus.UNAUTHORIZED);

	}

}
