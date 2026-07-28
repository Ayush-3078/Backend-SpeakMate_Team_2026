package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.response.ActivityResponse;
import com.rslsolution.speakmateai.dto.response.LeaderboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolProgressResponse;
import com.rslsolution.speakmateai.dto.response.SchoolDashboardResponse;
import com.rslsolution.speakmateai.dto.response.SkillScoresResponse;
import com.rslsolution.speakmateai.dto.response.StudentAttentionResponse;
import com.rslsolution.speakmateai.dto.response.TopStudentResponse;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.SchoolDashboardService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class SchoolDashboardServiceImpl implements SchoolDashboardService {

	private static final Logger logger = LoggerFactory.getLogger(SchoolDashboardServiceImpl.class);

	private final UserRepository userRepository;
	private final SchoolRepository schoolRepository;
	private final SpeakingSessionRepository speakingSessionRepository;
	private final LessonProgressRepository lessonProgressRepository;

	public SchoolDashboardServiceImpl(UserRepository userRepository, SchoolRepository schoolRepository,
			SpeakingSessionRepository speakingSessionRepository, LessonProgressRepository lessonProgressRepository) {
		this.userRepository = userRepository;
		this.schoolRepository = schoolRepository;
		this.speakingSessionRepository = speakingSessionRepository;
		this.lessonProgressRepository = lessonProgressRepository;
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
			throw new UserNotFoundException("User not authenticated");
		}
		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	@Override
	public SchoolDashboardResponse getSchoolDashboard() {
		logger.debug("[SchoolDashboard] Fetching dashboard for authenticated user");
		User currentUser = getCurrentUser();

		if (currentUser.getSchool() == null) {
			logger.debug("[SchoolDashboard] User has no school associated, returning empty dashboard");
			return SchoolDashboardResponse.builder().totalStudents(0L).activeStudents(0L).inactiveStudents(0L).totalTeachers(0L).totalClasses(0).practiceToday(0L).practiceThisWeek(0L).lessonsCompleted(0L).averageFluency(0.0).averageGrammarAccuracy(0.0).averageVocabularyScore(0.0).topStudents(List.of()).studentsNeedingAttention(List.of()).build();
		}

		Long schoolId = currentUser.getSchool().getId();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
		LocalDateTime startOfWeek = now.toLocalDate().minusDays(now.getDayOfWeek().getValue() - 1).atStartOfDay();

		long totalStudents = userRepository.countBySchoolIdAndUserType(schoolId, "Student");
		long activeStudents = userRepository.countBySchoolIdAndUserTypeAndActiveTrue(schoolId, "Student");
		long inactiveStudents = totalStudents - activeStudents;
		long totalTeachers = userRepository.countBySchoolIdAndUserType(schoolId, "Teacher");

		long practiceToday = speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(schoolId, startOfDay, now);
		long practiceThisWeek = speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(schoolId, startOfWeek, now);

		long lessonsCompleted = lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(schoolId);

		Double avgFluency = speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(schoolId);
		Double avgGrammar = speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(schoolId);
		Double avgVocabulary = speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(schoolId);

		if (avgFluency == null) avgFluency = 0.0;
		if (avgGrammar == null) avgGrammar = 0.0;
		if (avgVocabulary == null) avgVocabulary = 0.0;

		List<Object[]> topStudentsRaw = speakingSessionRepository.findTopStudentsBySchoolId(schoolId, PageRequest.of(0, 5));
		List<TopStudentResponse> topStudents = topStudentsRaw.stream()
				.map(row -> TopStudentResponse.builder()
						.userId((Long) row[0])
						.firstName((String) row[1])
						.lastName((String) row[2])
						.email((String) row[3])
						.averageScore(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0)
						.totalSessions(((Number) row[5]).intValue())
						.build())
				.collect(Collectors.toList());

		List<Object[]> attentionRaw = speakingSessionRepository.findStudentsNeedingAttentionBySchoolId(schoolId, PageRequest.of(0, 10));
		List<StudentAttentionResponse> studentsNeedingAttention = attentionRaw.stream()
				.map(row -> StudentAttentionResponse.builder()
						.userId((Long) row[0])
						.firstName((String) row[1])
						.lastName((String) row[2])
						.email((String) row[3])
						.reason("Low performance")
						.build())
				.collect(Collectors.toList());

		return SchoolDashboardResponse.builder()
				.totalStudents(totalStudents)
				.activeStudents(activeStudents)
				.inactiveStudents(inactiveStudents)
				.totalTeachers(totalTeachers)
				.totalClasses(0)
				.practiceToday(practiceToday)
				.practiceThisWeek(practiceThisWeek)
				.lessonsCompleted(lessonsCompleted)
				.averageFluency(avgFluency)
				.averageGrammarAccuracy(avgGrammar)
				.averageVocabularyScore(avgVocabulary)
				.topStudents(topStudents)
				.studentsNeedingAttention(studentsNeedingAttention)
				.build();
	}

	@Override
	public ActivityResponse getActivity() {
		logger.debug("[SchoolDashboard] Fetching activity for authenticated user");
		User currentUser = getCurrentUser();

		if (currentUser.getSchool() == null) {
			return ActivityResponse.builder().labels(List.of()).dailyPractice(List.of()).weeklyPractice(List.of()).build();
		}

		Long schoolId = currentUser.getSchool().getId();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
		LocalDateTime startOfWeek = now.toLocalDate().minusDays(now.getDayOfWeek().getValue() - 1).atStartOfDay();

		long practiceToday = speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(schoolId, startOfDay, now);
		long practiceThisWeek = speakingSessionRepository.countByUserSchoolIdAndCreatedAtBetween(schoolId, startOfWeek, now);

		return ActivityResponse.builder()
				.labels(List.of("Today", "This Week"))
				.dailyPractice(List.of((int) practiceToday))
				.weeklyPractice(List.of((int) practiceThisWeek))
				.build();
	}

	@Override
	public SchoolProgressResponse getProgress() {
		logger.debug("[SchoolDashboard] Fetching progress for authenticated user");
		User currentUser = getCurrentUser();

		if (currentUser.getSchool() == null) {
			return SchoolProgressResponse.builder().totalLessons(0L).completedLessons(0L).inProgressLessons(0L).recentProgress(List.of()).build();
		}

		Long schoolId = currentUser.getSchool().getId();

		long totalLessons = lessonProgressRepository.countByUserSchoolId(schoolId);
		long completedLessons = lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(schoolId);
		long inProgressLessons = totalLessons - completedLessons;

		return SchoolProgressResponse.builder()
				.totalLessons(totalLessons)
				.completedLessons(completedLessons)
				.inProgressLessons(inProgressLessons)
				.recentProgress(List.of())
				.build();
	}

	@Override
	public SkillScoresResponse getSkillScores() {
		logger.debug("[SchoolDashboard] Fetching skill scores for authenticated user");
		User currentUser = getCurrentUser();

		if (currentUser.getSchool() == null) {
			return SkillScoresResponse.builder().averageFluency(0.0).averageGrammarAccuracy(0.0).averageVocabularyScore(0.0).overallAverage(0.0).build();
		}

		Long schoolId = currentUser.getSchool().getId();

		Double avgFluency = speakingSessionRepository.findAverageFluencyScoreByUserSchoolId(schoolId);
		Double avgGrammar = speakingSessionRepository.findAverageGrammarScoreByUserSchoolId(schoolId);
		Double avgVocabulary = speakingSessionRepository.findAverageVocabularyScoreByUserSchoolId(schoolId);

		if (avgFluency == null) avgFluency = 0.0;
		if (avgGrammar == null) avgGrammar = 0.0;
		if (avgVocabulary == null) avgVocabulary = 0.0;

		double overall = (avgFluency + avgGrammar + avgVocabulary) / 3.0;

		return SkillScoresResponse.builder()
				.averageFluency(avgFluency)
				.averageGrammarAccuracy(avgGrammar)
				.averageVocabularyScore(avgVocabulary)
				.overallAverage(overall)
				.build();
	}

	@Override
	public LeaderboardResponse getLeaderboard() {
		logger.debug("[SchoolDashboard] Fetching leaderboard for authenticated user");
		User currentUser = getCurrentUser();

		if (currentUser.getSchool() == null) {
			return LeaderboardResponse.builder().entries(List.of()).build();
		}

		Long schoolId = currentUser.getSchool().getId();

		List<Object[]> topStudentsRaw = speakingSessionRepository.findTopStudentsBySchoolId(schoolId, PageRequest.of(0, 10));
		List<LeaderboardResponse.LeaderboardEntry> entries = topStudentsRaw.stream()
				.map(row -> LeaderboardResponse.LeaderboardEntry.builder()
						.userId((Long) row[0])
						.firstName((String) row[1])
						.lastName((String) row[2])
						.email((String) row[3])
						.averageScore(row[4] != null ? ((Number) row[4]).doubleValue() : 0.0)
						.totalSessions(((Number) row[5]).intValue())
						.build())
				.collect(Collectors.toList());

		for (int i = 0; i < entries.size(); i++) {
			entries.get(i).setRank(i + 1);
		}

		return LeaderboardResponse.builder().entries(entries).build();
	}
}
