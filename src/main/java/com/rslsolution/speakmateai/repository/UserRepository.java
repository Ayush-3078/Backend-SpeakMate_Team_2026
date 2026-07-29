package com.rslsolution.speakmateai.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByResetPasswordToken(String resetPasswordToken);

	long countByActiveTrue();

	long countByActiveFalse();

	long countByUserType(String userType);

	long countByCreatedAtAfter(LocalDateTime date);

	long countByUserTypeAndActiveTrue(String userType);

	long countByUserTypeAndActiveFalse(String userType);

	long countByUpdatedAtAfter(LocalDateTime date);

	long countByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

	long countByUserTypeAndCreatedAtAfter(String userType, LocalDateTime date);

	@Query("SELECT DATE(u.createdAt) as date, COUNT(u) as count FROM User u WHERE u.createdAt BETWEEN :start AND :end GROUP BY DATE(u.createdAt)")
	List<Object[]> countByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT DATE(u.createdAt) as date, COUNT(u) as count FROM User u WHERE u.active = true AND u.createdAt BETWEEN :start AND :end GROUP BY DATE(u.createdAt)")
	List<Object[]> countActiveByCreatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT DATE(u.createdAt) as date, COUNT(u) as count FROM User u WHERE u.userType = :userType AND u.createdAt BETWEEN :start AND :end GROUP BY DATE(u.createdAt)")
	List<Object[]> countByUserTypeAndCreatedAtDateBetween(@Param("userType") String userType, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT DATE(u.createdAt) as date, COUNT(u) as count FROM User u WHERE u.userType = :userType AND u.active = true AND u.createdAt BETWEEN :start AND :end GROUP BY DATE(u.createdAt)")
	List<Object[]> countActiveByUserTypeAndCreatedAtDateBetween(@Param("userType") String userType, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT DATE(u.updatedAt) as date, COUNT(u) as count FROM User u WHERE u.updatedAt BETWEEN :start AND :end GROUP BY DATE(u.updatedAt)")
	List<Object[]> countActiveByUpdatedAtDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	List<User> findBySchoolId(Long schoolId);

	long countBySchoolId(Long schoolId);

	long countBySchoolIdAndActiveTrue(Long schoolId);

	List<User> findBySchoolIdAndUserType(Long schoolId, String userType);

	long countBySchoolIdAndUserType(Long schoolId, String userType);

	long countBySchoolIdAndUserTypeAndActiveTrue(Long schoolId, String userType);

	long countBySchoolIdAndUserTypeAndActiveFalse(Long schoolId, String userType);

	List<User> findAllByRole(Role role);

	List<User> findAllByRoleAndSchoolId(Role role, Long schoolId);

	Optional<User> findByIdAndRole(Long id, Role role);

	Optional<User> findByIdAndRoleAndSchoolId(Long id, Role role, Long schoolId);

	boolean existsByStudentId(String studentId);

	boolean existsByStudentIdAndSchoolId(String studentId, Long schoolId);

}
