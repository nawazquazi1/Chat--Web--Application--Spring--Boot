package com.chat_application.nawaz.repository;

import com.chat_application.nawaz.model.Chat;
import com.chat_application.nawaz.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findByChat(Chat chat);
}
