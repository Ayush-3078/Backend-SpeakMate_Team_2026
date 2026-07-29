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
public class SchoolDashboardResponse {

	private Long totalStudents;
	private Long activeStudents;
	private Long inactiveStudents;
	private Long totalTeachers;
	private Integer totalClasses;
	private Long practiceToday;
	private Long practiceThisWeek;
	private Long lessonsCompleted;
	private Double averageFluency;
	private Double averageGrammarAccuracy;
	private Double averageVocabularyScore;
	private List<TopStudentResponse> topStudents;
	private List<StudentAttentionResponse> studentsNeedingAttention;

	public static SchoolDashboardResponseBuilder builder() {
		return new SchoolDashboardResponseBuilder();
	}

	public static class SchoolDashboardResponseBuilder {
		private Long totalStudents;
		private Long activeStudents;
		private Long inactiveStudents;
		private Long totalTeachers;
		private Integer totalClasses;
		private Long practiceToday;
		private Long practiceThisWeek;
		private Long lessonsCompleted;
		private Double averageFluency;
		private Double averageGrammarAccuracy;
		private Double averageVocabularyScore;
		private List<TopStudentResponse> topStudents;
		private List<StudentAttentionResponse> studentsNeedingAttention;

		public SchoolDashboardResponseBuilder totalStudents(Long totalStudents) { this.totalStudents = totalStudents; return this; }
		public SchoolDashboardResponseBuilder activeStudents(Long activeStudents) { this.activeStudents = activeStudents; return this; }
		public SchoolDashboardResponseBuilder inactiveStudents(Long inactiveStudents) { this.inactiveStudents = inactiveStudents; return this; }
		public SchoolDashboardResponseBuilder totalTeachers(Long totalTeachers) { this.totalTeachers = totalTeachers; return this; }
		public SchoolDashboardResponseBuilder totalClasses(Integer totalClasses) { this.totalClasses = totalClasses; return this; }
		public SchoolDashboardResponseBuilder practiceToday(Long practiceToday) { this.practiceToday = practiceToday; return this; }
		public SchoolDashboardResponseBuilder practiceThisWeek(Long practiceThisWeek) { this.practiceThisWeek = practiceThisWeek; return this; }
		public SchoolDashboardResponseBuilder lessonsCompleted(Long lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; return this; }
		public SchoolDashboardResponseBuilder averageFluency(Double averageFluency) { this.averageFluency = averageFluency; return this; }
		public SchoolDashboardResponseBuilder averageGrammarAccuracy(Double averageGrammarAccuracy) { this.averageGrammarAccuracy = averageGrammarAccuracy; return this; }
		public SchoolDashboardResponseBuilder averageVocabularyScore(Double averageVocabularyScore) { this.averageVocabularyScore = averageVocabularyScore; return this; }
		public SchoolDashboardResponseBuilder topStudents(List<TopStudentResponse> topStudents) { this.topStudents = topStudents; return this; }
		public SchoolDashboardResponseBuilder studentsNeedingAttention(List<StudentAttentionResponse> studentsNeedingAttention) { this.studentsNeedingAttention = studentsNeedingAttention; return this; }

		public SchoolDashboardResponse build() {
			SchoolDashboardResponse obj = new SchoolDashboardResponse();
			obj.setTotalStudents(totalStudents);
			obj.setActiveStudents(activeStudents);
			obj.setInactiveStudents(inactiveStudents);
			obj.setTotalTeachers(totalTeachers);
			obj.setTotalClasses(totalClasses);
			obj.setPracticeToday(practiceToday);
			obj.setPracticeThisWeek(practiceThisWeek);
			obj.setLessonsCompleted(lessonsCompleted);
			obj.setAverageFluency(averageFluency);
			obj.setAverageGrammarAccuracy(averageGrammarAccuracy);
			obj.setAverageVocabularyScore(averageVocabularyScore);
			obj.setTopStudents(topStudents);
			obj.setStudentsNeedingAttention(studentsNeedingAttention);
			return obj;
		}
	}
}
