package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentImportResponse {

	private int totalProcessed;
	private int successCount;
	private int failureCount;
	private java.util.List<String> errors;

	public static StudentImportResponseBuilder builder() {
		return new StudentImportResponseBuilder();
	}

	public static class StudentImportResponseBuilder {
		private int totalProcessed;
		private int successCount;
		private int failureCount;
		private java.util.List<String> errors;

		public StudentImportResponseBuilder totalProcessed(int totalProcessed) { this.totalProcessed = totalProcessed; return this; }
		public StudentImportResponseBuilder successCount(int successCount) { this.successCount = successCount; return this; }
		public StudentImportResponseBuilder failureCount(int failureCount) { this.failureCount = failureCount; return this; }
		public StudentImportResponseBuilder errors(java.util.List<String> errors) { this.errors = errors; return this; }

		public StudentImportResponse build() {
			StudentImportResponse obj = new StudentImportResponse();
			obj.setTotalProcessed(totalProcessed);
			obj.setSuccessCount(successCount);
			obj.setFailureCount(failureCount);
			obj.setErrors(errors);
			return obj;
		}
	}
}
