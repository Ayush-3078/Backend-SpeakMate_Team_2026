package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuperAdminDashboardResponse {

	private Long totalSchools;

	private Long activeSchools;

	private Long inactiveSchools;

	private Long totalStudents;

	private Long activeStudents;

	private Long individualUsers;

	private Long dailyActiveUsers;

	private Long monthlyActiveUsers;

	private Long aiConversations;

	private Long speakingSessions;

	private Long practiceMinutes;

	private Long lessonsCompleted;

	private Double averageFluencyScore;

	private Double averageGrammarScore;

	private Long groqApiUsage;

	private Long whisperUsage;

	private Long estimatedAiCost;

	private Long newSchoolsThisMonth;

	private Long newStudentsThisMonth;

	public Long getTotalSchools() { return totalSchools; }
	public void setTotalSchools(Long totalSchools) { this.totalSchools = totalSchools; }

	public Long getActiveSchools() { return activeSchools; }
	public void setActiveSchools(Long activeSchools) { this.activeSchools = activeSchools; }

	public Long getInactiveSchools() { return inactiveSchools; }
	public void setInactiveSchools(Long inactiveSchools) { this.inactiveSchools = inactiveSchools; }

	public Long getTotalStudents() { return totalStudents; }
	public void setTotalStudents(Long totalStudents) { this.totalStudents = totalStudents; }

	public Long getActiveStudents() { return activeStudents; }
	public void setActiveStudents(Long activeStudents) { this.activeStudents = activeStudents; }

	public Long getIndividualUsers() { return individualUsers; }
	public void setIndividualUsers(Long individualUsers) { this.individualUsers = individualUsers; }

	public Long getDailyActiveUsers() { return dailyActiveUsers; }
	public void setDailyActiveUsers(Long dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; }

	public Long getMonthlyActiveUsers() { return monthlyActiveUsers; }
	public void setMonthlyActiveUsers(Long monthlyActiveUsers) { this.monthlyActiveUsers = monthlyActiveUsers; }

	public Long getAiConversations() { return aiConversations; }
	public void setAiConversations(Long aiConversations) { this.aiConversations = aiConversations; }

	public Long getSpeakingSessions() { return speakingSessions; }
	public void setSpeakingSessions(Long speakingSessions) { this.speakingSessions = speakingSessions; }

	public Long getPracticeMinutes() { return practiceMinutes; }
	public void setPracticeMinutes(Long practiceMinutes) { this.practiceMinutes = practiceMinutes; }

	public Long getLessonsCompleted() { return lessonsCompleted; }
	public void setLessonsCompleted(Long lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; }

	public Double getAverageFluencyScore() { return averageFluencyScore; }
	public void setAverageFluencyScore(Double averageFluencyScore) { this.averageFluencyScore = averageFluencyScore; }

	public Double getAverageGrammarScore() { return averageGrammarScore; }
	public void setAverageGrammarScore(Double averageGrammarScore) { this.averageGrammarScore = averageGrammarScore; }

	public Long getGroqApiUsage() { return groqApiUsage; }
	public void setGroqApiUsage(Long groqApiUsage) { this.groqApiUsage = groqApiUsage; }

	public Long getWhisperUsage() { return whisperUsage; }
	public void setWhisperUsage(Long whisperUsage) { this.whisperUsage = whisperUsage; }

	public Long getEstimatedAiCost() { return estimatedAiCost; }
	public void setEstimatedAiCost(Long estimatedAiCost) { this.estimatedAiCost = estimatedAiCost; }

	public Long getNewSchoolsThisMonth() { return newSchoolsThisMonth; }
	public void setNewSchoolsThisMonth(Long newSchoolsThisMonth) { this.newSchoolsThisMonth = newSchoolsThisMonth; }

	public Long getNewStudentsThisMonth() { return newStudentsThisMonth; }
	public void setNewStudentsThisMonth(Long newStudentsThisMonth) { this.newStudentsThisMonth = newStudentsThisMonth; }

	public static SuperAdminDashboardResponseBuilder builder() {
		return new SuperAdminDashboardResponseBuilder();
	}

	public static class SuperAdminDashboardResponseBuilder {
		private Long totalSchools;
		private Long activeSchools;
		private Long inactiveSchools;
		private Long totalStudents;
		private Long activeStudents;
		private Long individualUsers;
		private Long dailyActiveUsers;
		private Long monthlyActiveUsers;
		private Long aiConversations;
		private Long speakingSessions;
		private Long practiceMinutes;
		private Long lessonsCompleted;
		private Double averageFluencyScore;
		private Double averageGrammarScore;
		private Long groqApiUsage;
		private Long whisperUsage;
		private Long estimatedAiCost;
		private Long newSchoolsThisMonth;
		private Long newStudentsThisMonth;

		public SuperAdminDashboardResponseBuilder totalSchools(Long totalSchools) { this.totalSchools = totalSchools; return this; }
		public SuperAdminDashboardResponseBuilder activeSchools(Long activeSchools) { this.activeSchools = activeSchools; return this; }
		public SuperAdminDashboardResponseBuilder inactiveSchools(Long inactiveSchools) { this.inactiveSchools = inactiveSchools; return this; }
		public SuperAdminDashboardResponseBuilder totalStudents(Long totalStudents) { this.totalStudents = totalStudents; return this; }
		public SuperAdminDashboardResponseBuilder activeStudents(Long activeStudents) { this.activeStudents = activeStudents; return this; }
		public SuperAdminDashboardResponseBuilder individualUsers(Long individualUsers) { this.individualUsers = individualUsers; return this; }
		public SuperAdminDashboardResponseBuilder dailyActiveUsers(Long dailyActiveUsers) { this.dailyActiveUsers = dailyActiveUsers; return this; }
		public SuperAdminDashboardResponseBuilder monthlyActiveUsers(Long monthlyActiveUsers) { this.monthlyActiveUsers = monthlyActiveUsers; return this; }
		public SuperAdminDashboardResponseBuilder aiConversations(Long aiConversations) { this.aiConversations = aiConversations; return this; }
		public SuperAdminDashboardResponseBuilder speakingSessions(Long speakingSessions) { this.speakingSessions = speakingSessions; return this; }
		public SuperAdminDashboardResponseBuilder practiceMinutes(Long practiceMinutes) { this.practiceMinutes = practiceMinutes; return this; }
		public SuperAdminDashboardResponseBuilder lessonsCompleted(Long lessonsCompleted) { this.lessonsCompleted = lessonsCompleted; return this; }
		public SuperAdminDashboardResponseBuilder averageFluencyScore(Double averageFluencyScore) { this.averageFluencyScore = averageFluencyScore; return this; }
		public SuperAdminDashboardResponseBuilder averageGrammarScore(Double averageGrammarScore) { this.averageGrammarScore = averageGrammarScore; return this; }
		public SuperAdminDashboardResponseBuilder groqApiUsage(Long groqApiUsage) { this.groqApiUsage = groqApiUsage; return this; }
		public SuperAdminDashboardResponseBuilder whisperUsage(Long whisperUsage) { this.whisperUsage = whisperUsage; return this; }
		public SuperAdminDashboardResponseBuilder estimatedAiCost(Long estimatedAiCost) { this.estimatedAiCost = estimatedAiCost; return this; }
		public SuperAdminDashboardResponseBuilder newSchoolsThisMonth(Long newSchoolsThisMonth) { this.newSchoolsThisMonth = newSchoolsThisMonth; return this; }
		public SuperAdminDashboardResponseBuilder newStudentsThisMonth(Long newStudentsThisMonth) { this.newStudentsThisMonth = newStudentsThisMonth; return this; }

		public SuperAdminDashboardResponse build() {
			SuperAdminDashboardResponse obj = new SuperAdminDashboardResponse();
			obj.setTotalSchools(totalSchools);
			obj.setActiveSchools(activeSchools);
			obj.setInactiveSchools(inactiveSchools);
			obj.setTotalStudents(totalStudents);
			obj.setActiveStudents(activeStudents);
			obj.setIndividualUsers(individualUsers);
			obj.setDailyActiveUsers(dailyActiveUsers);
			obj.setMonthlyActiveUsers(monthlyActiveUsers);
			obj.setAiConversations(aiConversations);
			obj.setSpeakingSessions(speakingSessions);
			obj.setPracticeMinutes(practiceMinutes);
			obj.setLessonsCompleted(lessonsCompleted);
			obj.setAverageFluencyScore(averageFluencyScore);
			obj.setAverageGrammarScore(averageGrammarScore);
			obj.setGroqApiUsage(groqApiUsage);
			obj.setWhisperUsage(whisperUsage);
			obj.setEstimatedAiCost(estimatedAiCost);
			obj.setNewSchoolsThisMonth(newSchoolsThisMonth);
			obj.setNewStudentsThisMonth(newStudentsThisMonth);
			return obj;
		}
	}
}
