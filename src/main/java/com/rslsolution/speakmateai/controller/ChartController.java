package com.rslsolution.speakmateai.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rslsolution.speakmateai.dto.response.chart.AiUsageResponse;
import com.rslsolution.speakmateai.dto.response.chart.LearningProgressResponse;
import com.rslsolution.speakmateai.dto.response.chart.SpeakingPerformanceResponse;
import com.rslsolution.speakmateai.dto.response.chart.TopLessonResponse;
import com.rslsolution.speakmateai.dto.response.chart.UserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.chart.WeeklyActiveUsersResponse;
import com.rslsolution.speakmateai.service.ChartService;

@RestController
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class ChartController {

	private final ChartService chartService;

	public ChartController(ChartService chartService) {
		this.chartService = chartService;
	}

	@GetMapping("/api/charts/user-growth")
	public UserGrowthResponse getUserGrowth() {
		return chartService.getUserGrowth();
	}

	@GetMapping("/api/charts/learning-progress")
	public LearningProgressResponse getLearningProgress() {
		return chartService.getLearningProgress();
	}

	@GetMapping("/api/charts/ai-usage")
	public AiUsageResponse getAiUsage() {
		return chartService.getAiUsage();
	}

	@GetMapping("/api/charts/weekly-active-users")
	public WeeklyActiveUsersResponse getWeeklyActiveUsers() {
		return chartService.getWeeklyActiveUsers();
	}

	@GetMapping("/api/charts/top-lessons")
	public List<TopLessonResponse> getTopLessons() {
		return chartService.getTopLessons();
	}

	@GetMapping("/api/charts/speaking-performance")
	public SpeakingPerformanceResponse getSpeakingPerformance() {
		return chartService.getSpeakingPerformance();
	}

}
