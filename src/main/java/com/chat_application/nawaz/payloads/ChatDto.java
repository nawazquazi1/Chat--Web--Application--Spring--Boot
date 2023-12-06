package com.chat_application.nawaz.payloads;

import com.chat_application.nawaz.model.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ChatDto {
	
	private String chatId;
	private Long user1;
	private Long user2;
	private Date createdOn;
    private Set<Message> messages = new HashSet<>();
}
