package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Lesson;
import com.rslsolution.speakmateai.entity.LessonProgress;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

	Optional<LessonProgress> findByUserAndLesson(User user, Lesson lesson);

	List<LessonProgress> findByUser(User user);

	List<LessonProgress> findByUserAndCompleted(User user, Boolean completed);

	List<LessonProgress> findByUserOrderByLastOpenedAtDesc(User user);

	boolean existsByUserAndLesson(User user, Lesson lesson);

	@Query("SELECT COUNT(l) FROM LessonProgress l WHERE l.user.id = :userId AND l.completed = true")
	long countByUserIdAndCompletedTrue(@Param("userId") Long userId);

	@Query("SELECT COUNT(l) FROM LessonProgress l WHERE l.user.id = :userId AND l.completed = true AND l.completedAt BETWEEN :start AND :end")
	long countByUserIdAndCompletedAtBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT COUNT(l) FROM LessonProgress l WHERE l.user.schoolId = :schoolId AND l.completed = true")
	long countByUserSchoolIdAndCompletedTrue(@Param("schoolId") Long schoolId);

	@Query("SELECT l FROM LessonProgress l WHERE l.user.id = :userId AND l.completed = true AND l.completedAt BETWEEN :start AND :end ORDER BY l.completedAt DESC")
	List<LessonProgress> findByUserIdAndCompletedAtBetween(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
