package com.chat_application.nawaz.payloads;

import lombok.Data;

@Data
public class JwtAuthenticationRequest {
	
	private String username;
	private String password;
}
