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
public class AdminUserGrowthResponse {

	private List<String> labels;
	private List<Integer> totalUsers;
	private List<Integer> activeUsers;
	private List<Integer> newUsers;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getTotalUsers() { return totalUsers; }
	public void setTotalUsers(List<Integer> totalUsers) { this.totalUsers = totalUsers; }

	public List<Integer> getActiveUsers() { return activeUsers; }
	public void setActiveUsers(List<Integer> activeUsers) { this.activeUsers = activeUsers; }

	public List<Integer> getNewUsers() { return newUsers; }
	public void setNewUsers(List<Integer> newUsers) { this.newUsers = newUsers; }

	public static AdminUserGrowthResponseBuilder builder() {
		return new AdminUserGrowthResponseBuilder();
	}

	public static class AdminUserGrowthResponseBuilder {
		private List<String> labels;
		private List<Integer> totalUsers;
		private List<Integer> activeUsers;
		private List<Integer> newUsers;

		public AdminUserGrowthResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public AdminUserGrowthResponseBuilder totalUsers(List<Integer> totalUsers) { this.totalUsers = totalUsers; return this; }
		public AdminUserGrowthResponseBuilder activeUsers(List<Integer> activeUsers) { this.activeUsers = activeUsers; return this; }
		public AdminUserGrowthResponseBuilder newUsers(List<Integer> newUsers) { this.newUsers = newUsers; return this; }

		public AdminUserGrowthResponse build() {
			AdminUserGrowthResponse obj = new AdminUserGrowthResponse();
			obj.setLabels(labels);
			obj.setTotalUsers(totalUsers);
			obj.setActiveUsers(activeUsers);
			obj.setNewUsers(newUsers);
			return obj;
		}
	}
}
