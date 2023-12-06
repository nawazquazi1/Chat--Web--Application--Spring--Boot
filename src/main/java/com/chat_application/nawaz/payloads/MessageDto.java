package com.chat_application.nawaz.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
	private Long messageId;
	private String chatId;
	private Long senderId;
	private Long recipientId;
	private String messageText;
	private Date timestamp;
}
