package com.concrete.desafiojava;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.concrete.desafiojava.model.User;
import com.concrete.desafiojava.repository.UserRepository;

public class UserAuditorAware implements AuditorAware<User> {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional<User> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication == null || !authentication.isAuthenticated() ||
				authentication instanceof AnonymousAuthenticationToken) {
			return Optional.empty();
		}
		
		return userRepository.findByEmail(authentication.getName());
	}
}
