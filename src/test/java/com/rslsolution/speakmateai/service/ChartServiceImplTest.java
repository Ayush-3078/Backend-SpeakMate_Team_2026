package com.rslsolution.speakmateai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rslsolution.speakmateai.dto.response.chart.AiUsageResponse;
import com.rslsolution.speakmateai.dto.response.chart.LearningProgressResponse;
import com.rslsolution.speakmateai.dto.response.chart.SpeakingPerformanceResponse;
import com.rslsolution.speakmateai.dto.response.chart.TopLessonResponse;
import com.rslsolution.speakmateai.dto.response.chart.UserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.chart.WeeklyActiveUsersResponse;
import com.rslsolution.speakmateai.entity.Lesson;
import com.rslsolution.speakmateai.entity.LessonProgress;
import com.rslsolution.speakmateai.entity.SpeakingSession;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.service.impl.ChartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ChartServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private SpeakingSessionRepository speakingSessionRepository;

	@Mock
	private LessonProgressRepository lessonProgressRepository;

	@Mock
	private GrammarHistoryRepository grammarHistoryRepository;

	@Mock
	private VocabularyRepository vocabularyRepository;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private ChartServiceImpl chartService;

	private void mockAuthentication(String email) {
		when(authentication.getName()).thenReturn(email);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	void getUserGrowth_ReturnsDailyCounts() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		User user2 = User.builder()
				.id(2L)
				.email("user2@test.com")
				.role(Role.USER)
				.build();

		user.setCreatedAt(LocalDateTime.now().minusDays(1));
		user2.setCreatedAt(LocalDateTime.now());

		when(userRepository.findAll()).thenReturn(List.of(user, user2));

		UserGrowthResponse response = chartService.getUserGrowth();

		assertNotNull(response);
		assertFalse(response.getLabels().isEmpty());
		assertEquals(2, response.getValues().stream().mapToInt(Integer::intValue).sum());
	}

	@Test
	void getLearningProgress_ReturnsMonthlyData() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		Lesson lesson = Lesson.builder()
				.id(1L)
				.title("Test Lesson")
				.build();

		LessonProgress progress = LessonProgress.builder()
				.id(1L)
				.user(user)
				.lesson(lesson)
				.completed(true)
				.completedAt(LocalDateTime.now())
				.lastOpenedAt(LocalDateTime.now())
				.build();

		mockAuthentication("user@test.com");
		when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
		when(lessonProgressRepository.findByUser(user)).thenReturn(List.of(progress));

		LearningProgressResponse response = chartService.getLearningProgress();

		assertNotNull(response);
		assertFalse(response.getLabels().isEmpty());
	}

	@Test
	void getAiUsage_ReturnsSevenDayBreakdown() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		mockAuthentication("user@test.com");
		when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
		when(chatMessageRepository.countBySessionUserAndCreatedAtBetween(any(), any(), any())).thenReturn(0L);
		when(speakingSessionRepository.countByUserAndCreatedAtBetween(any(), any(), any())).thenReturn(0L);
		when(grammarHistoryRepository.countByUserAndCreatedAtBetween(any(), any(), any())).thenReturn(0L);
		when(vocabularyRepository.countByUserAndCreatedAtBetween(any(), any(), any())).thenReturn(0L);

		AiUsageResponse response = chartService.getAiUsage();

		assertNotNull(response);
		assertEquals(7, response.getLabels().size());
		assertEquals(7, response.getChatMessages().size());
		assertEquals(7, response.getSpeakingSessions().size());
		assertEquals(7, response.getGrammarChecks().size());
		assertEquals(7, response.getVocabularyActions().size());
	}

	@Test
	void getWeeklyActiveUsers_ReturnsWeekData() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();
		user.setUpdatedAt(LocalDateTime.now());
		user.setCreatedAt(LocalDateTime.now());

		when(userRepository.findAll()).thenReturn(List.of(user));

		WeeklyActiveUsersResponse response = chartService.getWeeklyActiveUsers();

		assertNotNull(response);
		assertEquals(7, response.getLabels().size());
		assertEquals(7, response.getActiveUsers().size());
		assertEquals(7, response.getNewUsers().size());
	}

	@Test
	void getTopLessons_ReturnsTopFive() {
		Lesson lesson = Lesson.builder()
				.id(1L)
				.title("Top Lesson")
				.category("Grammar")
				.build();

		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		LessonProgress progress = LessonProgress.builder()
				.id(1L)
				.user(user)
				.lesson(lesson)
				.completed(true)
				.xpEarned(50)
				.build();

		when(lessonProgressRepository.findAll()).thenReturn(List.of(progress));

		List<TopLessonResponse> response = chartService.getTopLessons();

		assertNotNull(response);
		assertFalse(response.isEmpty());
		assertTrue(response.size() <= 5);
		assertEquals("Top Lesson", response.get(0).getTitle());
		assertEquals(1, response.get(0).getCompletions().intValue());
	}

	@Test
	void getSpeakingPerformance_ReturnsAverages() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		SpeakingSession session = SpeakingSession.builder()
				.id(1L)
				.user(user)
				.topic("Test")
				.duration(120)
				.overallScore(80.0)
				.pronunciationScore(75.0)
				.fluencyScore(85.0)
				.grammarScore(70.0)
				.vocabularyScore(78.0)
				.build();

		mockAuthentication("user@test.com");
		when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.findByUser(user)).thenReturn(List.of(session));

		SpeakingPerformanceResponse response = chartService.getSpeakingPerformance();

		assertNotNull(response);
		assertEquals(1, response.getTotalSessions());
		assertEquals(2, response.getTotalMinutes());
		assertEquals(80.0, response.getAverageScore());
		assertEquals(75.0, response.getAveragePronunciation());
		assertEquals(85.0, response.getAverageFluency());
		assertEquals(70.0, response.getAverageGrammar());
		assertEquals(78.0, response.getAverageVocabulary());
	}

	@Test
	void getSpeakingPerformance_ReturnsEmptyWhenNoSessions() {
		User user = User.builder()
				.id(1L)
				.email("user@test.com")
				.role(Role.USER)
				.build();

		mockAuthentication("user@test.com");
		when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
		when(speakingSessionRepository.findByUser(user)).thenReturn(List.of());

		SpeakingPerformanceResponse response = chartService.getSpeakingPerformance();

		assertNotNull(response);
		assertEquals(0, response.getTotalSessions());
		assertEquals(0, response.getTotalMinutes());
		assertEquals(0.0, response.getAverageScore());
	}
}
