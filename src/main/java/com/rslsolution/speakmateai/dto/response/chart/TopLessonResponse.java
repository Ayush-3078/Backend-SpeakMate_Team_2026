package com.rslsolution.speakmateai.dto.response.chart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopLessonResponse {

	private Long lessonId;

	private String title;

	private String category;

	private Integer completions;

	private Integer xpEarned;

	public Long getLessonId() { return lessonId; }
	public void setLessonId(Long lessonId) { this.lessonId = lessonId; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public Integer getCompletions() { return completions; }
	public void setCompletions(Integer completions) { this.completions = completions; }

	public Integer getXpEarned() { return xpEarned; }
	public void setXpEarned(Integer xpEarned) { this.xpEarned = xpEarned; }

	public static TopLessonResponseBuilder builder() {
		return new TopLessonResponseBuilder();
	}

	public static class TopLessonResponseBuilder {
		private Long lessonId;
		private String title;
		private String category;
		private Integer completions;
		private Integer xpEarned;

		public TopLessonResponseBuilder lessonId(Long lessonId) { this.lessonId = lessonId; return this; }
		public TopLessonResponseBuilder title(String title) { this.title = title; return this; }
		public TopLessonResponseBuilder category(String category) { this.category = category; return this; }
		public TopLessonResponseBuilder completions(Integer completions) { this.completions = completions; return this; }
		public TopLessonResponseBuilder xpEarned(Integer xpEarned) { this.xpEarned = xpEarned; return this; }

		public TopLessonResponse build() {
			TopLessonResponse obj = new TopLessonResponse();
			obj.setLessonId(lessonId);
			obj.setTitle(title);
			obj.setCategory(category);
			obj.setCompletions(completions);
			obj.setXpEarned(xpEarned);
			return obj;
		}
	}
}
