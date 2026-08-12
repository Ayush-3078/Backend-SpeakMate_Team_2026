package com.rslsolution.speakmateai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Student;
import com.rslsolution.speakmateai.entity.Vocabulary;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

	List<Vocabulary> findByStudent(Student student);

	List<Vocabulary> findByStudentOrderByCreatedAtDesc(Student student);

	List<Vocabulary> findByStudentAndFavoriteTrue(Student student);

}