package com.rslsolution.speakmateai.dto.request;

import com.rslsolution.speakmateai.enums.SchoolStatus;
import com.rslsolution.speakmateai.enums.SubscriptionPlan;
import com.rslsolution.speakmateai.enums.SubscriptionStatus;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolUpdateRequest {

	@Size(min = 2, max = 100, message = "School name must be between 2 and 100 characters")
	private String schoolName;

	@Size(max = 255, message = "Location must not exceed 255 characters")
	private String location;

	@Size(max = 100, message = "Admin name must not exceed 100 characters")
	private String admin;

	private Integer totalStudents;

	private SubscriptionPlan subscriptionPlan;

	private SubscriptionStatus subscriptionStatus;

	private SchoolStatus status;

	public String getSchoolName() { return schoolName; }
	public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

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
}
