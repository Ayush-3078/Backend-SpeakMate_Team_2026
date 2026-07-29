package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	boolean existsByEmail(String email); // Check duplicate email during registration.

	Optional<User> findByEmail(String email); // Used for login and fetching a user by email.

	Optional<User> findByResetPasswordToken(String resetPasswordToken);

	long countByActiveTrue();
	
	long countByActiveFalse();
	
	long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Query("SELECT COUNT(l) FROM LessonProgress l WHERE l.user.id = :userId")
	long countLessonProgressByUserId(@Param("userId") Long userId);

	@Query("SELECT COUNT(s) FROM SpeakingSession s WHERE s.user.id = :userId")
	long countSpeakingSessionsByUserId(@Param("userId") Long userId);

	@Query("SELECT COUNT(g) FROM GrammarHistory g WHERE g.user.id = :userId")
	long countGrammarHistoriesByUserId(@Param("userId") Long userId);

	@Query("SELECT COUNT(v) FROM Vocabulary v WHERE v.user.id = :userId")
	long countVocabularyByUserId(@Param("userId") Long userId);

}
