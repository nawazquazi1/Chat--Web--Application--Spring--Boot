package com.chat_application.nawaz.controllers;

import com.chat_application.nawaz.config.AppConstants;
import com.chat_application.nawaz.exceptions.ApiException;
import com.chat_application.nawaz.model.Status;
import com.chat_application.nawaz.model.User;
import com.chat_application.nawaz.payloads.*;
import com.chat_application.nawaz.repository.UserRepository;
import com.chat_application.nawaz.security.JwtTokenHelper;
import com.chat_application.nawaz.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/messenger/authentication")
public class AuthenticationController {
	
	private final JwtTokenHelper jwtTokenHelper;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final UserService userService;
	
	// Temporary DB while registration process
	private final RedisTemplate<String, String> redisTemplate;
	
	public AuthenticationController(JwtTokenHelper jwtTokenHelper, AuthenticationManager authenticationManager,
			UserRepository userRepository, UserService userService,
			RedisTemplate<String, String> redisTemplate) {
		super();
		this.jwtTokenHelper = jwtTokenHelper;
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.userService = userService;
		this.redisTemplate = redisTemplate;
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> createToken(@RequestBody JwtAuthenticationRequest request,
											   HttpServletResponse response) throws Exception {
		this.authenticate(request.getUsername(), request.getPassword());
		
		User user = this.userRepository
				.findByUsername(request.getUsername())
				.orElseThrow();
		
		user.setStatus(Status.ONLINE);
		String token = this.jwtTokenHelper.generateToken(user);
		Cookie cookie = new Cookie("jwtToken", token);
		cookie.setHttpOnly(true);
//      cookie.setSecure(true); // Set to true if using HTTPS
        cookie.setPath("/");
        response.addCookie(cookie);
        
        UserDto userDto = UserDto.builder()
        		.id(user.getId())
        		.email(user.getEmail())
        		.username(user.getUsername())
        		.lastSeen(user.getLastSeen())
//        		.status(user.getStatus())
        		.build();
		
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> createUser(
			@Valid @RequestBody UserDto userDto,
			HttpServletResponse response){
		
		// saving username and email till the verification completed
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		operations.set(userDto.getUsername(), userDto.getEmail(), 3, TimeUnit.MINUTES);
		
		// Sending email
		this.userService.sendVerificationToEmail(userDto.getUsername(), userDto.getEmail());
			
		// Generating JWT token
		User user = User.builder()
				.username(userDto.getUsername())
				.email(userDto.getEmail())
				.status(Status.ONLINE)
				.build();
		System.out.println(user);
		String token = this.jwtTokenHelper.generateToken(user);
		System.out.println(token);
		Cookie cookie = new Cookie("jwtToken", token);
		cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@PostMapping("/register/verify")
	public ResponseEntity<VerificationResponse> verifyNewUserEmail(
			@RequestBody VerificationRequest verificationRequest,
			HttpServletRequest request){
		
		String token = this.userService.extractJwtFromRequest(request);
		System.out.println(token+"trf");
		boolean success = this.userService.verifyConfirmationCode(
								this.jwtTokenHelper.getUsernameFromToken(token), 
								verificationRequest.getCode());
		
		VerificationResponse verificationResponse = new VerificationResponse();
		if(success) {
			verificationResponse.setMessage(AppConstants.SUCCESSFUL_VERIFICATION_RESPONSE);
		} else {
			verificationResponse.setMessage(AppConstants.UNSUCCESSFUL_VERIFICATION_RESPONSE);
		}
		verificationResponse.setSuccess(success);
		return new ResponseEntity<VerificationResponse>(verificationResponse, HttpStatus.OK);
	}
	
	@PostMapping("/register/setPassword")
	public ResponseEntity<UserDto> setPasswordForNewUser(
			@RequestBody NewPasswordRequest newPasswordRequest,
			HttpServletRequest request) {
		
		String token = this.userService.extractJwtFromRequest(request);
		String username = this.jwtTokenHelper.getUsernameFromToken(token);
		
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		String email = operations.get(username);
		
		User user = User.builder()
			.username(username)
			.email(email)
			.password(newPasswordRequest.getPassword())
			.status(Status.ONLINE)
			.build();
		
		UserDto newUser = this.userService.registerNewUser(user);
		return new ResponseEntity<UserDto>(newUser, HttpStatus.OK);
	}
	
	private void authenticate(String username, String password) throws Exception {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new ApiException("Invalid Username or password !!");
		}
	}
}
