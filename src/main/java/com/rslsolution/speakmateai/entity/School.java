package com.rslsolution.speakmateai.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.rslsolution.speakmateai.enums.SchoolStatus;
import com.rslsolution.speakmateai.enums.SubscriptionPlan;
import com.rslsolution.speakmateai.enums.SubscriptionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schools")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "school_name", nullable = false)
	private String schoolName;

	@Column(name = "school_code", unique = true, nullable = false, length = 50)
	private String schoolCode;

	@Column(name = "location")
	private String location;

	@Column(name = "admin_name")
	private String admin;

	@Column(name = "total_students")
	private Integer totalStudents;

	@Enumerated(EnumType.STRING)
	@Column(name = "subscription_plan", length = 20)
	private SubscriptionPlan subscriptionPlan;

	@Enumerated(EnumType.STRING)
	@Column(name = "subscription_status", length = 20)
	private SubscriptionStatus subscriptionStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 20)
	private SchoolStatus status;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@OneToMany(mappedBy = "school")
	private java.util.List<User> students;

	@OneToMany(mappedBy = "school")
	private java.util.List<User> teachers;

	@PrePersist
	public void prePersist() {
		if (status == null) {
			status = SchoolStatus.ACTIVE;
		}
		if (totalStudents == null) {
			totalStudents = 0;
		}
	}
}
