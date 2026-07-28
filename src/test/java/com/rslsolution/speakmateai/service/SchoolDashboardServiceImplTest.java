package com.rslsolution.speakmateai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rslsolution.speakmateai.dto.response.ActivityResponse;
import com.rslsolution.speakmateai.dto.response.LeaderboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolProgressResponse;
import com.rslsolution.speakmateai.dto.response.SchoolDashboardResponse;
import com.rslsolution.speakmateai.dto.response.SkillScoresResponse;
import com.rslsolution.speakmateai.dto.response.StudentAttentionResponse;
import com.rslsolution.speakmateai.dto.response.TopStudentResponse;
import com.rslsolution.speakmateai.entity.School;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.SchoolStatus;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.impl.SchoolDashboardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SchoolDashboardServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private SchoolRepository schoolRepository;

	@Mock
	private SpeakingSessionRepository speakingSessionRepository;

	@Mock
	private LessonProgressRepository lessonProgressRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private SchoolDashboardServiceImpl schoolDashboardService;

	private void mockAuthentication(String email) {
		when(authentication.getName()).thenReturn(email);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void getSchoolDashboard_ReturnsDashboardData() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(userRepository.countBySchoolIdAndUserType(1L, "Student")).thenReturn(100L);
		when(userRepository.countBySchoolIdAndUserTypeAndActiveTrue(1L, "Student")).thenReturn(80L);
		when(userRepository.countBySchoolIdAndUserType(1L, "Teacher")).thenReturn(10L);
		when(speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5L);
		when(lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(1L)).thenReturn(50L);
		when(speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(1L)).thenReturn(75.5);
		when(speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(1L)).thenReturn(68.2);
		when(speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(1L)).thenReturn(70.0);
		when(speakingSessionRepository.findTopStudentsBySchoolId(any(Long.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(List.<Object[]>of(new Object[]{1L, "John", "Doe", "john@test.com", 85.0, 10}));
		when(speakingSessionRepository.findStudentsNeedingAttentionBySchoolId(any(Long.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(List.<Object[]>of(new Object[]{2L, "Jane", "Smith", "jane@test.com", 45.0}));

		SchoolDashboardResponse response = schoolDashboardService.getSchoolDashboard();

		assertNotNull(response);
		assertEquals(100, response.getTotalStudents());
		assertEquals(80, response.getActiveStudents());
		assertEquals(20, response.getInactiveStudents());
		assertEquals(10, response.getTotalTeachers());
		assertEquals(5, response.getPracticeToday());
		assertEquals(50, response.getLessonsCompleted());
		assertEquals(75.5, response.getAverageFluency());
		assertEquals(68.2, response.getAverageGrammarAccuracy());
		assertEquals(70.0, response.getAverageVocabularyScore());
		assertEquals(1, response.getTopStudents().size());
		assertEquals(1, response.getStudentsNeedingAttention().size());
	}

	@Test
	void getSchoolDashboard_ReturnsEmptyWhenUserHasNoSchool() {
		User user = User.builder()
				.id(1L)
				.email("admin@test.com")
				.role(Role.ADMIN)
				.school(null)
				.build();

		mockAuthentication("admin@test.com");
		when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

		SchoolDashboardResponse response = schoolDashboardService.getSchoolDashboard();

		assertNotNull(response);
		assertEquals(0, response.getTotalStudents());
		assertEquals(0, response.getActiveStudents());
		assertEquals(0, response.getTotalTeachers());
		assertEquals(0, response.getPracticeToday());
		assertEquals(0, response.getLessonsCompleted());
		assertEquals(0.0, response.getAverageFluency());
		assertEquals(0.0, response.getAverageGrammarAccuracy());
		assertEquals(0.0, response.getAverageVocabularyScore());
	}

	@Test
	void getSchoolDashboard_ReturnsZeroWhenNoData() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(userRepository.countBySchoolIdAndUserType(1L, "Student")).thenReturn(0L);
		when(userRepository.countBySchoolIdAndUserTypeAndActiveTrue(1L, "Student")).thenReturn(0L);
		when(userRepository.countBySchoolIdAndUserType(1L, "Teacher")).thenReturn(0L);
		when(speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
		when(lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(1L)).thenReturn(0L);
		when(speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(1L)).thenReturn(null);
		when(speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(1L)).thenReturn(null);
		when(speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(1L)).thenReturn(null);
		when(speakingSessionRepository.findTopStudentsBySchoolId(any(Long.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(List.of());
		when(speakingSessionRepository.findStudentsNeedingAttentionBySchoolId(any(Long.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(List.of());

		SchoolDashboardResponse response = schoolDashboardService.getSchoolDashboard();

		assertNotNull(response);
		assertEquals(0, response.getTotalStudents());
		assertEquals(0, response.getActiveStudents());
		assertEquals(0, response.getInactiveStudents());
		assertEquals(0, response.getTotalTeachers());
		assertEquals(0, response.getPracticeToday());
		assertEquals(0, response.getPracticeThisWeek());
		assertEquals(0, response.getLessonsCompleted());
		assertEquals(0.0, response.getAverageFluency());
		assertEquals(0.0, response.getAverageGrammarAccuracy());
		assertEquals(0.0, response.getAverageVocabularyScore());
		assertEquals(0, response.getTopStudents().size());
		assertEquals(0, response.getStudentsNeedingAttention().size());
	}

	@Test
	void getActivity_ReturnsActivityData() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(any(Long.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(3L);

		ActivityResponse response = schoolDashboardService.getActivity();

		assertNotNull(response);
		assertEquals(2, response.getLabels().size());
		assertEquals(1, response.getDailyPractice().size());
		assertEquals(1, response.getWeeklyPractice().size());
		assertEquals(3, response.getDailyPractice().get(0));
		assertEquals(3, response.getWeeklyPractice().get(0));
	}

	@Test
	void getActivity_ReturnsEmptyWhenUserHasNoSchool() {
		User user = User.builder()
				.id(1L)
				.email("admin@test.com")
				.role(Role.ADMIN)
				.school(null)
				.build();

		mockAuthentication("admin@test.com");
		when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

		ActivityResponse response = schoolDashboardService.getActivity();

		assertNotNull(response);
		assertEquals(0, response.getLabels().size());
	}

	@Test
	void getProgress_ReturnsProgressData() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(lessonProgressRepository.countByUserSchoolId(1L)).thenReturn(100L);
		when(lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(1L)).thenReturn(60L);

		SchoolProgressResponse response = schoolDashboardService.getProgress();

		assertNotNull(response);
		assertEquals(100, response.getTotalLessons());
		assertEquals(60, response.getCompletedLessons());
		assertEquals(40, response.getInProgressLessons());
	}

	@Test
	void getProgress_ReturnsEmptyWhenUserHasNoSchool() {
		User user = User.builder()
				.id(1L)
				.email("admin@test.com")
				.role(Role.ADMIN)
				.school(null)
				.build();

		mockAuthentication("admin@test.com");
		when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

		SchoolProgressResponse response = schoolDashboardService.getProgress();

		assertNotNull(response);
		assertEquals(0, response.getTotalLessons());
		assertEquals(0, response.getCompletedLessons());
		assertEquals(0, response.getInProgressLessons());
	}

	@Test
	void getSkillScores_ReturnsScores() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(1L)).thenReturn(80.0);
		when(speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(1L)).thenReturn(75.0);
		when(speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(1L)).thenReturn(70.0);

		SkillScoresResponse response = schoolDashboardService.getSkillScores();

		assertNotNull(response);
		assertEquals(80.0, response.getAverageFluency());
		assertEquals(75.0, response.getAverageGrammarAccuracy());
		assertEquals(70.0, response.getAverageVocabularyScore());
		assertEquals(75.0, response.getOverallAverage());
	}

	@Test
	void getSkillScores_ReturnsEmptyWhenUserHasNoSchool() {
		User user = User.builder()
				.id(1L)
				.email("admin@test.com")
				.role(Role.ADMIN)
				.school(null)
				.build();

		mockAuthentication("admin@test.com");
		when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

		SkillScoresResponse response = schoolDashboardService.getSkillScores();

		assertNotNull(response);
		assertEquals(0.0, response.getAverageFluency());
		assertEquals(0.0, response.getAverageGrammarAccuracy());
		assertEquals(0.0, response.getAverageVocabularyScore());
		assertEquals(0.0, response.getOverallAverage());
	}

	@Test
	void getSkillScores_HandlesNullAverages() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(1L)).thenReturn(null);
		when(speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(1L)).thenReturn(null);
		when(speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(1L)).thenReturn(null);

		SkillScoresResponse response = schoolDashboardService.getSkillScores();

		assertNotNull(response);
		assertEquals(0.0, response.getAverageFluency());
		assertEquals(0.0, response.getAverageGrammarAccuracy());
		assertEquals(0.0, response.getAverageVocabularyScore());
		assertEquals(0.0, response.getOverallAverage());
	}

	@Test
	void getLeaderboard_ReturnsLeaderboard() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.build();

		User user = User.builder()
				.id(1L)
				.email("schooladmin@test.com")
				.role(Role.ADMIN)
				.school(school)
				.build();

		mockAuthentication("schooladmin@test.com");
		when(userRepository.findByEmail("schooladmin@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.findTopStudentsBySchoolId(any(Long.class), any(org.springframework.data.domain.Pageable.class))).thenReturn(List.<Object[]>of(
				new Object[]{1L, "John", "Doe", "john@test.com", 90.0, 15},
				new Object[]{2L, "Jane", "Smith", "jane@test.com", 85.0, 12}
		));

		LeaderboardResponse response = schoolDashboardService.getLeaderboard();

		assertNotNull(response);
		assertEquals(2, response.getEntries().size());
		assertEquals(1, response.getEntries().get(0).getRank());
		assertEquals(2, response.getEntries().get(1).getRank());
		assertEquals(90.0, response.getEntries().get(0).getAverageScore());
		assertEquals(15, response.getEntries().get(0).getTotalSessions());
	}

	@Test
	void getLeaderboard_ReturnsEmptyWhenUserHasNoSchool() {
		User user = User.builder()
				.id(1L)
				.email("admin@test.com")
				.role(Role.ADMIN)
				.school(null)
				.build();

		mockAuthentication("admin@test.com");
		when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

		LeaderboardResponse response = schoolDashboardService.getLeaderboard();

		assertNotNull(response);
		assertEquals(0, response.getEntries().size());
	}

	@Test
	void getCurrentUser_ThrowsWhenNotAuthenticated() {
		when(securityContext.getAuthentication()).thenReturn(null);
		SecurityContextHolder.setContext(securityContext);

		assertThrows(UserNotFoundException.class, () -> schoolDashboardService.getSchoolDashboard());
	}

	@Test
	void getCurrentUser_ThrowsWhenAnonymous() {
		mockAuthentication("anonymousUser");

		assertThrows(UserNotFoundException.class, () -> schoolDashboardService.getSchoolDashboard());
	}
}
