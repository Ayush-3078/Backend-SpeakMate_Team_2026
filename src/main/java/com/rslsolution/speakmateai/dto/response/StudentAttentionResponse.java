package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttentionResponse {

	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String reason;

	public static StudentAttentionResponseBuilder builder() {
		return new StudentAttentionResponseBuilder();
	}

	public static class StudentAttentionResponseBuilder {
		private Long userId;
		private String firstName;
		private String lastName;
		private String email;
		private String reason;

		public StudentAttentionResponseBuilder userId(Long userId) { this.userId = userId; return this; }
		public StudentAttentionResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
		public StudentAttentionResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
		public StudentAttentionResponseBuilder email(String email) { this.email = email; return this; }
		public StudentAttentionResponseBuilder reason(String reason) { this.reason = reason; return this; }

		public StudentAttentionResponse build() {
			StudentAttentionResponse obj = new StudentAttentionResponse();
			obj.setUserId(userId);
			obj.setFirstName(firstName);
			obj.setLastName(lastName);
			obj.setEmail(email);
			obj.setReason(reason);
			return obj;
		}
	}
}
