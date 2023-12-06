package com.chat_application.nawaz.services;

import com.chat_application.nawaz.model.Chat;
import com.chat_application.nawaz.payloads.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
	
	List<UserDto> getChatsForUser(Long userId);
	Chat getChat(Long senderId, Long recipientId);
}
