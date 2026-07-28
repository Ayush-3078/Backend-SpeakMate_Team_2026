package com.rslsolution.speakmateai.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardResponse {

	private List<LeaderboardEntry> entries;

	public static LeaderboardResponseBuilder builder() {
		return new LeaderboardResponseBuilder();
	}

	public static class LeaderboardResponseBuilder {
		private List<LeaderboardEntry> entries;

		public LeaderboardResponseBuilder entries(List<LeaderboardEntry> entries) { this.entries = entries; return this; }

		public LeaderboardResponse build() {
			LeaderboardResponse obj = new LeaderboardResponse();
			obj.setEntries(entries);
			return obj;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class LeaderboardEntry {
		private Long userId;
		private String firstName;
		private String lastName;
		private String email;
		private Double averageScore;
		private Integer totalSessions;
		private Integer rank;

		public static LeaderboardEntryBuilder builder() {
			return new LeaderboardEntryBuilder();
		}

		public static class LeaderboardEntryBuilder {
			private Long userId;
			private String firstName;
			private String lastName;
			private String email;
			private Double averageScore;
			private Integer totalSessions;
			private Integer rank;

			public LeaderboardEntryBuilder userId(Long userId) { this.userId = userId; return this; }
			public LeaderboardEntryBuilder firstName(String firstName) { this.firstName = firstName; return this; }
			public LeaderboardEntryBuilder lastName(String lastName) { this.lastName = lastName; return this; }
			public LeaderboardEntryBuilder email(String email) { this.email = email; return this; }
			public LeaderboardEntryBuilder averageScore(Double averageScore) { this.averageScore = averageScore; return this; }
			public LeaderboardEntryBuilder totalSessions(Integer totalSessions) { this.totalSessions = totalSessions; return this; }
			public LeaderboardEntryBuilder rank(Integer rank) { this.rank = rank; return this; }

			public LeaderboardEntry build() {
				LeaderboardEntry obj = new LeaderboardEntry();
				obj.setUserId(userId);
				obj.setFirstName(firstName);
				obj.setLastName(lastName);
				obj.setEmail(email);
				obj.setAverageScore(averageScore);
				obj.setTotalSessions(totalSessions);
				obj.setRank(rank);
				return obj;
			}
		}
	}
}
