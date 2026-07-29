package com.rslsolution.speakmateai.dto.request;

import com.rslsolution.speakmateai.enums.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Last name is required")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Student ID is required")
	private String studentId;

	private Long schoolId;

	private Status status;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters")
	private String password;
}
