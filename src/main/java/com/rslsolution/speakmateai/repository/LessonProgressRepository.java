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

	long countByCompletedTrue();

	@Query("SELECT DATE(lp.completedAt) as date, COUNT(lp) as count FROM LessonProgress lp WHERE lp.completed = true AND lp.completedAt BETWEEN :start AND :end GROUP BY DATE(lp.completedAt)")
	List<Object[]> countCompletedByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.completed = true AND lp.user.school.id = :schoolId")
	long countByUserSchoolIdAndCompletedTrue(@Param("schoolId") Long schoolId);

	@Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.user.school.id = :schoolId")
	long countByUserSchoolId(@Param("schoolId") Long schoolId);

}