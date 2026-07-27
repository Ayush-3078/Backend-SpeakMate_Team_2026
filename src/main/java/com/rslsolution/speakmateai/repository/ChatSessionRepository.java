package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.ChatSession;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

	List<ChatSession> findByUserOrderByUpdatedAtDesc(User user);

	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT DATE(cs.createdAt) as date, COUNT(cs) as count FROM ChatSession cs WHERE cs.createdAt BETWEEN :start AND :end GROUP BY DATE(cs.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
