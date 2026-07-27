package com.rslsolution.speakmateai.dto.response.chart;

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
public class LearningProgressResponse {

	private List<String> labels;
	private List<Integer> completedLessons;
	private List<Integer> totalLessons;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getCompletedLessons() { return completedLessons; }
	public void setCompletedLessons(List<Integer> completedLessons) { this.completedLessons = completedLessons; }

	public List<Integer> getTotalLessons() { return totalLessons; }
	public void setTotalLessons(List<Integer> totalLessons) { this.totalLessons = totalLessons; }

	public static LearningProgressResponseBuilder builder() {
		return new LearningProgressResponseBuilder();
	}

	public static class LearningProgressResponseBuilder {
		private List<String> labels;
		private List<Integer> completedLessons;
		private List<Integer> totalLessons;

		public LearningProgressResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public LearningProgressResponseBuilder completedLessons(List<Integer> completedLessons) { this.completedLessons = completedLessons; return this; }
		public LearningProgressResponseBuilder totalLessons(List<Integer> totalLessons) { this.totalLessons = totalLessons; return this; }

		public LearningProgressResponse build() {
			LearningProgressResponse obj = new LearningProgressResponse();
			obj.setLabels(labels);
			obj.setCompletedLessons(completedLessons);
			obj.setTotalLessons(totalLessons);
			return obj;
		}
	}
}
