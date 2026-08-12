package com.rslsolution.speakmateai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.SpeakingSession;
import com.rslsolution.speakmateai.entity.Student;

@Repository
public interface SpeakingSessionRepository extends JpaRepository<SpeakingSession, Long> {

	List<SpeakingSession> findByStudent(Student student);

	List<SpeakingSession> findByStudentOrderByCreatedAtDesc(Student student);

}