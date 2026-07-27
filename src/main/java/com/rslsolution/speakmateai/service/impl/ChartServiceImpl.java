package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.response.chart.AiUsageResponse;
import com.rslsolution.speakmateai.dto.response.chart.LearningProgressResponse;
import com.rslsolution.speakmateai.dto.response.chart.SpeakingPerformanceResponse;
import com.rslsolution.speakmateai.dto.response.chart.TopLessonResponse;
import com.rslsolution.speakmateai.dto.response.chart.UserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.chart.WeeklyActiveUsersResponse;
import com.rslsolution.speakmateai.entity.Lesson;
import com.rslsolution.speakmateai.entity.LessonProgress;
import com.rslsolution.speakmateai.entity.SpeakingSession;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.service.ChartService;

@Service
@Transactional
public class ChartServiceImpl implements ChartService {

	private final UserRepository userRepository;
	private final SpeakingSessionRepository speakingSessionRepository;
	private final LessonProgressRepository lessonProgressRepository;
	private final GrammarHistoryRepository grammarHistoryRepository;
	private final VocabularyRepository vocabularyRepository;
	private final ChatMessageRepository chatMessageRepository;

	public ChartServiceImpl(UserRepository userRepository,
			SpeakingSessionRepository speakingSessionRepository,
			LessonProgressRepository lessonProgressRepository,
			GrammarHistoryRepository grammarHistoryRepository,
			VocabularyRepository vocabularyRepository,
			ChatMessageRepository chatMessageRepository) {
		this.userRepository = userRepository;
		this.speakingSessionRepository = speakingSessionRepository;
		this.lessonProgressRepository = lessonProgressRepository;
		this.grammarHistoryRepository = grammarHistoryRepository;
		this.vocabularyRepository = vocabularyRepository;
		this.chatMessageRepository = chatMessageRepository;
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	@Override
	public UserGrowthResponse getUserGrowth() {
		List<User> allUsers = userRepository.findAll();
		Map<String, Long> countsByDate = new HashMap<>();

		for (User user : allUsers) {
			if (user.getCreatedAt() != null) {
				String date = user.getCreatedAt().toLocalDate().toString();
				countsByDate.merge(date, 1L, Long::sum);
			}
		}

		List<String> labels = new ArrayList<>(countsByDate.keySet()).stream().sorted().toList();
		List<Integer> values = labels.stream().map(l -> countsByDate.get(l).intValue()).toList();

		if (labels.isEmpty()) {
			labels = List.of(LocalDate.now().toString());
			values = List.of(0);
		}

		return UserGrowthResponse.builder().labels(labels).values(values).build();
	}

	@Override
	public LearningProgressResponse getLearningProgress() {
		User user = getCurrentUser();
		List<LessonProgress> progresses = lessonProgressRepository.findByUser(user);

		Map<String, Integer> completedByMonth = new HashMap<>();
		Map<String, Integer> startedByMonth = new HashMap<>();

		for (LessonProgress p : progresses) {
			if (Boolean.TRUE.equals(p.getCompleted()) && p.getCompletedAt() != null) {
				String month = p.getCompletedAt().toLocalDate().withDayOfMonth(1).toString();
				completedByMonth.merge(month, 1, Integer::sum);
			}
			if (p.getLastOpenedAt() != null) {
				String month = p.getLastOpenedAt().toLocalDate().withDayOfMonth(1).toString();
				startedByMonth.merge(month, 1, Integer::sum);
			}
		}

		List<String> labels = new ArrayList<>(startedByMonth.keySet()).stream().sorted().toList();
		if (labels.isEmpty()) {
			labels = List.of(LocalDate.now().withDayOfMonth(1).toString());
		}

		List<Integer> completedList = labels.stream().map(l -> completedByMonth.getOrDefault(l, 0)).toList();
		List<Integer> totalList = labels.stream().map(l -> startedByMonth.getOrDefault(l, 0)).toList();

		return LearningProgressResponse.builder().labels(labels).completedLessons(completedList).totalLessons(totalList).build();
	}

	@Override
	public AiUsageResponse getAiUsage() {
		User user = getCurrentUser();

		List<String> labels = new ArrayList<>();
		List<Integer> chatMessagesList = new ArrayList<>();
		List<Integer> speakingSessionsList = new ArrayList<>();
		List<Integer> grammarChecksList = new ArrayList<>();
		List<Integer> vocabularyActionsList = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDateTime endOfDay = today.atTime(23, 59, 59);
		LocalDateTime startOfDay6 = today.minusDays(6).atStartOfDay();

		for (int i = 6; i >= 0; i--) {
			LocalDate d = today.minusDays(i);
			LocalDateTime dayStart = d.atStartOfDay();
			LocalDateTime dayEnd = d.atTime(23, 59, 59);
			labels.add(d.toString());

			long chatCount = chatMessageRepository.countBySessionUserAndCreatedAtBetween(user, dayStart, dayEnd);
			long sessionCount = speakingSessionRepository.countByUserAndCreatedAtBetween(user, dayStart, dayEnd);
			long grammarCount = grammarHistoryRepository.countByUserAndCreatedAtBetween(user, dayStart, dayEnd);
			long vocabCount = vocabularyRepository.countByUserAndCreatedAtBetween(user, dayStart, dayEnd);

			chatMessagesList.add((int) chatCount);
			speakingSessionsList.add((int) sessionCount);
			grammarChecksList.add((int) grammarCount);
			vocabularyActionsList.add((int) vocabCount);
		}

		return AiUsageResponse.builder()
				.labels(labels)
				.chatMessages(chatMessagesList)
				.speakingSessions(speakingSessionsList)
				.grammarChecks(grammarChecksList)
				.vocabularyActions(vocabularyActionsList)
				.build();
	}

	@Override
	public WeeklyActiveUsersResponse getWeeklyActiveUsers() {
		List<User> allUsers = userRepository.findAll();

		List<String> labels = new ArrayList<>();
		List<Integer> activeUsersList = new ArrayList<>();
		List<Integer> newUsersList = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);

		for (int i = 0; i < 7; i++) {
			LocalDate day = monday.plusDays(i);
			labels.add(day.toString());

			LocalDate finalDay = day;
			int activeCount = (int) allUsers.stream()
					.filter(u -> u.getUpdatedAt() != null && u.getUpdatedAt().toLocalDate().isEqual(finalDay))
					.count();

			int newCount = (int) allUsers.stream()
					.filter(u -> u.getCreatedAt() != null && u.getCreatedAt().toLocalDate().isEqual(finalDay))
					.count();

			activeUsersList.add(activeCount);
			newUsersList.add(newCount);
		}

		return WeeklyActiveUsersResponse.builder().labels(labels).activeUsers(activeUsersList).newUsers(newUsersList).build();
	}

