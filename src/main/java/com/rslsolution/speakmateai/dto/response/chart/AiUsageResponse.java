package com.rslsolution.speakmateai.dto.response.chart;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiUsageResponse {

	private List<String> labels;
	private List<Integer> chatMessages;
	private List<Integer> speakingSessions;
	private List<Integer> grammarChecks;
	private List<Integer> vocabularyActions;

	public List<String> getLabels() { return labels; }
	public void setLabels(List<String> labels) { this.labels = labels; }

	public List<Integer> getChatMessages() { return chatMessages; }
	public void setChatMessages(List<Integer> chatMessages) { this.chatMessages = chatMessages; }

	public List<Integer> getSpeakingSessions() { return speakingSessions; }
	public void setSpeakingSessions(List<Integer> speakingSessions) { this.speakingSessions = speakingSessions; }

	public List<Integer> getGrammarChecks() { return grammarChecks; }
	public void setGrammarChecks(List<Integer> grammarChecks) { this.grammarChecks = grammarChecks; }

	public List<Integer> getVocabularyActions() { return vocabularyActions; }
	public void setVocabularyActions(List<Integer> vocabularyActions) { this.vocabularyActions = vocabularyActions; }

	public static AiUsageResponseBuilder builder() {
		return new AiUsageResponseBuilder();
	}

	public static class AiUsageResponseBuilder {
		private List<String> labels;
		private List<Integer> chatMessages;
		private List<Integer> speakingSessions;
		private List<Integer> grammarChecks;
		private List<Integer> vocabularyActions;

		public AiUsageResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public AiUsageResponseBuilder chatMessages(List<Integer> chatMessages) { this.chatMessages = chatMessages; return this; }
		public AiUsageResponseBuilder speakingSessions(List<Integer> speakingSessions) { this.speakingSessions = speakingSessions; return this; }
		public AiUsageResponseBuilder grammarChecks(List<Integer> grammarChecks) { this.grammarChecks = grammarChecks; return this; }
		public AiUsageResponseBuilder vocabularyActions(List<Integer> vocabularyActions) { this.vocabularyActions = vocabularyActions; return this; }

		public AiUsageResponse build() {
			AiUsageResponse obj = new AiUsageResponse();
			obj.setLabels(labels);
			obj.setChatMessages(chatMessages);
			obj.setSpeakingSessions(speakingSessions);
			obj.setGrammarChecks(grammarChecks);
			obj.setVocabularyActions(vocabularyActions);
			return obj;
		}
	}
}
