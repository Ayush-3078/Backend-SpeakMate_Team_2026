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
public class UserGrowthResponse {

	private List<String> labels;
	private List<Integer> values;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getValues() { return values; }
	public void setValues(List<Integer> values) { this.values = values; }

	public static UserGrowthResponseBuilder builder() {
		return new UserGrowthResponseBuilder();
	}

	public static class UserGrowthResponseBuilder {
		private List<String> labels;
		private List<Integer> values;

		public UserGrowthResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public UserGrowthResponseBuilder values(List<Integer> values) { this.values = values; return this; }

		public UserGrowthResponse build() {
			UserGrowthResponse obj = new UserGrowthResponse();
			obj.setLabels(labels);
			obj.setValues(values);
			return obj;
		}
	}
}
