package com.chat_application.nawaz.services;

import com.chat_application.nawaz.payloads.MessageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
	List<MessageDto> getMessagesForChat(Long senderId, Long recipientId);
	MessageDto saveNewMessage(MessageDto messageDto);
}
