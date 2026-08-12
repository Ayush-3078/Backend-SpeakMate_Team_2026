package com.rslsolution.speakmateai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Notification;
import com.rslsolution.speakmateai.entity.Student;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByStudent(Student student);

	List<Notification> findByStudentOrderByCreatedAtDesc(Student student);

	List<Notification> findByStudentAndIsReadFalse(Student student);

	long countByStudentAndIsReadFalse(Student student);

}