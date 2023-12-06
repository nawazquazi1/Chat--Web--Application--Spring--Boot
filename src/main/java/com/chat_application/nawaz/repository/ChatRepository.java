package com.chat_application.nawaz.repository;

import com.chat_application.nawaz.model.Chat;
import com.chat_application.nawaz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String> {
	
	List<Chat> findBySender(User sender);
	List<Chat> findByRecipient(User recipient);
	
	Optional<Chat> findBySenderAndRecipient(User sender, User recipient);
}
