package com.rslsolution.speakmateai.dto.response;

import java.time.LocalDateTime;

import com.rslsolution.speakmateai.enums.SchoolStatus;
import com.rslsolution.speakmateai.enums.SubscriptionPlan;
import com.rslsolution.speakmateai.enums.SubscriptionStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolResponse {

	private Long id;
	private String schoolName;
	private String schoolCode;
	private String location;
	private String admin;
	private Integer totalStudents;
	private SubscriptionPlan subscriptionPlan;
	private SubscriptionStatus subscriptionStatus;
	private SchoolStatus status;
	private LocalDateTime createdDate;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getSchoolName() { return schoolName; }
	public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

	public String getSchoolCode() { return schoolCode; }
	public void setSchoolCode(String schoolCode) { this.schoolCode = schoolCode; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	public String getAdmin() { return admin; }
	public void setAdmin(String admin) { this.admin = admin; }

	public Integer getTotalStudents() { return totalStudents; }
	public void setTotalStudents(Integer totalStudents) { this.totalStudents = totalStudents; }

	public SubscriptionPlan getSubscriptionPlan() { return subscriptionPlan; }
	public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

	public SubscriptionStatus getSubscriptionStatus() { return subscriptionStatus; }
	public void setSubscriptionStatus(SubscriptionStatus subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; }

	public SchoolStatus getStatus() { return status; }
	public void setStatus(SchoolStatus status) { this.status = status; }

	public LocalDateTime getCreatedDate() { return createdDate; }
	public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

	public static SchoolResponseBuilder builder() {
		return new SchoolResponseBuilder();
	}

	public static class SchoolResponseBuilder {
		private Long id;
		private String schoolName;
		private String schoolCode;
		private String location;
		private String admin;
		private Integer totalStudents;
		private SubscriptionPlan subscriptionPlan;
		private SubscriptionStatus subscriptionStatus;
		private SchoolStatus status;
		private LocalDateTime createdDate;

		public SchoolResponseBuilder id(Long id) { this.id = id; return this; }
		public SchoolResponseBuilder schoolName(String schoolName) { this.schoolName = schoolName; return this; }
		public SchoolResponseBuilder schoolCode(String schoolCode) { this.schoolCode = schoolCode; return this; }
		public SchoolResponseBuilder location(String location) { this.location = location; return this; }
		public SchoolResponseBuilder admin(String admin) { this.admin = admin; return this; }
		public SchoolResponseBuilder totalStudents(Integer totalStudents) { this.totalStudents = totalStudents; return this; }
		public SchoolResponseBuilder subscriptionPlan(SubscriptionPlan subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; return this; }
		public SchoolResponseBuilder subscriptionStatus(SubscriptionStatus subscriptionStatus) { this.subscriptionStatus = subscriptionStatus; return this; }
		public SchoolResponseBuilder status(SchoolStatus status) { this.status = status; return this; }
		public SchoolResponseBuilder createdDate(LocalDateTime createdDate) { this.createdDate = createdDate; return this; }

		public SchoolResponse build() {
			SchoolResponse obj = new SchoolResponse();
			obj.setId(id);
			obj.setSchoolName(schoolName);
			obj.setSchoolCode(schoolCode);
			obj.setLocation(location);
			obj.setAdmin(admin);
			obj.setTotalStudents(totalStudents);
			obj.setSubscriptionPlan(subscriptionPlan);
			obj.setSubscriptionStatus(subscriptionStatus);
			obj.setStatus(status);
			obj.setCreatedDate(createdDate);
			return obj;
		}
	}
}
