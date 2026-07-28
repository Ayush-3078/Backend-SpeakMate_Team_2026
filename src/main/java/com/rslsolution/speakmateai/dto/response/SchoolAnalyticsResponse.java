package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolAnalyticsResponse {

	private Long totalStudents;
	private Long activeStudents;
	private Long lessonsCompleted;
	private Long aiConversations;
	private Long speakingSessions;
	private Long practiceMinutes;

	public Long getTotalStudents() { return totalStudents; }
	public void setTotalStudents(Long totalStudents) { this.totalStudents = totalStudents; }

	public Long getActiveStudents() { return activeStudents; }
	public void setActiveStudents(Long activeStudents) { this.activeStudents = activeStudents; }

	public Long getLessonsCompleted() { return lessonsCompleted; }
	public void setLessonsCompleted(Long lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; }

	public Long getAiConversations() { return aiConversations; }
	public void setAiConversations(Long aiConversations) { this.aiConversations = aiConversations; }

	public Long getSpeakingSessions() { return speakingSessions; }
	public void setSpeakingSessions(Long speakingSessions) { this.speakingSessions = speakingSessions; }

	public Long getPracticeMinutes() { return practiceMinutes; }
	public void setPracticeMinutes(Long practiceMinutes) { this.practiceMinutes = practiceMinutes; }

	public static SchoolAnalyticsResponseBuilder builder() {
		return new SchoolAnalyticsResponseBuilder();
	}

	public static class SchoolAnalyticsResponseBuilder {
		private Long totalStudents;
		private Long activeStudents;
		private Long lessonsCompleted;
		private Long aiConversations;
		private Long speakingSessions;
		private Long practiceMinutes;

		public SchoolAnalyticsResponseBuilder totalStudents(Long totalStudents) { this.totalStudents = totalStudents; return this; }
		public SchoolAnalyticsResponseBuilder activeStudents(Long activeStudents) { this.activeStudents = activeStudents; return this; }
		public SchoolAnalyticsResponseBuilder lessonsCompleted(Long lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; return this; }
		public SchoolAnalyticsResponseBuilder aiConversations(Long aiConversations) { this.aiConversations = aiConversations; return this; }
		public SchoolAnalyticsResponseBuilder speakingSessions(Long speakingSessions) { this.speakingSessions = speakingSessions; return this; }
		public SchoolAnalyticsResponseBuilder practiceMinutes(Long practiceMinutes) { this.practiceMinutes = practiceMinutes; return this; }

		public SchoolAnalyticsResponse build() {
			SchoolAnalyticsResponse obj = new SchoolAnalyticsResponse();
			obj.setTotalStudents(totalStudents);
			obj.setActiveStudents(activeStudents);
			obj.setLessonsCompleted(lessonsCompleted);
			obj.setAiConversations(aiConversations);
			obj.setSpeakingSessions(speakingSessions);
			obj.setPracticeMinutes(practiceMinutes);
			return obj;
		}
	}
}
