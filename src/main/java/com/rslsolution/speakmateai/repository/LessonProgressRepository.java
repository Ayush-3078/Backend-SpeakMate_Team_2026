package com.rslsolution.speakmateai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Lesson;
import com.rslsolution.speakmateai.entity.LessonProgress;
import com.rslsolution.speakmateai.entity.Student;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

	Optional<LessonProgress> findByStudentAndLesson(Student student, Lesson lesson);

	List<LessonProgress> findByStudent(Student student);

	List<LessonProgress> findByStudentAndCompleted(Student student, Boolean completed);

	List<LessonProgress> findByStudentOrderByLastOpenedAtDesc(Student student);

	boolean existsByStudentAndLesson(Student student, Lesson lesson);
}
