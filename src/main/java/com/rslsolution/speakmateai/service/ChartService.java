package com.rslsolution.speakmateai.service;

import com.rslsolution.speakmateai.dto.response.chart.AiUsageResponse;
import com.rslsolution.speakmateai.dto.response.chart.LearningProgressResponse;
import com.rslsolution.speakmateai.dto.response.chart.SpeakingPerformanceResponse;
import com.rslsolution.speakmateai.dto.response.chart.TopLessonResponse;
import com.rslsolution.speakmateai.dto.response.chart.UserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.chart.WeeklyActiveUsersResponse;

import java.util.List;

public interface ChartService {

	UserGrowthResponse getUserGrowth();

	LearningProgressResponse getLearningProgress();

	AiUsageResponse getAiUsage();

	WeeklyActiveUsersResponse getWeeklyActiveUsers();

	List<TopLessonResponse> getTopLessons();

	SpeakingPerformanceResponse getSpeakingPerformance();

}
