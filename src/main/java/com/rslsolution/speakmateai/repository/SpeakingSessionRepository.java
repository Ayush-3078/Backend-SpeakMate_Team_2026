package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.SpeakingSession;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface SpeakingSessionRepository extends JpaRepository<SpeakingSession, Long> {

	List<SpeakingSession> findByUser(User user);

	List<SpeakingSession> findByUserOrderByCreatedAtDesc(User user);

	long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT COALESCE(SUM(s.duration), 0) FROM SpeakingSession s")
	Integer sumDuration();

	@Query("SELECT AVG(s.fluencyScore) FROM SpeakingSession s WHERE s.fluencyScore IS NOT NULL")
	Double findAverageFluencyScore();

	@Query("SELECT AVG(s.grammarScore) FROM SpeakingSession s WHERE s.grammarScore IS NOT NULL")
	Double findAverageGrammarScore();

	@Query("SELECT DATE(s.createdAt) as date, COUNT(s) as count FROM SpeakingSession s WHERE s.createdAt BETWEEN :start AND :end GROUP BY DATE(s.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT DATE(s.createdAt) as date, COALESCE(SUM(s.duration), 0L) as total FROM SpeakingSession s WHERE s.createdAt BETWEEN :start AND :end GROUP BY DATE(s.createdAt)")
	List<Object[]> sumDurationByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
