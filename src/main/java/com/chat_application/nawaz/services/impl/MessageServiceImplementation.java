package com.chat_application.nawaz.services.impl;

import com.chat_application.nawaz.model.Chat;
import com.chat_application.nawaz.model.Message;
import com.chat_application.nawaz.payloads.MessageDto;
import com.chat_application.nawaz.repository.MessageRepository;
import com.chat_application.nawaz.repository.UserRepository;
import com.chat_application.nawaz.services.ChatService;
import com.chat_application.nawaz.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService {
	
	private final MessageRepository messageRepository;
	private final ChatService chatService;
	private final UserRepository userRepository;
	
	@Override
	public List<MessageDto> getMessagesForChat(Long myId, Long chatUserId) {
		
		Chat chat = this.chatService.getChat(myId, chatUserId);
		
		List<Message> messages = this.messageRepository.findByChat(chat);
		if(messages == null) messages = new ArrayList<>();
		
		List<MessageDto> messageDtos = messages.stream().map(message -> {
			return new MessageDto(message.getMessageId(),
					message.getChat().getChatId(),
					message.getSender().getId(),
					message.getRecipient().getId(),
					message.getMessageText(),
					message.getTimestamp());
		}).collect(Collectors.toList());
		
		return messageDtos;
	}

	@Override
	public MessageDto saveNewMessage(MessageDto messageDto) {
		System.out.println();
		System.out.println();
		System.out.println();
		if(Objects.equals(messageDto.getSenderId(), messageDto.getRecipientId())) {
			System.out.println("they are the same");
		}
		
		Chat chat = this.chatService
				.getChat(messageDto.getSenderId(), messageDto.getRecipientId());
		
		Message message = Message.builder()
				.sender(this.userRepository.findById(messageDto.getSenderId()).orElseThrow())
				.recipient(this.userRepository.findById(messageDto.getRecipientId()).orElseThrow())
				.chat(chat)
				.messageText(messageDto.getMessageText())
				.timestamp(messageDto.getTimestamp())
				.build();
		this.messageRepository.save(message);
		messageDto.setChatId(message.getChat().getChatId());
		return messageDto;
	}
}
