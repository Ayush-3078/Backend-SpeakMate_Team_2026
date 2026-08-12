package com.rslsolution.speakmateai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Progress;
import com.rslsolution.speakmateai.entity.Student;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

	Optional<Progress> findByStudent(Student student);

	java.util.List<Progress> findByCurrentStreakGreaterThan(int streak);

	java.util.List<Progress> findTop50ByOrderByXpDesc();
	
	java.util.List<Progress> findAllByOrderByXpDesc();

	@Query("SELECT COALESCE(AVG(p.totalPracticeMinutes), 0.0) FROM Progress p")
	Double getAveragePracticeMinutes();
}