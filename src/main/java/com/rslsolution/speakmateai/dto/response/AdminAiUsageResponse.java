package com.rslsolution.speakmateai.dto.response;

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
public class AdminAiUsageResponse {

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

	public static AdminAiUsageResponseBuilder builder() {
		return new AdminAiUsageResponseBuilder();
	}

	public static class AdminAiUsageResponseBuilder {
		private List<String> labels;
		private List<Integer> chatMessages;
		private List<Integer> speakingSessions;
		private List<Integer> grammarChecks;
		private List<Integer> vocabularyActions;

		public AdminAiUsageResponseBuilder labels(List<String> labels) { this.labels = labels; return this; }
		public AdminAiUsageResponseBuilder chatMessages(List<Integer> chatMessages) { this.chatMessages = chatMessages; return this; }
		public AdminAiUsageResponseBuilder speakingSessions(List<Integer> speakingSessions) { this.speakingSessions = speakingSessions; return this; }
		public AdminAiUsageResponseBuilder grammarChecks(List<Integer> grammarChecks) { this.grammarChecks = grammarChecks; return this; }
		public AdminAiUsageResponseBuilder vocabularyActions(List<Integer> vocabularyActions) { this.vocabularyActions = vocabularyActions; return this; }

		public AdminAiUsageResponse build() {
			AdminAiUsageResponse obj = new AdminAiUsageResponse();
			obj.setLabels(labels);
			obj.setChatMessages(chatMessages);
			obj.setSpeakingSessions(speakingSessions);
			obj.setGrammarChecks(grammarChecks);
			obj.setVocabularyActions(vocabularyActions);
			return obj;
		}
	}
}
