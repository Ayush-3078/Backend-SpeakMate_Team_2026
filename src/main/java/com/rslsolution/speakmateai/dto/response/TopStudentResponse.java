package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopStudentResponse {

	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private Double averageScore;
	private Integer totalSessions;

	public static TopStudentResponseBuilder builder() {
		return new TopStudentResponseBuilder();
	}

	public static class TopStudentResponseBuilder {
		private Long userId;
		private String firstName;
		private String lastName;
		private String email;
		private Double averageScore;
		private Integer totalSessions;

		public TopStudentResponseBuilder userId(Long userId) { this.userId = userId; return this; }
		public TopStudentResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
		public TopStudentResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
		public TopStudentResponseBuilder email(String email) { this.email = email; return this; }
		public TopStudentResponseBuilder averageScore(Double averageScore) { this.averageScore = averageScore; return this; }
		public TopStudentResponseBuilder totalSessions(Integer totalSessions) { this.totalSessions = totalSessions; return this; }

		public TopStudentResponse build() {
			TopStudentResponse obj = new TopStudentResponse();
			obj.setUserId(userId);
			obj.setFirstName(firstName);
			obj.setLastName(lastName);
			obj.setEmail(email);
			obj.setAverageScore(averageScore);
			obj.setTotalSessions(totalSessions);
			return obj;
		}
	}
}
