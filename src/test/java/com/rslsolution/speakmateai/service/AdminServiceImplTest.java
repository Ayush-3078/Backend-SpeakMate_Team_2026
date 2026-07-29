package com.rslsolution.speakmateai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rslsolution.speakmateai.dto.response.AdminAiUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminSchoolGrowthResponse;
import com.rslsolution.speakmateai.dto.response.AdminUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminUserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.SuperAdminDashboardResponse;
import com.rslsolution.speakmateai.repository.AchievementRepository;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.repository.ChatSessionRepository;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.LessonRepository;
import com.rslsolution.speakmateai.repository.NotificationRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.service.impl.AdminServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private LessonRepository lessonRepository;

	@Mock
	private SpeakingSessionRepository speakingSessionRepository;

	@Mock
	private VocabularyRepository vocabularyRepository;

	@Mock
	private AchievementRepository achievementRepository;

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private LessonProgressRepository lessonProgressRepository;

	@Mock
	private ChatSessionRepository chatSessionRepository;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private GrammarHistoryRepository grammarHistoryRepository;

	@InjectMocks
	private AdminServiceImpl adminService;

	@Test
	void getSuperAdminDashboard_ReturnsAllStats() {
		when(userRepository.countByUserType("School")).thenReturn(5L);
		when(userRepository.countByUserTypeAndActiveTrue("School")).thenReturn(4L);
		when(userRepository.countByUserTypeAndActiveFalse("School")).thenReturn(1L);
		when(userRepository.countByUserType("Student")).thenReturn(100L);
		when(userRepository.countByUserTypeAndActiveTrue("Student")).thenReturn(80L);
		when(userRepository.countByUserType("Individual")).thenReturn(10L);
		when(userRepository.countByUpdatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(50L);
		when(chatSessionRepository.count()).thenReturn(200L);
		when(speakingSessionRepository.count()).thenReturn(500L);
		when(speakingSessionRepository.sumDuration()).thenReturn(360000);
		when(lessonProgressRepository.countByCompletedTrue()).thenReturn(150L);
		when(speakingSessionRepository.findAverageFluencyScore()).thenReturn(75.5);
		when(speakingSessionRepository.findAverageGrammarScore()).thenReturn(68.2);
		when(chatMessageRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(1000L);
		when(grammarHistoryRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(300L);
		when(userRepository.countByUserTypeAndCreatedAtAfter(any(String.class), any(LocalDateTime.class))).thenReturn(3L);

		SuperAdminDashboardResponse response = adminService.getSuperAdminDashboard();

		assertNotNull(response);
		assertEquals(5, response.getTotalSchools());
		assertEquals(4, response.getActiveSchools());
		assertEquals(1, response.getInactiveSchools());
		assertEquals(100, response.getTotalStudents());
		assertEquals(80, response.getActiveStudents());
		assertEquals(10, response.getIndividualUsers());
		assertEquals(50, response.getDailyActiveUsers());
		assertEquals(50, response.getMonthlyActiveUsers());
		assertEquals(200, response.getAiConversations());
		assertEquals(500, response.getSpeakingSessions());
		assertEquals(6000, response.getPracticeMinutes());
		assertEquals(150, response.getLessonsCompleted());
		assertEquals(75.5, response.getAverageFluencyScore());
		assertEquals(68.2, response.getAverageGrammarScore());
		assertEquals(200 + 300 + 500, response.getGroqApiUsage());
		assertEquals(500, response.getWhisperUsage());
		assertEquals(0, response.getEstimatedAiCost());
		assertEquals(3, response.getNewSchoolsThisMonth());
		assertEquals(3, response.getNewStudentsThisMonth());
	}

	@Test
	void getSuperAdminDashboard_ReturnsZeroWhenNoData() {
		when(userRepository.countByUserType(any(String.class))).thenReturn(0L);
		when(userRepository.countByUserTypeAndActiveTrue(any(String.class))).thenReturn(0L);
		when(userRepository.countByUserTypeAndActiveFalse(any(String.class))).thenReturn(0L);
		when(userRepository.countByUpdatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
		when(chatSessionRepository.count()).thenReturn(0L);
		when(speakingSessionRepository.count()).thenReturn(0L);
		when(speakingSessionRepository.sumDuration()).thenReturn(null);
		when(lessonProgressRepository.countByCompletedTrue()).thenReturn(0L);
		when(speakingSessionRepository.findAverageFluencyScore()).thenReturn(null);
		when(speakingSessionRepository.findAverageGrammarScore()).thenReturn(null);
		when(chatMessageRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
		when(grammarHistoryRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(0L);
		when(userRepository.countByUserTypeAndCreatedAtAfter(any(String.class), any(LocalDateTime.class))).thenReturn(0L);

		SuperAdminDashboardResponse response = adminService.getSuperAdminDashboard();

		assertNotNull(response);
		assertEquals(0, response.getTotalSchools());
		assertEquals(0, response.getActiveSchools());
		assertEquals(0, response.getInactiveSchools());
		assertEquals(0, response.getTotalStudents());
		assertEquals(0, response.getActiveStudents());
		assertEquals(0, response.getIndividualUsers());
		assertEquals(0, response.getDailyActiveUsers());
		assertEquals(0, response.getMonthlyActiveUsers());
		assertEquals(0, response.getAiConversations());
		assertEquals(0, response.getSpeakingSessions());
		assertEquals(0, response.getPracticeMinutes());
		assertEquals(0, response.getLessonsCompleted());
		assertEquals(0, response.getAverageFluencyScore());
		assertEquals(0, response.getAverageGrammarScore());
		assertEquals(0, response.getGroqApiUsage());
		assertEquals(0, response.getWhisperUsage());
		assertEquals(0, response.getEstimatedAiCost());
		assertEquals(0, response.getNewSchoolsThisMonth());
		assertEquals(0, response.getNewStudentsThisMonth());
	}

	@Test
	void getUserGrowth_ReturnsTimeSeries() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		Object[] row1 = new Object[]{Date.valueOf(today), 5L};
		Object[] row2 = new Object[]{Date.valueOf(today), 3L};
		List<Object[]> newUsersList = List.<Object[]>of(row1);
		List<Object[]> activeUsersList = List.<Object[]>of(row2);
		when(userRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(newUsersList);
		when(userRepository.countActiveByUpdatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(activeUsersList);

		AdminUserGrowthResponse response = adminService.getUserGrowth();

		assertNotNull(response);
		assertEquals(31, response.getLabels().size());
		assertEquals(31, response.getTotalUsers().size());
		assertEquals(31, response.getActiveUsers().size());
		assertEquals(31, response.getNewUsers().size());
		assertEquals(5, response.getNewUsers().get(30));
		assertEquals(3, response.getActiveUsers().get(30));
		assertEquals(5, response.getTotalUsers().get(30));
	}

	@Test
	void getSchoolGrowth_ReturnsTimeSeries() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		Object[] row1 = new Object[]{Date.valueOf(today), 2L};
		Object[] row2 = new Object[]{Date.valueOf(today), 1L};
		List<Object[]> totalSchoolsList = List.<Object[]>of(row1);
		List<Object[]> activeSchoolsList = List.<Object[]>of(row2);
		when(userRepository.countByUserTypeAndCreatedAtDateBetween(any(String.class), any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(totalSchoolsList);
		when(userRepository.countActiveByUserTypeAndCreatedAtDateBetween(any(String.class), any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(activeSchoolsList);

		AdminSchoolGrowthResponse response = adminService.getSchoolGrowth();

		assertNotNull(response);
		assertEquals(31, response.getLabels().size());
		assertEquals(31, response.getTotalSchools().size());
		assertEquals(31, response.getActiveSchools().size());
		assertEquals(2, response.getTotalSchools().get(30));
		assertEquals(1, response.getActiveSchools().get(30));
	}

	@Test
	void getUsage_ReturnsTimeSeries() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		Object[] row1 = new Object[]{Date.valueOf(today), 10L};
		Object[] row2 = new Object[]{Date.valueOf(today), 5L};
		Object[] row3 = new Object[]{Date.valueOf(today), 3L};
		Object[] row4 = new Object[]{Date.valueOf(today), 120L};
		List<Object[]> activeUsersList = List.<Object[]>of(row1);
		List<Object[]> sessionsList = List.<Object[]>of(row2);
		List<Object[]> lessonsList = List.<Object[]>of(row3);
		List<Object[]> durationList = List.<Object[]>of(row4);
		when(userRepository.countActiveByUpdatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(activeUsersList);
		when(speakingSessionRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(sessionsList);
		when(lessonProgressRepository.countCompletedByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(lessonsList);
		when(speakingSessionRepository.sumDurationByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(durationList);

		AdminUsageResponse response = adminService.getUsage();

		assertNotNull(response);
		assertEquals(31, response.getLabels().size());
		assertEquals(31, response.getDailyActiveUsers().size());
		assertEquals(31, response.getSpeakingSessions().size());
		assertEquals(31, response.getLessonsCompleted().size());
		assertEquals(31, response.getPracticeMinutes().size());
		assertEquals(10, response.getDailyActiveUsers().get(30));
		assertEquals(5, response.getSpeakingSessions().get(30));
		assertEquals(3, response.getLessonsCompleted().get(30));
		assertEquals(120, response.getPracticeMinutes().get(30));
	}

	@Test
	void getAiUsage_ReturnsTimeSeries() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		Object[] row1 = new Object[]{Date.valueOf(today), 50L};
		Object[] row2 = new Object[]{Date.valueOf(today), 20L};
		Object[] row3 = new Object[]{Date.valueOf(today), 15L};
		Object[] row4 = new Object[]{Date.valueOf(today), 8L};
		List<Object[]> chatMessagesList = List.<Object[]>of(row1);
		List<Object[]> speakingSessionsList = List.<Object[]>of(row2);
		List<Object[]> grammarChecksList = List.<Object[]>of(row3);
		List<Object[]> vocabularyActionsList = List.<Object[]>of(row4);
		when(chatMessageRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(chatMessagesList);
		when(speakingSessionRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(speakingSessionsList);
		when(grammarHistoryRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(grammarChecksList);
		when(vocabularyRepository.countByCreatedAtDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(vocabularyActionsList);

		AdminAiUsageResponse response = adminService.getAiUsage();

		assertNotNull(response);
		assertEquals(31, response.getLabels().size());
		assertEquals(31, response.getChatMessages().size());
		assertEquals(31, response.getSpeakingSessions().size());
		assertEquals(31, response.getGrammarChecks().size());
		assertEquals(31, response.getVocabularyActions().size());
		assertEquals(50, response.getChatMessages().get(30));
		assertEquals(20, response.getSpeakingSessions().get(30));
		assertEquals(15, response.getGrammarChecks().get(30));
		assertEquals(8, response.getVocabularyActions().get(30));
	}
}
