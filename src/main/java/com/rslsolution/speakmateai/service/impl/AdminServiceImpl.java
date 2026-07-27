package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.response.AdminAiUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminDashboardResponse;
import com.rslsolution.speakmateai.dto.response.AdminSchoolGrowthResponse;
import com.rslsolution.speakmateai.dto.response.AdminUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminUserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.SuperAdminDashboardResponse;
import com.rslsolution.speakmateai.dto.response.UserResponse;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.AchievementRepository;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.repository.ChatSessionRepository;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.LessonRepository;
import com.rslsolution.speakmateai.repository.NotificationRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.service.AdminService;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final LessonRepository lessonRepository;
	private final SpeakingSessionRepository speakingSessionRepository;
	private final VocabularyRepository vocabularyRepository;
	private final AchievementRepository achievementRepository;
	private final NotificationRepository notificationRepository;
	private final LessonProgressRepository lessonProgressRepository;
	private final ChatSessionRepository chatSessionRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final GrammarHistoryRepository grammarHistoryRepository;

	public AdminServiceImpl(UserRepository userRepository, LessonRepository lessonRepository,
			SpeakingSessionRepository speakingSessionRepository, VocabularyRepository vocabularyRepository,
			AchievementRepository achievementRepository, NotificationRepository notificationRepository,
			LessonProgressRepository lessonProgressRepository, ChatSessionRepository chatSessionRepository,
			ChatMessageRepository chatMessageRepository, GrammarHistoryRepository grammarHistoryRepository) {

		this.userRepository = userRepository;
		this.lessonRepository = lessonRepository;
		this.speakingSessionRepository = speakingSessionRepository;
		this.vocabularyRepository = vocabularyRepository;
		this.achievementRepository = achievementRepository;
		this.notificationRepository = notificationRepository;
		this.lessonProgressRepository = lessonProgressRepository;
		this.chatSessionRepository = chatSessionRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.grammarHistoryRepository = grammarHistoryRepository;
	}

	@Override
	public AdminDashboardResponse getDashboard() {

		return AdminDashboardResponse.builder().totalUsers(userRepository.count())
				.activeUsers(userRepository.countByActiveTrue()).totalLessons(lessonRepository.count())
				.activeLessons(lessonRepository.findByActiveTrue().stream().count())
				.totalSpeakingSessions(speakingSessionRepository.count())
				.totalVocabularyWords(vocabularyRepository.count()).totalAchievements(achievementRepository.count())
				.totalNotifications(notificationRepository.count()).build();
	}

	@Override
	public SuperAdminDashboardResponse getSuperAdminDashboard() {
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
		LocalDateTime startOfMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
		LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

		// School Statistics
		Long totalSchools = userRepository.countByUserType("School");
		Long activeSchools = userRepository.countByUserTypeAndActiveTrue("School");
		Long inactiveSchools = userRepository.countByUserTypeAndActiveFalse("School");

		// Student Statistics
		Long totalStudents = userRepository.countByUserType("Student");
		Long activeStudents = userRepository.countByUserTypeAndActiveTrue("Student");
		Long individualUsers = userRepository.countByUserType("Individual");

		// Platform Statistics
		Long dailyActiveUsers = userRepository.countByUpdatedAtBetween(startOfDay, now);
		Long monthlyActiveUsers = userRepository.countByUpdatedAtBetween(startOfMonth, endOfMonth);
		Long aiConversations = chatSessionRepository.count();
		Long speakingSessions = speakingSessionRepository.count();
		Integer totalDurationSeconds = speakingSessionRepository.sumDuration();
		Long practiceMinutes = totalDurationSeconds != null
				? Math.round(totalDurationSeconds / 60.0f)
				: 0L;

		// Learning Statistics
		Long lessonsCompleted = lessonProgressRepository.countByCompletedTrue();
		Double avgFluency = speakingSessionRepository.findAverageFluencyScore();
		Double avgGrammar = speakingSessionRepository.findAverageGrammarScore();

		if (avgFluency == null) avgFluency = 0.0;
		if (avgGrammar == null) avgGrammar = 0.0;

		// AI Statistics
		Long chatMessages = chatMessageRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
		Long grammarChecks = grammarHistoryRepository.countByCreatedAtBetween(startOfMonth, endOfMonth);
		Long groqApiUsage = aiConversations + grammarChecks + speakingSessions;
		Long whisperUsage = speakingSessions;
		Long estimatedAiCost = 0L;

		// Growth Statistics
		Long newSchoolsThisMonth = userRepository.countByUserTypeAndCreatedAtAfter("School", startOfMonth);
		Long newStudentsThisMonth = userRepository.countByUserTypeAndCreatedAtAfter("Student", startOfMonth);

		return SuperAdminDashboardResponse.builder()
				.totalSchools(totalSchools)
				.activeSchools(activeSchools)
				.inactiveSchools(inactiveSchools)
				.totalStudents(totalStudents)
				.activeStudents(activeStudents)
				.individualUsers(individualUsers)
				.dailyActiveUsers(dailyActiveUsers)
				.monthlyActiveUsers(monthlyActiveUsers)
				.aiConversations(aiConversations)
				.speakingSessions(speakingSessions)
				.practiceMinutes(practiceMinutes)
				.lessonsCompleted(lessonsCompleted)
				.averageFluencyScore(avgFluency)
				.averageGrammarScore(avgGrammar)
				.groqApiUsage(groqApiUsage)
				.whisperUsage(whisperUsage)
				.estimatedAiCost(estimatedAiCost)
				.newSchoolsThisMonth(newSchoolsThisMonth)
				.newStudentsThisMonth(newStudentsThisMonth)
				.build();
	}

	@Override
	public AdminUserGrowthResponse getUserGrowth() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(30);

		List<Object[]> newUsersRaw = userRepository.countByCreatedAtDateBetween(start, end);
		List<Object[]> activeUsersRaw = userRepository.countActiveByUpdatedAtDateBetween(start, end);

		Map<LocalDate, Long> newUsersMap = toDateMap(newUsersRaw);
		Map<LocalDate, Long> activeUsersMap = toDateMap(activeUsersRaw);

		List<String> labels = IntStream.rangeClosed(0, 30)
				.mapToObj(i -> end.minusDays(30 - i).toLocalDate().toString())
				.toList();

		List<Integer> newUsersList = labels.stream()
				.map(label -> newUsersMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> activeUsersList = labels.stream()
				.map(label -> activeUsersMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> totalUsersList = new ArrayList<>();
		int sum = 0;
		for (Integer val : newUsersList) {
			sum += val;
			totalUsersList.add(sum);
		}

		return AdminUserGrowthResponse.builder()
				.labels(labels)
				.totalUsers(totalUsersList)
				.activeUsers(activeUsersList)
				.newUsers(newUsersList)
				.build();
	}

	@Override
	public AdminSchoolGrowthResponse getSchoolGrowth() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(30);

		List<Object[]> totalSchoolsRaw = userRepository.countByUserTypeAndCreatedAtDateBetween("School", start, end);
		List<Object[]> activeSchoolsRaw = userRepository.countActiveByUserTypeAndCreatedAtDateBetween("School", start, end);

		Map<LocalDate, Long> totalSchoolsMap = toDateMap(totalSchoolsRaw);
		Map<LocalDate, Long> activeSchoolsMap = toDateMap(activeSchoolsRaw);

		List<String> labels = IntStream.rangeClosed(0, 30)
				.mapToObj(i -> end.minusDays(30 - i).toLocalDate().toString())
				.toList();

		List<Integer> totalSchoolsList = labels.stream()
				.map(label -> totalSchoolsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> activeSchoolsList = labels.stream()
				.map(label -> activeSchoolsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		return AdminSchoolGrowthResponse.builder()
				.labels(labels)
				.totalSchools(totalSchoolsList)
				.activeSchools(activeSchoolsList)
				.build();
	}

	@Override
	public AdminUsageResponse getUsage() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(30);

		List<Object[]> activeUsersRaw = userRepository.countActiveByUpdatedAtDateBetween(start, end);
		List<Object[]> sessionsRaw = speakingSessionRepository.countByCreatedAtDateBetween(start, end);
		List<Object[]> lessonsRaw = lessonProgressRepository.countCompletedByDateBetween(start, end);
		List<Object[]> durationRaw = speakingSessionRepository.sumDurationByDateBetween(start, end);

		Map<LocalDate, Long> activeUsersMap = toDateMap(activeUsersRaw);
		Map<LocalDate, Long> sessionsMap = toDateMap(sessionsRaw);
		Map<LocalDate, Long> lessonsMap = toDateMap(lessonsRaw);
		Map<LocalDate, Long> durationMap = toDateMap(durationRaw);

		List<String> labels = IntStream.rangeClosed(0, 30)
				.mapToObj(i -> end.minusDays(30 - i).toLocalDate().toString())
				.toList();

		List<Integer> dailyActiveUsers = labels.stream()
				.map(label -> activeUsersMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> speakingSessions = labels.stream()
				.map(label -> sessionsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> lessonsCompleted = labels.stream()
				.map(label -> lessonsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> practiceMinutes = labels.stream()
				.map(label -> durationMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		return AdminUsageResponse.builder()
				.labels(labels)
				.dailyActiveUsers(dailyActiveUsers)
				.speakingSessions(speakingSessions)
				.lessonsCompleted(lessonsCompleted)
				.practiceMinutes(practiceMinutes)
				.build();
	}

	@Override
	public AdminAiUsageResponse getAiUsage() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(30);

		List<Object[]> chatMessagesRaw = chatMessageRepository.countByCreatedAtDateBetween(start, end);
		List<Object[]> speakingSessionsRaw = speakingSessionRepository.countByCreatedAtDateBetween(start, end);
		List<Object[]> grammarChecksRaw = grammarHistoryRepository.countByCreatedAtDateBetween(start, end);
		List<Object[]> vocabularyActionsRaw = vocabularyRepository.countByCreatedAtDateBetween(start, end);

		Map<LocalDate, Long> chatMessagesMap = toDateMap(chatMessagesRaw);
		Map<LocalDate, Long> speakingSessionsMap = toDateMap(speakingSessionsRaw);
		Map<LocalDate, Long> grammarChecksMap = toDateMap(grammarChecksRaw);
		Map<LocalDate, Long> vocabularyActionsMap = toDateMap(vocabularyActionsRaw);

		List<String> labels = IntStream.rangeClosed(0, 30)
				.mapToObj(i -> end.minusDays(30 - i).toLocalDate().toString())
				.toList();

		List<Integer> chatMessages = labels.stream()
				.map(label -> chatMessagesMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> speakingSessions = labels.stream()
				.map(label -> speakingSessionsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> grammarChecks = labels.stream()
				.map(label -> grammarChecksMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		List<Integer> vocabularyActions = labels.stream()
				.map(label -> vocabularyActionsMap.getOrDefault(LocalDate.parse(label), 0L).intValue())
				.toList();

		return AdminAiUsageResponse.builder()
				.labels(labels)
				.chatMessages(chatMessages)
				.speakingSessions(speakingSessions)
				.grammarChecks(grammarChecks)
				.vocabularyActions(vocabularyActions)
				.build();
	}

	private Map<LocalDate, Long> toDateMap(List<Object[]> rows) {
		if (rows == null || rows.isEmpty()) {
			return new HashMap<>();
		}
		return rows.stream()
				.collect(Collectors.toMap(
						row -> ((java.sql.Date) row[0]).toLocalDate(),
						row -> (Long) row[1]
				));
	}

	@Override
	public List<UserResponse> getAllUsers() {

		return userRepository.findAll().stream().map(this::mapToUserResponse).toList();
	}

	@Override
	public UserResponse getUserById(Long id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

		return mapToUserResponse(user);
	}

	@Override
	public UserResponse activateUser(Long id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

		user.setActive(true);

		User updatedUser = userRepository.save(user);

		return mapToUserResponse(updatedUser);
	}

	@Override
	public UserResponse deactivateUser(Long id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

		user.setActive(false);

		User updatedUser = userRepository.save(user);

		return mapToUserResponse(updatedUser);
	}

	private UserResponse mapToUserResponse(User user) {

		return UserResponse.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
				.email(user.getEmail()).role(user.getRole()).avatar(user.getAvatar()).active(user.isActive())
				.createdAt(user.getCreatedAt()).build();
	}
}
