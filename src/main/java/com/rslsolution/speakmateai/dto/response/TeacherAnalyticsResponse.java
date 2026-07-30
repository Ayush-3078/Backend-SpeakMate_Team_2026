package com.rslsolution.speakmateai.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAnalyticsResponse {
	private List<ClassPerformanceResponse> classPerformance;
	private SkillPerformanceSummaryResponse skillPerformance;
	private List<PerformanceTrendResponse> performanceTrends;
	private List<TopPerformerResponse> topPerformers;
	private List<StudentAttentionItemResponse> studentsRequiringAttention;
	private List<AiLearningInsightResponse> aiLearningInsights;
}
