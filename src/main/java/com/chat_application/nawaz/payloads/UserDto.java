package com.chat_application.nawaz.payloads;

import com.chat_application.nawaz.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	
	private Long id;
	
	@NotEmpty
	@Size(min=4, max=20, message="Username must be between 4 and 20 characters")
	private String username;
	
	@Email(message = "Email address is not valid !!!")
	private String email;
	private String password;
	private Status status;
	private Date lastSeen;
	private Set<RoleDto> roles = new HashSet<>();
}