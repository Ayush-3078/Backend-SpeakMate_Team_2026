package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.entity.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

	List<Vocabulary> findByUser(User user);

	List<Vocabulary> findByUserOrderByCreatedAtDesc(User user);

	List<Vocabulary> findByUserAndFavoriteTrue(User user);

	long countByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

	@Query("SELECT DATE(v.createdAt) as date, COUNT(v) as count FROM Vocabulary v WHERE v.createdAt BETWEEN :start AND :end GROUP BY DATE(v.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
