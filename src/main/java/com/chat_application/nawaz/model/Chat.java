package com.chat_application.nawaz.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chat {
	
	@Id
    private String chatId;
	
	@ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;
    
    @Column(nullable = false)
	private Date createdOn;
    
    @OneToMany(mappedBy = "chat")
    private Set<Message> messages = new HashSet<>();
}