	@Override
	public List<TopLessonResponse> getTopLessons() {
		List<LessonProgress> allProgress = lessonProgressRepository.findAll();
		Map<Long, Integer> completionCountByLesson = new HashMap<>();
		Map<Long, Integer> xpByLesson = new HashMap<>();
		Map<Long, Lesson> lessonMap = new HashMap<>();

		for (LessonProgress p : allProgress) {
			if (p.getLesson() != null) {
				Long lessonId = p.getLesson().getId();
				lessonMap.put(lessonId, p.getLesson());
				completionCountByLesson.merge(lessonId, Boolean.TRUE.equals(p.getCompleted()) ? 1 : 0, Integer::sum);
				xpByLesson.merge(lessonId, p.getXpEarned() != null ? p.getXpEarned() : 0, Integer::sum);
			}
		}

		return completionCountByLesson.entrySet().stream()
				.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
				.limit(5)
				.map(e -> {
					Lesson l = lessonMap.get(e.getKey());
					return TopLessonResponse.builder()
							.lessonId(l != null ? l.getId() : e.getKey())
							.title(l != null ? l.getTitle() : "Unknown")
							.category(l != null ? l.getCategory() : "Unknown")
							.completions(e.getValue())
							.xpEarned(xpByLesson.getOrDefault(e.getKey(), 0))
							.build();
				})
				.toList();
	}

	@Override
	public SpeakingPerformanceResponse getSpeakingPerformance() {
		User user = getCurrentUser();
		List<SpeakingSession> sessions = speakingSessionRepository.findByUser(user);

		int totalSessions = sessions.size();
		int totalSeconds = sessions.stream().mapToInt(s -> s.getDuration() != null ? s.getDuration() : 0).sum();
		int totalMinutes = (int) Math.ceil(totalSeconds / 60.0);

		double avgScore = sessions.stream()
				.filter(s -> s.getOverallScore() != null)
				.mapToDouble(SpeakingSession::getOverallScore)
				.average()
				.orElse(0.0);

		double avgPronunciation = sessions.stream()
				.filter(s -> s.getPronunciationScore() != null)
				.mapToDouble(SpeakingSession::getPronunciationScore)
				.average()
				.orElse(0.0);

		double avgFluency = sessions.stream()
				.filter(s -> s.getFluencyScore() != null)
				.mapToDouble(SpeakingSession::getFluencyScore)
				.average()
				.orElse(0.0);

		double avgGrammar = sessions.stream()
				.filter(s -> s.getGrammarScore() != null)
				.mapToDouble(SpeakingSession::getGrammarScore)
				.average()
				.orElse(0.0);

		double avgVocabulary = sessions.stream()
				.filter(s -> s.getVocabularyScore() != null)
				.mapToDouble(SpeakingSession::getVocabularyScore)
				.average()
				.orElse(0.0);

		return SpeakingPerformanceResponse.builder()
				.averageScore(Math.round(avgScore * 10.0) / 10.0)
				.totalSessions(totalSessions)
				.totalMinutes(totalMinutes)
				.averagePronunciation(Math.round(avgPronunciation * 10.0) / 10.0)
				.averageFluency(Math.round(avgFluency * 10.0) / 10.0)
				.averageGrammar(Math.round(avgGrammar * 10.0) / 10.0)
				.averageVocabulary(Math.round(avgVocabulary * 10.0) / 10.0)
				.build();
	}
}
