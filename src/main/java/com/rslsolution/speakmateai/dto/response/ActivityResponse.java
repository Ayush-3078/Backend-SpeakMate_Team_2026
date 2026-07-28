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
public class ActivityResponse {

	private List<String> labels;
	private List<Integer> dailyPractice;
	private List<Integer> weeklyPractice;

	public static ActivityResponseBuilder builder() {
		return new ActivityResponseBuilder();
	}

	public static class ActivityResponseBuilder {
		private List<String> labels;
		private List<Integer> dailyPractice;
		private List<Integer> weeklyPractice;

		public ActivityResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public ActivityResponseBuilder dailyPractice(List<Integer> dailyPractice) { this.dailyPractice = dailyPractice; return this; }
		public ActivityResponseBuilder weeklyPractice(List<Integer> weeklyPractice) { this.weeklyPractice = weeklyPractice; return this; }

		public ActivityResponse build() {
			ActivityResponse obj = new ActivityResponse();
			obj.setLabels(labels);
			obj.setDailyPractice(dailyPractice);
			obj.setWeeklyPractice(weeklyPractice);
			return obj;
		}
	}
}
