package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.GrammarHistory;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface GrammarHistoryRepository extends JpaRepository<GrammarHistory, Long> {

	List<GrammarHistory> findByUser(User user);

	List<GrammarHistory> findByUserOrderByCreatedAtDesc(User user);

	long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT DATE(gh.createdAt) as date, COUNT(gh) as count FROM GrammarHistory gh WHERE gh.createdAt BETWEEN :start AND :end GROUP BY DATE(gh.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
