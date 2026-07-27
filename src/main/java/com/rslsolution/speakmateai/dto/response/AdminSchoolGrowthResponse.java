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
public class AdminSchoolGrowthResponse {

	private List<String> labels;
	private List<Integer> totalSchools;
	private List<Integer> activeSchools;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getTotalSchools() { return totalSchools; }
	public void setTotalSchools(List<Integer> totalSchools) { this.totalSchools = totalSchools; }

	public List<Integer> getActiveSchools() { return activeSchools; }
	public void setActiveSchools(List<Integer> activeSchools) { this.activeSchools = activeSchools; }

	public static AdminSchoolGrowthResponseBuilder builder() {
		return new AdminSchoolGrowthResponseBuilder();
	}

	public static class AdminSchoolGrowthResponseBuilder {
		private List<String> labels;
		private List<Integer> totalSchools;
		private List<Integer> activeSchools;

		public AdminSchoolGrowthResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public AdminSchoolGrowthResponseBuilder totalSchools(List<Integer> totalSchools) { this.totalSchools = totalSchools; return this; }
		public AdminSchoolGrowthResponseBuilder activeSchools(List<Integer> activeSchools) { this.activeSchools = activeSchools; return this; }

		public AdminSchoolGrowthResponse build() {
			AdminSchoolGrowthResponse obj = new AdminSchoolGrowthResponse();
			obj.setLabels(labels);
			obj.setTotalSchools(totalSchools);
			obj.setActiveSchools(activeSchools);
			return obj;
		}
	}
}
