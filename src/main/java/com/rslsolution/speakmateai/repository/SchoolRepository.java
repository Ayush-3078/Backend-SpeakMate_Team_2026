package com.rslsolution.speakmateai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {

	boolean existsBySchoolCode(String schoolCode);

	boolean existsBySchoolCodeAndIdNot(String schoolCode, Long id);

	Optional<School> findBySchoolCode(String schoolCode);

}
