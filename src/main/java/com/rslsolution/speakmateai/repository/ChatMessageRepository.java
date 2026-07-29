package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.ChatMessage;
import com.rslsolution.speakmateai.entity.ChatSession;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);

	long countBySessionUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT DATE(cm.createdAt) as date, COUNT(cm) as count FROM ChatMessage cm WHERE cm.createdAt BETWEEN :start AND :end GROUP BY DATE(cm.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
