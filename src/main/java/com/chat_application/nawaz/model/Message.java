package com.chat_application.nawaz.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

	@ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "chatId")
	private Chat chat;
	
	@ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;
	
	@ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
	private User recipient;
	
	@Column(nullable = false, length = 500)
    private String messageText;
	
	@Column(nullable = false)
	private Date timestamp;
	
}
