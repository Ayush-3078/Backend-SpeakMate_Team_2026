package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
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

	@Query("SELECT COUNT(ss) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId")
	long countByUserSchoolId(@Param("schoolId") Long schoolId);

	@Query("SELECT COALESCE(SUM(ss.duration), 0L) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId")
	Integer sumDurationByUserSchoolId(@Param("schoolId") Long schoolId);

	@Query("SELECT COUNT(ss) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId AND ss.createdAt BETWEEN :start AND :end")
	long countByUserSchoolIdAndCreatedAtBetween(@Param("schoolId") Long schoolId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT COALESCE(SUM(ss.duration), 0L) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId AND ss.createdAt BETWEEN :start AND :end")
	Integer sumDurationByUserSchoolIdAndCreatedAtBetween(@Param("schoolId") Long schoolId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT AVG(ss.fluencyScore) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId AND ss.fluencyScore IS NOT NULL")
	Double findAverageFluencyScoreByUserSchoolId(@Param("schoolId") Long schoolId);

	@Query("SELECT AVG(ss.grammarScore) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId AND ss.grammarScore IS NOT NULL")
	Double findAverageGrammarScoreByUserSchoolId(@Param("schoolId") Long schoolId);

	@Query("SELECT AVG(ss.vocabularyScore) FROM SpeakingSession ss WHERE ss.user.school.id = :schoolId AND ss.vocabularyScore IS NOT NULL")
	Double findAverageVocabularyScoreByUserSchoolId(@Param("schoolId") Long schoolId);

	@Query("SELECT u.id, u.firstName, u.lastName, u.email, AVG(ss.overallScore) as avgScore, COUNT(ss) as sessionCount FROM SpeakingSession ss JOIN ss.user u WHERE u.school.id = :schoolId AND u.userType = 'Student' GROUP BY u.id, u.firstName, u.lastName, u.email ORDER BY avgScore DESC")
	List<Object[]> findTopStudentsBySchoolId(@Param("schoolId") Long schoolId, Pageable pageable);

	@Query("SELECT u.id, u.firstName, u.lastName, u.email, AVG(ss.overallScore) as avgScore FROM SpeakingSession ss JOIN ss.user u WHERE u.school.id = :schoolId AND u.userType = 'Student' GROUP BY u.id, u.firstName, u.lastName, u.email HAVING COUNT(ss) = 0 OR AVG(ss.overallScore) < 50 ORDER BY avgScore ASC")
	List<Object[]> findStudentsNeedingAttentionBySchoolId(@Param("schoolId") Long schoolId, Pageable pageable);

}
