package com.rslsolution.speakmateai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rslsolution.speakmateai.entity.Achievement;
import com.rslsolution.speakmateai.entity.User;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

	List<Achievement> findByUser(User user);

	List<Achievement> findByUserOrderByCreatedAtDesc(User user);

	List<Achievement> findByUserAndUnlockedTrue(User user);

	@Query("SELECT COUNT(a) FROM Achievement a WHERE a.user.id = :userId AND a.unlocked = true")
	long countByUserIdAndUnlockedTrue(@Param("userId") Long userId);

	@Query("SELECT a FROM Achievement a WHERE a.user.id = :userId AND a.unlocked = true ORDER BY a.unlockedAt DESC")
	List<Achievement> findByUserIdAndUnlockedTrueOrderByUnlockedAtDesc(@Param("userId") Long userId);
}