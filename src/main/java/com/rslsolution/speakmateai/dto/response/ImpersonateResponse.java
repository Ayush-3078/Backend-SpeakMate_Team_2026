package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpersonateResponse {

	private String token;
	private String message;
	private String impersonatedEmail;

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public String getImpersonatedEmail() { return impersonatedEmail; }
	public void setImpersonatedEmail(String impersonatedEmail) { this.impersonatedEmail = impersonatedEmail; }

	public static ImpersonateResponseBuilder builder() {
		return new ImpersonateResponseBuilder();
	}

	public static class ImpersonateResponseBuilder {
		private String token;
		private String message;
		private String impersonatedEmail;

		public ImpersonateResponseBuilder token(String token) { this.token = token; return this; }
		public ImpersonateResponseBuilder message(String message) { this.message = message; return this; }
		public ImpersonateResponseBuilder impersonatedEmail(String impersonatedEmail) { this.impersonatedEmail = impersonatedEmail; return this; }

		public ImpersonateResponse build() {
			ImpersonateResponse obj = new ImpersonateResponse();
			obj.setToken(token);
			obj.setMessage(message);
			obj.setImpersonatedEmail(impersonatedEmail);
			return obj;
		}
	}
}
