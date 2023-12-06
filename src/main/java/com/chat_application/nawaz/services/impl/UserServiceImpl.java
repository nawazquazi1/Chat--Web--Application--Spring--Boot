package com.chat_application.nawaz.services.impl;

import com.chat_application.nawaz.config.AppConstants;
import com.chat_application.nawaz.model.Role;
import com.chat_application.nawaz.model.User;
import com.chat_application.nawaz.payloads.UserDto;
import com.chat_application.nawaz.repository.RoleRepository;
import com.chat_application.nawaz.repository.UserRepository;
import com.chat_application.nawaz.services.UserService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {
	
	// Temporary DB while registration process
	private RedisTemplate<String, String> redisTemplate;
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	public UserServiceImpl(RedisTemplate<String, String> redisTemplate, UserRepository userRepository,
			RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
		super();
		this.redisTemplate = redisTemplate;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDto registerNewUser(User user) {
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		Role role = this.roleRepository.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		
		User newUser = this.userRepository.save(user);
		return this.modelMapper.map(newUser, UserDto.class);
	}

	@Override
	public boolean sendVerificationToEmail(String username, String email) {
		String randomCode = generateConfirmationCode();
		
		Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.transport.protocol", "smtp");
        
        // code will be sent from this email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("otaboyev149@gmail.com", "zoebjqhtjtstfjcb");
            }
        });
        
        try {

            // preparing message to send
            MimeMessage code = new MimeMessage(session);
            code.setSubject("Confirmation from Messenger application");
            code.setText("Confirmation code: " + randomCode);

            // adding email address
            Address addressTo = new InternetAddress(email);
            code.setRecipient(Message.RecipientType.TO, addressTo);

            // sending....
            Transport.send(code);
            
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
    		operations.set(username, randomCode, 3, TimeUnit.MINUTES);
    		
    		return true;

        } catch (Exception e){
            e.printStackTrace();
        }
        
        return false;
	}
	
	@Override
	public boolean verifyConfirmationCode(String email, String password) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		String generatedPassword = operations.get(email);

        //			operations.getAndExpire(email, 1, TimeUnit.SECONDS);
        return password.equals(generatedPassword);
    }

	private String generateConfirmationCode() {
		Random random = new Random();
		System.out.println(100000 + random.nextInt(900000));
		return Integer.toString(100000 + random.nextInt(900000));
	}

	@Override
	public String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.out.println("this is cookies" + cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}
}
