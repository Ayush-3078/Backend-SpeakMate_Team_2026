package com.rslsolution.speakmateai.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.rslsolution.speakmateai.dto.response.ActivityResponse;
import com.rslsolution.speakmateai.dto.response.LeaderboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolDashboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolProgressResponse;
import com.rslsolution.speakmateai.dto.response.SkillScoresResponse;
import com.rslsolution.speakmateai.dto.response.StudentAttentionResponse;
import com.rslsolution.speakmateai.dto.response.TopStudentResponse;
import com.rslsolution.speakmateai.service.SchoolDashboardService;

@SpringBootTest
@AutoConfigureMockMvc
class SchoolDashboardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SchoolDashboardService schoolDashboardService;

	@Test
	@WithMockUser(roles = "SCHOOL_ADMIN")
	@DisplayName("GET /api/v1/school/dashboard - returns dashboard data")
	void getSchoolDashboard_ReturnsDashboard() throws Exception {
		SchoolDashboardResponse response = SchoolDashboardResponse.builder()
				.totalStudents(100L)
				.activeStudents(80L)
				.inactiveStudents(20L)
				.totalTeachers(10L)
				.totalClasses(5)
				.practiceToday(15L)
				.practiceThisWeek(45L)
				.lessonsCompleted(50L)
				.averageFluency(75.5)
				.averageGrammarAccuracy(68.2)
				.averageVocabularyScore(70.0)
				.topStudents(List.of(
						TopStudentResponse.builder().userId(1L).firstName("John").lastName("Doe").email("john@test.com").averageScore(85.0).totalSessions(10).build()))
				.studentsNeedingAttention(List.of(
						StudentAttentionResponse.builder().userId(2L).firstName("Jane").lastName("Smith").email("jane@test.com").reason("Low performance").build()))
				.build();

		when(schoolDashboardService.getSchoolDashboard()).thenReturn(response);

		mockMvc.perform(get("/api/v1/school/dashboard")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.totalStudents").value(100))
				.andExpect(jsonPath("$.activeStudents").value(80))
				.andExpect(jsonPath("$.inactiveStudents").value(20))
				.andExpect(jsonPath("$.totalTeachers").value(10))
				.andExpect(jsonPath("$.totalClasses").value(5))
				.andExpect(jsonPath("$.practiceToday").value(15))
				.andExpect(jsonPath("$.practiceThisWeek").value(45))
				.andExpect(jsonPath("$.lessonsCompleted").value(50))
				.andExpect(jsonPath("$.averageFluency").value(75.5))
				.andExpect(jsonPath("$.averageGrammarAccuracy").value(68.2))
				.andExpect(jsonPath("$.averageVocabularyScore").value(70.0))
				.andExpect(jsonPath("$.topStudents[0].userId").value(1))
				.andExpect(jsonPath("$.topStudents[0].firstName").value("John"))
				.andExpect(jsonPath("$.studentsNeedingAttention[0].userId").value(2))
				.andExpect(jsonPath("$.studentsNeedingAttention[0].reason").value("Low performance"));
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("GET /api/v1/school/dashboard - denies non-admin users")
	void getSchoolDashboard_DeniesNonAdmin() throws Exception {
		mockMvc.perform(get("/api/v1/school/dashboard")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "SCHOOL_ADMIN")
	@DisplayName("GET /api/v1/school/dashboard/activity - returns activity data")
	void getActivity_ReturnsActivity() throws Exception {
		ActivityResponse response = ActivityResponse.builder()
				.labels(List.of("Today", "This Week"))
				.dailyPractice(List.of(15))
				.weeklyPractice(List.of(45))
				.build();

		when(schoolDashboardService.getActivity()).thenReturn(response);

		mockMvc.perform(get("/api/v1/school/dashboard/activity")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.labels[0]").value("Today"))
				.andExpect(jsonPath("$.labels[1]").value("This Week"))
				.andExpect(jsonPath("$.dailyPractice[0]").value(15))
				.andExpect(jsonPath("$.weeklyPractice[0]").value(45));
	}

	@Test
	@WithMockUser(roles = "SCHOOL_ADMIN")
	@DisplayName("GET /api/v1/school/dashboard/progress - returns progress data")
	void getProgress_ReturnsProgress() throws Exception {
		SchoolProgressResponse response = SchoolProgressResponse.builder()
				.totalLessons(100L)
				.completedLessons(60L)
				.inProgressLessons(40L)
				.recentProgress(List.of())
				.build();

		when(schoolDashboardService.getProgress()).thenReturn(response);

		mockMvc.perform(get("/api/v1/school/dashboard/progress")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.totalLessons").value(100))
				.andExpect(jsonPath("$.completedLessons").value(60))
				.andExpect(jsonPath("$.inProgressLessons").value(40));
	}

	@Test
	@WithMockUser(roles = "SCHOOL_ADMIN")
	@DisplayName("GET /api/v1/school/dashboard/skill-scores - returns skill scores")
	void getSkillScores_ReturnsScores() throws Exception {
		SkillScoresResponse response = SkillScoresResponse.builder()
				.averageFluency(80.0)
				.averageGrammarAccuracy(75.0)
				.averageVocabularyScore(70.0)
				.overallAverage(75.0)
				.build();

		when(schoolDashboardService.getSkillScores()).thenReturn(response);

		mockMvc.perform(get("/api/v1/school/dashboard/skill-scores")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.averageFluency").value(80.0))
				.andExpect(jsonPath("$.averageGrammarAccuracy").value(75.0))
				.andExpect(jsonPath("$.averageVocabularyScore").value(70.0))
				.andExpect(jsonPath("$.overallAverage").value(75.0));
	}

	@Test
	@WithMockUser(roles = "SCHOOL_ADMIN")
	@DisplayName("GET /api/v1/school/dashboard/leaderboard - returns leaderboard")
	void getLeaderboard_ReturnsLeaderboard() throws Exception {
		LeaderboardResponse response = LeaderboardResponse.builder()
				.entries(List.of(
						LeaderboardResponse.LeaderboardEntry.builder()
								.rank(1)
								.userId(1L)
								.firstName("John")
								.lastName("Doe")
								.email("john@test.com")
								.averageScore(90.0)
								.totalSessions(15)
								.build(),
						LeaderboardResponse.LeaderboardEntry.builder()
								.rank(2)
								.userId(2L)
								.firstName("Jane")
								.lastName("Smith")
								.email("jane@test.com")
								.averageScore(85.0)
								.totalSessions(12)
								.build()))
				.build();

		when(schoolDashboardService.getLeaderboard()).thenReturn(response);

		mockMvc.perform(get("/api/v1/school/dashboard/leaderboard")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.entries[0].rank").value(1))
				.andExpect(jsonPath("$.entries[0].userId").value(1))
				.andExpect(jsonPath("$.entries[0].firstName").value("John"))
				.andExpect(jsonPath("$.entries[0].averageScore").value(90.0))
				.andExpect(jsonPath("$.entries[0].totalSessions").value(15))
				.andExpect(jsonPath("$.entries[1].rank").value(2))
				.andExpect(jsonPath("$.entries[1].userId").value(2))
				.andExpect(jsonPath("$.entries[1].firstName").value("Jane"));
	}

	@Test
	@DisplayName("GET /api/v1/school/dashboard - denies unauthenticated access")
	void getSchoolDashboard_DeniesUnauthenticated() throws Exception {
		mockMvc.perform(get("/api/v1/school/dashboard")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
}
