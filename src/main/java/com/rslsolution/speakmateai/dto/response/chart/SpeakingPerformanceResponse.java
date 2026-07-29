package com.rslsolution.speakmateai.dto.response.chart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakingPerformanceResponse {

	private Double averageScore;

	private Integer totalSessions;

	private Integer totalMinutes;

	private Double averagePronunciation;

	private Double averageFluency;

	private Double averageGrammar;

	private Double averageVocabulary;

	public Double getAverageScore() { return averageScore; }
	public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }

	public Integer getTotalSessions() { return totalSessions; }
	public void setTotalSessions(Integer totalSessions) { this.totalSessions = totalSessions; }

	public Integer getTotalMinutes() { return totalMinutes; }
	public void setTotalMinutes(Integer totalMinutes) { this.totalMinutes = totalMinutes; }

	public Double getAveragePronunciation() { return averagePronunciation; }
	public void setAveragePronunciation(Double averagePronunciation) { this.averagePronunciation = averagePronunciation; }

	public Double getAverageFluency() { return averageFluency; }
	public void setAverageFluency(Double averageFluency) { this.averageFluency = averageFluency; }

	public Double getAverageGrammar() { return averageGrammar; }
	public void setAverageGrammar(Double averageGrammar) { this.averageGrammar = averageGrammar; }

	public Double getAverageVocabulary() { return averageVocabulary; }
	public void setAverageVocabulary(Double averageVocabulary) { this.averageVocabulary = averageVocabulary; }

	public static SpeakingPerformanceResponseBuilder builder() {
		return new SpeakingPerformanceResponseBuilder();
	}

	public static class SpeakingPerformanceResponseBuilder {
		private Double averageScore;
		private Integer totalSessions;
		private Integer totalMinutes;
		private Double averagePronunciation;
		private Double averageFluency;
		private Double averageGrammar;
		private Double averageVocabulary;

		public SpeakingPerformanceResponseBuilder averageScore(Double averageScore) { this.averageScore = averageScore; return this; }
		public SpeakingPerformanceResponseBuilder totalSessions(Integer totalSessions) { this.totalSessions = totalSessions; return this; }
		public SpeakingPerformanceResponseBuilder totalMinutes(Integer totalMinutes) { this.totalMinutes = totalMinutes; return this; }
		public SpeakingPerformanceResponseBuilder averagePronunciation(Double averagePronunciation) { this.averagePronunciation = averagePronunciation; return this; }
		public SpeakingPerformanceResponseBuilder averageFluency(Double averageFluency) { this.averageFluency = averageFluency; return this; }
		public SpeakingPerformanceResponseBuilder averageGrammar(Double averageGrammar) { this.averageGrammar = averageGrammar; return this; }
		public SpeakingPerformanceResponseBuilder averageVocabulary(Double averageVocabulary) { this.averageVocabulary = averageVocabulary; return this; }

		public SpeakingPerformanceResponse build() {
			SpeakingPerformanceResponse obj = new SpeakingPerformanceResponse();
			obj.setAverageScore(averageScore);
			obj.setTotalSessions(totalSessions);
			obj.setTotalMinutes(totalMinutes);
			obj.setAveragePronunciation(averagePronunciation);
			obj.setAverageFluency(averageFluency);
			obj.setAverageGrammar(averageGrammar);
			obj.setAverageVocabulary(averageVocabulary);
			return obj;
		}
	}
}
