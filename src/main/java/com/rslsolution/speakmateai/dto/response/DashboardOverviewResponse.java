package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardOverviewResponse {

	private Integer totalUsers;

	private Integer schoolUsers;

	private Integer activeUsers;

	private Integer inactiveUsers;

	private Integer newUsers;

	public Integer getTotalUsers() { return totalUsers; }
	public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }

	public Integer getSchoolUsers() { return schoolUsers; }
	public void setSchoolUsers(Integer schoolUsers) { this.schoolUsers = schoolUsers; }

	public Integer getActiveUsers() { return activeUsers; }
	public void setActiveUsers(Integer activeUsers) { this.activeUsers = activeUsers; }

	public Integer getInactiveUsers() { return inactiveUsers; }
	public void setInactiveUsers(Integer inactiveUsers) { this.inactiveUsers = inactiveUsers; }

	public Integer getNewUsers() { return newUsers; }
	public void setNewUsers(Integer newUsers) { this.newUsers = newUsers; }

	public static DashboardOverviewResponseBuilder builder() {
		return new DashboardOverviewResponseBuilder();
	}

	public static class DashboardOverviewResponseBuilder {
		private Integer totalUsers;
		private Integer schoolUsers;
		private Integer activeUsers;
		private Integer inactiveUsers;
		private Integer newUsers;

		public DashboardOverviewResponseBuilder totalUsers(Integer totalUsers) { this.totalUsers = totalUsers; return this; }
		public DashboardOverviewResponseBuilder schoolUsers(Integer schoolUsers) { this.schoolUsers = schoolUsers; return this; }
		public DashboardOverviewResponseBuilder activeUsers(Integer activeUsers) { this.activeUsers = activeUsers; return this; }
		public DashboardOverviewResponseBuilder inactiveUsers(Integer inactiveUsers) { this.inactiveUsers = inactiveUsers; return this; }
		public DashboardOverviewResponseBuilder newUsers(Integer newUsers) { this.newUsers = newUsers; return this; }

		public DashboardOverviewResponse build() {
			DashboardOverviewResponse obj = new DashboardOverviewResponse();
			obj.setTotalUsers(totalUsers);
			obj.setSchoolUsers(schoolUsers);
			obj.setActiveUsers(activeUsers);
			obj.setInactiveUsers(inactiveUsers);
			obj.setNewUsers(newUsers);
			return obj;
		}
	}
}
