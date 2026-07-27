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
public class WeeklyActiveUsersResponse {

	private List<String> labels;
	private List<Integer> activeUsers;
	private List<Integer> newUsers;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getActiveUsers() { return activeUsers; }
	public void setActiveUsers(List<Integer> activeUsers) { this.activeUsers = activeUsers; }

	public List<Integer> getNewUsers() { return newUsers; }
	public void setNewUsers(List<Integer> newUsers) { this.newUsers = newUsers; }

	public static WeeklyActiveUsersResponseBuilder builder() {
		return new WeeklyActiveUsersResponseBuilder();
	}

	public static class WeeklyActiveUsersResponseBuilder {
		private List<String> labels;
		private List<Integer> activeUsers;
		private List<Integer> newUsers;

		public WeeklyActiveUsersResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public WeeklyActiveUsersResponseBuilder activeUsers(List<Integer> activeUsers) { this.activeUsers = activeUsers; return this; }
		public WeeklyActiveUsersResponseBuilder newUsers(List<Integer> newUsers) { this.newUsers = newUsers; return this; }

		public WeeklyActiveUsersResponse build() {
			WeeklyActiveUsersResponse obj = new WeeklyActiveUsersResponse();
			obj.setLabels(labels);
			obj.setActiveUsers(activeUsers);
			obj.setNewUsers(newUsers);
			return obj;
		}
	}
}
