package com.rslsolution.speakmateai.dto.request;

import com.rslsolution.speakmateai.enums.SchoolStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolStatusRequest {

	@NotNull(message = "Status is required")
	private SchoolStatus status;

	public SchoolStatus getStatus() { return status; }
	public void setStatus(SchoolStatus status) { this.status = status; }
}
