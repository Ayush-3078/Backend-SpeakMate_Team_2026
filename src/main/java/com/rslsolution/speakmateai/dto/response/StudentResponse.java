package com.rslsolution.speakmateai.dto.response;

import com.rslsolution.speakmateai.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Role role;
	private Boolean active;
	private String userType;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }

	public Boolean getActive() { return active; }
	public void setActive(Boolean active) { this.active = active; }

	public String getUserType() { return userType; }
	public void setUserType(String userType) { this.userType = userType; }

	public static StudentResponseBuilder builder() {
		return new StudentResponseBuilder();
	}

	public static class StudentResponseBuilder {
		private Long id;
		private String firstName;
		private String lastName;
		private String email;
		private Role role;
		private Boolean active;
		private String userType;

		public StudentResponseBuilder id(Long id) { this.id = id; return this; }
		public StudentResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
		public StudentResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
		public StudentResponseBuilder email(String email) { this.email = email; return this; }
		public StudentResponseBuilder role(Role role) { this.role = role; return this; }
		public StudentResponseBuilder active(Boolean active) { this.active = active; return this; }
		public StudentResponseBuilder userType(String userType) { this.userType = userType; return this; }

		public StudentResponse build() {
			StudentResponse obj = new StudentResponse();
			obj.setId(id);
			obj.setFirstName(firstName);
			obj.setLastName(lastName);
			obj.setEmail(email);
			obj.setRole(role);
			obj.setActive(active);
			obj.setUserType(userType);
			return obj;
		}
	}
}
