package com.chat_application.nawaz.services;

import com.chat_application.nawaz.model.User;
import com.chat_application.nawaz.payloads.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
	
	// Registration
	UserDto registerNewUser(User user);
	boolean sendVerificationToEmail(String username, String email);
	boolean verifyConfirmationCode(String email, String password);
	String extractJwtFromRequest(HttpServletRequest request);
	List<User> findAllUsers();
	
}
