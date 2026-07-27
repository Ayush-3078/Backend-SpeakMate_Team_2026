package com.rslsolution.speakmateai.dto.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUsageResponse {

	private List<String> labels;
	private List<Integer> dailyActiveUsers;
	private List<Integer> speakingSessions;
	private List<Integer> lessonsCompleted;
	private List<Integer> practiceMinutes;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getDailyActiveUsers() { return dailyActiveUsers; }
	public void setDailyActiveUsers(List<Integer> dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; }

	public List<Integer> getSpeakingSessions() { return speakingSessions; }
	public void setSpeakingSessions(List<Integer> speakingSessions) { this.speakingSessions = speakingSessions; }

	public List<Integer> getLessonsCompleted() { return lessonsCompleted; }
	public void setLessonsCompleted(List<Integer> lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; }

	public List<Integer> getPracticeMinutes() { return practiceMinutes; }
	public void setPracticeMinutes(List<Integer> practiceMinutes) { this.practiceMinutes = practiceMinutes; }

	public static AdminUsageResponseBuilder builder() {
		return new AdminUsageResponseBuilder();
	}

	public static class AdminUsageResponseBuilder {
		private List<String> labels;
		private List<Integer> dailyActiveUsers;
		private List<Integer> speakingSessions;
		private List<Integer> lessonsCompleted;
		private List<Integer> practiceMinutes;

		public AdminUsageResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public AdminUsageResponseBuilder dailyActiveUsers(List<Integer> dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; return this; }
		public AdminUsageResponseBuilder speakingSessions(List<Integer> speakingSessions) { this.speakingSessions = speakingSessions; return this; }
		public AdminUsageResponseBuilder lessonsCompleted(List<Integer> lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; return this; }
		public AdminUsageResponseBuilder practiceMinutes(List<Integer> practiceMinutes) { this.practiceMinutes = practiceMinutes; return this; }

		public AdminUsageResponse build() {
			AdminUsageResponse obj = new AdminUsageResponse();
			obj.setLabels(labels);
			obj.setDailyActiveUsers(dailyActiveUsers);
			obj.setSpeakingSessions(speakingSessions);
			obj.setLessonsCompleted(lessonsCompleted);
			obj.setPracticeMinutes(practiceMinutes);
			return obj;
		}
	}
}
