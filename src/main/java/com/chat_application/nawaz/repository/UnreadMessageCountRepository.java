package com.chat_application.nawaz.repository;

import com.chat_application.nawaz.model.UnreadMessageCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnreadMessageCountRepository extends JpaRepository<UnreadMessageCount, Long> {
	Optional<UnreadMessageCount> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
	void deleteBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
