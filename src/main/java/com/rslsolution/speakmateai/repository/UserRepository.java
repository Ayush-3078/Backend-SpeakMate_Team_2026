package com.rslsolution.speakmateai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email); // Check duplicate email during registration.

	Optional<User> findByEmail(String email); // Used for login and fetching a user by email.

	Optional<User> findByEmailVerificationToken(String token);

	Optional<User> findByResetPasswordToken(String resetPasswordToken);

	long countByActiveTrue();

	@org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.role = 'STUDENT'")
	java.util.List<User> findAllStudents();

	@org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.role = 'STUDENT' AND u.schoolId = :schoolId")
	java.util.List<User> findAllStudentsBySchoolId(@org.springframework.data.repository.query.Param("schoolId") Long schoolId);

	@org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'STUDENT'")
	Optional<User> findStudentById(@org.springframework.data.repository.query.Param("id") Long id);

	@org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'STUDENT' AND u.schoolId = :schoolId")
	Optional<User> findStudentByIdAndSchoolId(@org.springframework.data.repository.query.Param("id") Long id, @org.springframework.data.repository.query.Param("schoolId") Long schoolId);

	@org.springframework.data.jpa.repository.Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.studentId = :studentId AND u.role = 'STUDENT'")
	boolean existsStudentByStudentId(@org.springframework.data.repository.query.Param("studentId") String studentId);

	@org.springframework.data.jpa.repository.Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.studentId = :studentId AND u.schoolId = :schoolId AND u.role = 'STUDENT'")
	boolean existsStudentByStudentIdAndSchoolId(@org.springframework.data.repository.query.Param("studentId") String studentId, @org.springframework.data.repository.query.Param("schoolId") Long schoolId);

}
