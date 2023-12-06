package com.chat_application.nawaz.controllers;

import com.chat_application.nawaz.model.User;
import com.chat_application.nawaz.payloads.UserDto;
import com.chat_application.nawaz.services.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/messenger/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("all/users")
	public List<User> findAllUser(){
		return userService.findAllUsers();
	}
	
	
}
