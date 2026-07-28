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
public class SchoolProgressResponse {

	private Long totalLessons;
	private Long completedLessons;
	private Long inProgressLessons;
	private List<LessonProgressItem> recentProgress;

	public static SchoolProgressResponseBuilder builder() {
		return new SchoolProgressResponseBuilder();
	}

	public static class SchoolProgressResponseBuilder {
		private Long totalLessons;
		private Long completedLessons;
		private Long inProgressLessons;
		private List<LessonProgressItem> recentProgress;

		public SchoolProgressResponseBuilder totalLessons(Long totalLessons) { this.totalLessons = totalLessons; return this; }
		public SchoolProgressResponseBuilder completedLessons(Long completedLessons) { this.completedLessons = completedLessons; return this; }
		public SchoolProgressResponseBuilder inProgressLessons(Long inProgressLessons) { this.inProgressLessons = inProgressLessons; return this; }
		public SchoolProgressResponseBuilder recentProgress(List<LessonProgressItem> recentProgress) { this.recentProgress = recentProgress; return this; }

		public SchoolProgressResponse build() {
			SchoolProgressResponse obj = new SchoolProgressResponse();
			obj.setTotalLessons(totalLessons);
			obj.setCompletedLessons(completedLessons);
			obj.setInProgressLessons(inProgressLessons);
			obj.setRecentProgress(recentProgress);
			return obj;
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class LessonProgressItem {
		private String studentName;
		private String lessonTitle;
		private Integer progressPercent;
		private String status;

		public static LessonProgressItemBuilder builder() {
			return new LessonProgressItemBuilder();
		}

		public static class LessonProgressItemBuilder {
			private String studentName;
			private String lessonTitle;
			private Integer progressPercent;
			private String status;

			public LessonProgressItemBuilder studentName(String studentName) { this.studentName = studentName; return this; }
			public LessonProgressItemBuilder lessonTitle(String lessonTitle) { this.lessonTitle = lessonTitle; return this; }
			public LessonProgressItemBuilder progressPercent(Integer progressPercent) { this.progressPercent = progressPercent; return this; }
			public LessonProgressItemBuilder status(String status) { this.status = status; return this; }

			public LessonProgressItem build() {
				LessonProgressItem obj = new LessonProgressItem();
				obj.setStudentName(studentName);
				obj.setLessonTitle(lessonTitle);
				obj.setProgressPercent(progressPercent);
				obj.setStatus(status);
				return obj;
			}
		}
	}
}
