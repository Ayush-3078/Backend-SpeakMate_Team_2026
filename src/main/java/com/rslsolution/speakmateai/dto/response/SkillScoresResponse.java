package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillScoresResponse {

	private Double averageFluency;
	private Double averageGrammarAccuracy;
	private Double averageVocabularyScore;
	private Double overallAverage;

	public static SkillScoresResponseBuilder builder() {
		return new SkillScoresResponseBuilder();
	}

	public static class SkillScoresResponseBuilder {
		private Double averageFluency;
		private Double averageGrammarAccuracy;
		private Double averageVocabularyScore;
		private Double overallAverage;

		public SkillScoresResponseBuilder averageFluency(Double averageFluency) { this.averageFluency = averageFluency; return this; }
		public SkillScoresResponseBuilder averageGrammarAccuracy(Double averageGrammarAccuracy) { this.averageGrammarAccuracy = averageGrammarAccuracy; return this; }
		public SkillScoresResponseBuilder averageVocabularyScore(Double averageVocabularyScore) { this.averageVocabularyScore = averageVocabularyScore; return this; }
		public SkillScoresResponseBuilder overallAverage(Double overallAverage) { this.overallAverage = overallAverage; return this; }

		public SkillScoresResponse build() {
			SkillScoresResponse obj = new SkillScoresResponse();
			obj.setAverageFluency(averageFluency);
			obj.setAverageGrammarAccuracy(averageGrammarAccuracy);
			obj.setAverageVocabularyScore(averageVocabularyScore);
			obj.setOverallAverage(overallAverage);
			return obj;
		}
	}
}
