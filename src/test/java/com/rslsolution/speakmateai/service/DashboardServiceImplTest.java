package com.rslsolution.speakmateai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rslsolution.speakmateai.dto.response.DashboardOverviewResponse;
import com.rslsolution.speakmateai.repository.ChatSessionRepository;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.LessonRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.service.impl.DashboardServiceImpl;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private SpeakingSessionRepository speakingSessionRepository;

	@Mock
	private LessonProgressRepository lessonProgressRepository;

	@Mock
	private LessonRepository lessonRepository;

	@Mock
	private GrammarHistoryRepository grammarHistoryRepository;

	@Mock
	private VocabularyRepository vocabularyRepository;

	@Mock
	private ChatSessionRepository chatSessionRepository;

	@InjectMocks
	private DashboardServiceImpl dashboardService;

	@Test
	void getDashboardOverview_ReturnsCounts() {
		when(userRepository.count()).thenReturn(10L);
		when(userRepository.countByUserType("School")).thenReturn(3L);
		when(userRepository.countByActiveTrue()).thenReturn(8L);
		when(userRepository.countByActiveFalse()).thenReturn(2L);
		when(userRepository.countByCreatedAtAfter(any(LocalDateTime.class))).thenReturn(4L);

		DashboardOverviewResponse response = dashboardService.getDashboardOverview();

		assertNotNull(response);
		assertEquals(10, response.getTotalUsers());
		assertEquals(3, response.getSchoolUsers());
		assertEquals(8, response.getActiveUsers());
		assertEquals(2, response.getInactiveUsers());
		assertEquals(4, response.getNewUsers());
	}

	@Test
	void getDashboardOverview_ReturnsZeroWhenNoData() {
		when(userRepository.count()).thenReturn(0L);
		when(userRepository.countByUserType("School")).thenReturn(0L);
		when(userRepository.countByActiveTrue()).thenReturn(0L);
		when(userRepository.countByActiveFalse()).thenReturn(0L);
		when(userRepository.countByCreatedAtAfter(any(LocalDateTime.class))).thenReturn(0L);

		DashboardOverviewResponse response = dashboardService.getDashboardOverview();

		assertNotNull(response);
		assertEquals(0, response.getTotalUsers());
		assertEquals(0, response.getSchoolUsers());
		assertEquals(0, response.getActiveUsers());
		assertEquals(0, response.getInactiveUsers());
		assertEquals(0, response.getNewUsers());
	}
}
