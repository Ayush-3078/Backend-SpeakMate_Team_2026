package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.response.AchievementResponse;
import com.rslsolution.speakmateai.dto.response.AiLearningInsightResponse;
import com.rslsolution.speakmateai.dto.response.AssignedClassResponse;
import com.rslsolution.speakmateai.dto.response.ClassPerformanceResponse;
import com.rslsolution.speakmateai.dto.response.ClassRoomResponse;
import com.rslsolution.speakmateai.dto.response.PerformanceSummaryResponse;
import com.rslsolution.speakmateai.dto.response.PerformanceTrendResponse;
import com.rslsolution.speakmateai.dto.response.PracticeStatisticsResponse;
import com.rslsolution.speakmateai.dto.response.ProfileResponse;
import com.rslsolution.speakmateai.dto.response.ProgressResponse;
import com.rslsolution.speakmateai.dto.response.RecentActivityResponse;
import com.rslsolution.speakmateai.dto.response.ReportCategoryResponse;
import com.rslsolution.speakmateai.dto.response.RecentReportResponse;
import com.rslsolution.speakmateai.dto.response.ReportStatusResponse;
import com.rslsolution.speakmateai.dto.response.SkillPerformanceSummaryResponse;
import com.rslsolution.speakmateai.dto.response.StrengthImprovementResponse;
import com.rslsolution.speakmateai.dto.response.StudentAttentionItemResponse;
import com.rslsolution.speakmateai.dto.response.TeacherAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherDashboardResponse;
import com.rslsolution.speakmateai.dto.response.TeacherProfileResponse;
import com.rslsolution.speakmateai.dto.response.TeacherReportsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentDetailResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentSummaryResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentsListResponse;
import com.rslsolution.speakmateai.dto.response.TopPerformerResponse;
import com.rslsolution.speakmateai.dto.response.UpcomingReportResponse;
import com.rslsolution.speakmateai.dto.response.WeeklyProgressResponse;
import com.rslsolution.speakmateai.dto.response.StatisticsResponse;
import com.rslsolution.speakmateai.dto.response.AcademicSessionResponse;
import com.rslsolution.speakmateai.dto.response.IdentityResponse;
import com.rslsolution.speakmateai.dto.response.ProfessionalInfoResponse;
import com.rslsolution.speakmateai.dto.response.TeachingOverviewResponse;
import com.rslsolution.speakmateai.dto.response.ContactInfoResponse;
import com.rslsolution.speakmateai.dto.response.AccountInfoResponse;
import com.rslsolution.speakmateai.dto.response.UserPreferencesResponse;
import com.rslsolution.speakmateai.entity.Achievement;
import com.rslsolution.speakmateai.entity.Admin;
import com.rslsolution.speakmateai.entity.ClassRoom;
import com.rslsolution.speakmateai.entity.ClassStudent;
import com.rslsolution.speakmateai.entity.GrammarHistory;
import com.rslsolution.speakmateai.entity.LessonProgress;
import com.rslsolution.speakmateai.entity.Progress;
import com.rslsolution.speakmateai.entity.SpeakingSession;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.entity.Vocabulary;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.AchievementRepository;
import com.rslsolution.speakmateai.repository.AdminRepository;
import com.rslsolution.speakmateai.repository.ClassRoomRepository;
import com.rslsolution.speakmateai.repository.ClassStudentRepository;
import com.rslsolution.speakmateai.repository.GrammarHistoryRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.ProgressRepository;
import com.rslsolution.speakmateai.repository.SettingsRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.repository.VocabularyRepository;
import com.rslsolution.speakmateai.service.TeacherService;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final ClassRoomRepository classRoomRepository;
	private final ClassStudentRepository classStudentRepository;
	private final ProgressRepository progressRepository;
	private final SpeakingSessionRepository speakingSessionRepository;
	private final GrammarHistoryRepository grammarHistoryRepository;
	private final VocabularyRepository vocabularyRepository;
	private final LessonProgressRepository lessonProgressRepository;
	private final AchievementRepository achievementRepository;
	private final SettingsRepository settingsRepository;

	public TeacherServiceImpl(UserRepository userRepository, AdminRepository adminRepository,
			ClassRoomRepository classRoomRepository, ClassStudentRepository classStudentRepository,
			ProgressRepository progressRepository, SpeakingSessionRepository speakingSessionRepository,
			GrammarHistoryRepository grammarHistoryRepository, VocabularyRepository vocabularyRepository,
			LessonProgressRepository lessonProgressRepository, AchievementRepository achievementRepository,
			SettingsRepository settingsRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.classRoomRepository = classRoomRepository;
		this.classStudentRepository = classStudentRepository;
		this.progressRepository = progressRepository;
		this.speakingSessionRepository = speakingSessionRepository;
		this.grammarHistoryRepository = grammarHistoryRepository;
		this.vocabularyRepository = vocabularyRepository;
		this.lessonProgressRepository = lessonProgressRepository;
		this.achievementRepository = achievementRepository;
		this.settingsRepository = settingsRepository;
	}

	private User getCurrentTeacher() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElse(null);
		if (user != null) {
			if (user.getRole() != Role.TEACHER) {
				throw new RuntimeException("Unauthorized: Teacher access required");
			}
			return user;
		}

		Admin admin = adminRepository.findByEmail(email).orElse(null);
		if (admin != null && admin.getRole() == Role.SUPER_ADMIN) {
			throw new RuntimeException("Unauthorized: Teacher access required");
		}

		throw new UserNotFoundException("Teacher not found");
	}

	private List<ClassRoom> getTeacherClasses(Long teacherId) {
		return classRoomRepository.findByTeacherId(teacherId);
	}

	private List<User> getStudentsInClasses(List<ClassRoom> classes) {
		if (classes == null || classes.isEmpty()) {
			return new ArrayList<>();
		}
		List<Long> classIds = classes.stream().map(ClassRoom::getId).collect(Collectors.toList());
		List<ClassStudent> classStudents = classStudentRepository.findByClassIdIn(classIds);
		List<Long> studentIds = classStudents.stream().map(ClassStudent::getStudentId).distinct()
				.collect(Collectors.toList());
		if (studentIds.isEmpty()) {
			return new ArrayList<>();
		}
		return userRepository.findAllById(studentIds).stream()
				.filter(u -> u.getRole() == Role.STUDENT).collect(Collectors.toList());
	}

	private List<User> getStudentsInClassesFiltered(List<ClassRoom> classes, String search, Status status) {
		List<User> students = getStudentsInClasses(classes);

		if (status != null) {
			students = students.stream().filter(s -> s.getStatus() == status).collect(Collectors.toList());
		}

		if (search != null && !search.trim().isEmpty()) {
			String lowerSearch = search.toLowerCase();
			students = students.stream()
					.filter(s -> (s.getFirstName() != null && s.getFirstName().toLowerCase().contains(lowerSearch))
							|| (s.getLastName() != null && s.getLastName().toLowerCase().contains(lowerSearch))
							|| (s.getEmail() != null && s.getEmail().toLowerCase().contains(lowerSearch))
							|| (s.getRollNumber() != null && s.getRollNumber().toLowerCase().contains(lowerSearch)))
					.collect(Collectors.toList());
		}

		return students;
	}

	private ProfileResponse buildProfileResponse(User user, Progress progress) {
		int xp = (progress != null && progress.getXp() != null) ? progress.getXp() : 0;
		return ProfileResponse.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
				.email(user.getEmail()).role(user.getRole().name()).avatar(user.getAvatar())
				.englishLevel(user.getEnglishLevel()).learningGoal(user.getLearningGoal()).xp(xp)
				.level((xp / 500) + 1)
				.currentStreak(progress != null ? progress.getCurrentStreak() : 0)
				.longestStreak(progress != null ? progress.getLongestStreak() : 0)
				.totalPracticeMinutes(progress != null ? progress.getTotalPracticeMinutes() : 0)
				.totalSpeakingSessions(progress != null ? progress.getTotalSpeakingSessions() : 0)
				.totalGrammarChecks(progress != null ? progress.getTotalGrammarChecks() : 0)
				.totalVocabularyWords(progress != null ? progress.getTotalVocabularyWords() : 0).build();
	}

	private Double getAverageGrammarScore(User user) {
		return grammarHistoryRepository.findAverageGrammarScoreByUserId(user.getId());
	}

	private Double getAverageSpeakingScore(User user) {
		return speakingSessionRepository.findAverageOverallScoreByUserId(user.getId());
	}

	private Double getAverageListeningScore(User user) {
		Double pronunciation = speakingSessionRepository.findAveragePronunciationScoreByUserId(user.getId());
		Double fluency = speakingSessionRepository.findAverageFluencyScoreByUserId(user.getId());
		Double grammar = speakingSessionRepository.findAverageGrammarScoreByUserId(user.getId());
		Double vocabulary = speakingSessionRepository.findAverageVocabularyScoreByUserId(user.getId());
		double sum = 0;
		int count = 0;
		if (pronunciation != null) {
			sum += pronunciation;
			count++;
		}
		if (fluency != null) {
			sum += fluency;
			count++;
		}
		if (grammar != null) {
			sum += grammar;
			count++;
		}
		if (vocabulary != null) {
			sum += vocabulary;
			count++;
		}
		return count > 0 ? sum / count : null;
	}

	private List<WeeklyProgressResponse> getWeeklyProgressForUser(User user) {
		String[] dayNames = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		List<WeeklyProgressResponse> weeklyProgress = new ArrayList<>();
		for (String dayName : dayNames) {
			weeklyProgress.add(WeeklyProgressResponse.builder().day(dayName).studyMinutes(0).lessonsCompleted(0)
					.speakingSessions(0).build());
		}

		LocalDate today = LocalDate.now();
		LocalDateTime weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
				.atStartOfDay();
		LocalDateTime weekEnd = weekStart.plusDays(7);

		List<SpeakingSession> sessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(user.getId(), weekStart,
				weekEnd);
		int[] studySeconds = new int[7];
		int[] speakingSessions = new int[7];
		int[] lessonsCompleted = new int[7];

		for (SpeakingSession s : sessions) {
			if (s.getCreatedAt() != null) {
				LocalDate date = s.getCreatedAt().toLocalDate();
			if (!date.isBefore(weekStart.toLocalDate()) && !date.isAfter(weekStart.toLocalDate().plusDays(6))) {
					int dayOfWeekIndex = date.getDayOfWeek().getValue() - 1;
					studySeconds[dayOfWeekIndex] += s.getDuration() != null ? s.getDuration() : 0;
					speakingSessions[dayOfWeekIndex]++;
					lessonsCompleted[dayOfWeekIndex] = 1;
				}
			}
		}

		List<LessonProgress> completedLessons = lessonProgressRepository.findByUserIdAndCompletedAtBetween(user.getId(),
				weekStart, weekEnd);
		for (LessonProgress lp : completedLessons) {
			if (lp.getCompletedAt() != null) {
				LocalDate date = lp.getCompletedAt().toLocalDate();
				if (!date.isBefore(weekStart.toLocalDate()) && !date.isAfter(weekStart.toLocalDate().plusDays(6))) {
					int dayOfWeekIndex = date.getDayOfWeek().getValue() - 1;
					lessonsCompleted[dayOfWeekIndex]++;
				}
			}
		}

		for (int i = 0; i < 7; i++) {
			WeeklyProgressResponse dayRes = weeklyProgress.get(i);
			dayRes.setStudyMinutes((int) Math.ceil(studySeconds[i] / 60.0));
			dayRes.setSpeakingSessions(speakingSessions[i]);
			dayRes.setLessonsCompleted(lessonsCompleted[i]);
		}

		return weeklyProgress;
	}

	private List<WeeklyProgressResponse> getAggregatedWeeklyProgress(List<User> students) {
		String[] dayNames = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
		List<WeeklyProgressResponse> weeklyProgress = new ArrayList<>();
		for (String dayName : dayNames) {
			weeklyProgress.add(WeeklyProgressResponse.builder().day(dayName).studyMinutes(0).lessonsCompleted(0)
					.speakingSessions(0).build());
		}

		LocalDate today = LocalDate.now();
		LocalDateTime weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
				.atStartOfDay();
		LocalDateTime weekEnd = weekStart.plusDays(7);

		int[] studySeconds = new int[7];
		int[] speakingSessions = new int[7];
		int[] lessonsCompleted = new int[7];

		for (User student : students) {
			List<SpeakingSession> sessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(student.getId(),
					weekStart, weekEnd);
			for (SpeakingSession s : sessions) {
				if (s.getCreatedAt() != null) {
					LocalDate date = s.getCreatedAt().toLocalDate();
					if (!date.isBefore(weekStart.toLocalDate()) && !date.isBefore(today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))) && !date
							.isAfter(weekStart.toLocalDate().plusDays(6))) {
						int dayOfWeekIndex = date.getDayOfWeek().getValue() - 1;
						studySeconds[dayOfWeekIndex] += s.getDuration() != null ? s.getDuration() : 0;
						speakingSessions[dayOfWeekIndex]++;
						lessonsCompleted[dayOfWeekIndex] = 1;
					}
				}
			}

			List<LessonProgress> completedLessons = lessonProgressRepository.findByUserIdAndCompletedAtBetween(student.getId(),
					weekStart, weekEnd);
			for (LessonProgress lp : completedLessons) {
				if (lp.getCompletedAt() != null) {
					LocalDate date = lp.getCompletedAt().toLocalDate();
					if (!date.isBefore(weekStart.toLocalDate()) && !date.isAfter(weekStart.toLocalDate().plusDays(6))) {
						int dayOfWeekIndex = date.getDayOfWeek().getValue() - 1;
						lessonsCompleted[dayOfWeekIndex]++;
					}
				}
			}
		}

		for (int i = 0; i < 7; i++) {
			WeeklyProgressResponse dayRes = weeklyProgress.get(i);
			dayRes.setStudyMinutes((int) Math.ceil(studySeconds[i] / 60.0));
			dayRes.setSpeakingSessions(speakingSessions[i]);
			dayRes.setLessonsCompleted(lessonsCompleted[i]);
		}

		return weeklyProgress;
	}

	private List<RecentActivityResponse> getRecentActivityForStudents(List<User> students) {
		List<RecentActivityResponse> activities = new ArrayList<>();
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

		for (User student : students) {
			List<SpeakingSession> sessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(student.getId(),
					oneWeekAgo, LocalDateTime.now());
			for (SpeakingSession s : sessions) {
				activities.add(RecentActivityResponse.builder().id("speaking-" + s.getId()).type("speaking").icon("mic")
						.title(s.getTopic() != null ? "Speaking Session: " + s.getTopic() : "Speaking Session")
						.time(s.getCreatedAt()).xp(15).build());
			}

			List<Vocabulary> vocabs = vocabularyRepository.findByUserOrderByCreatedAtDesc(student);
			for (Vocabulary v : vocabs) {
				if (v.getCreatedAt() != null && !v.getCreatedAt().isBefore(oneWeekAgo)) {
					activities.add(RecentActivityResponse.builder().id("vocabulary-" + v.getId()).type("vocabulary")
							.icon("library")
							.title(v.getWord() != null ? "Vocabulary Practice: " + v.getWord() : "Vocabulary Practice")
							.time(v.getCreatedAt()).xp(8).build());
				}
			}

			List<GrammarHistory> grammars = grammarHistoryRepository.findByUserIdAndCreatedAtBetween(student.getId(),
					oneWeekAgo, LocalDateTime.now());
			for (GrammarHistory g : grammars) {
				activities.add(RecentActivityResponse.builder().id("grammar-" + g.getId()).type("grammar").icon("text")
						.title("Grammar Practice").time(g.getCreatedAt()).xp(10).build());
			}
		}

		activities.sort((a, b) -> b.getTime().compareTo(a.getTime()));
		if (activities.size() > 10) {
			return activities.subList(0, 10);
		}
		return activities;
	}

	@Override
	public TeacherDashboardResponse getTeacherDashboard() {
		User teacher = getCurrentTeacher();
		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<User> students = getStudentsInClasses(classes);

		ProfileResponse profile = ProfileResponse.builder().id(teacher.getId()).firstName(teacher.getFirstName())
				.lastName(teacher.getLastName()).email(teacher.getEmail()).role(teacher.getRole().name())
				.avatar(teacher.getAvatar()).englishLevel(teacher.getEnglishLevel()).learningGoal(teacher.getLearningGoal())
				.build();

		List<AssignedClassResponse> assignedClasses = classes.stream().map(c -> {
			List<ClassStudent> classStudents = classStudentRepository.findByClassId(c.getId());
			return AssignedClassResponse.builder().id(c.getId()).name(c.getName()).grade(c.getGrade())
					.academicYear(c.getAcademicYear()).status(c.getStatus())
					.studentCount(classStudents.size()).build();
		}).collect(Collectors.toList());

		int totalStudents = students.size();

		double avgProgress = 0;
		if (!students.isEmpty()) {
			double totalXp = students.stream().mapToDouble(s -> {
				Progress p = progressRepository.findByUser(s).orElse(null);
				return p != null && p.getXp() != null ? p.getXp() : 0;
			}).sum();
			avgProgress = totalXp / students.size();
		}

		List<WeeklyProgressResponse> weeklyCompletion = getAggregatedWeeklyProgress(students);

		long completedStudentsCount = students.stream()
				.filter(s -> lessonProgressRepository.countByUserIdAndCompletedTrue(s.getId()) > 0).count();

		double avgGrammar = 0;
		double avgVocabulary = 0;
		double avgSpeaking = 0;
		double avgListening = 0;
		int skillCount = 0;

		List<Double> grammarScores = new ArrayList<>();
		List<Double> vocabularyScores = new ArrayList<>();
		List<Double> speakingScores = new ArrayList<>();
		List<Double> listeningScores = new ArrayList<>();

		for (User student : students) {
			Double g = getAverageGrammarScore(student);
			if (g != null)
				grammarScores.add(g);
			Double v = (double) (progressRepository.findByUser(student).map(Progress::getTotalVocabularyWords).orElse(0));
			if (v > 0)
				vocabularyScores.add(v);
			Double sp = getAverageSpeakingScore(student);
			if (sp != null)
				speakingScores.add(sp);
			Double l = getAverageListeningScore(student);
			if (l != null)
				listeningScores.add(l);
		}

		if (!grammarScores.isEmpty()) {
			avgGrammar = grammarScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
			skillCount++;
		}
		if (!vocabularyScores.isEmpty()) {
			avgVocabulary = vocabularyScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
			skillCount++;
		}
		if (!speakingScores.isEmpty()) {
			avgSpeaking = speakingScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
			skillCount++;
		}
		if (!listeningScores.isEmpty()) {
			avgListening = listeningScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
			skillCount++;
		}

		SkillPerformanceSummaryResponse skillPerformance = SkillPerformanceSummaryResponse.builder()
				.grammar(avgGrammar).vocabulary(avgVocabulary).speaking(avgSpeaking).listening(avgListening).build();

		List<StudentAttentionItemResponse> attentionStudents = new ArrayList<>();
		for (User student : students) {
			List<String> reasons = new ArrayList<>();
			String severity = "low";

			Progress p = progressRepository.findByUser(student).orElse(null);
			int xp = p != null && p.getXp() != null ? p.getXp() : 0;

			if (xp < 50) {
				reasons.add("Very low progress");
				severity = "high";
			}

			Double speakingScore = getAverageSpeakingScore(student);
			if (speakingScore != null && speakingScore < 30) {
				reasons.add("Low speaking performance");
				if ("high".equals(severity))
					severity = "high";
				else
					severity = "medium";
			}

			List<SpeakingSession> recentSessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(student.getId(),
					LocalDateTime.now().minusDays(7), LocalDateTime.now());
			if (recentSessions.isEmpty() && student.getStatus() == Status.ACTIVE) {
				reasons.add("No practice this week");
				if ("low".equals(severity))
					severity = "medium";
			}

			if (student.getStatus() == Status.INACTIVE) {
				reasons.add("Account inactive");
				severity = "high";
			}

			if (!reasons.isEmpty()) {
				attentionStudents.add(StudentAttentionItemResponse.builder().studentId(student.getId())
						.studentName(student.getFirstName() + " " + student.getLastName()).reason(String.join(", ", reasons))
						.severity(severity).build());
			}
		}
		attentionStudents.sort((a, b) -> {
			int order = getSeverityOrder(b.getSeverity()) - getSeverityOrder(a.getSeverity());
			return Integer.compare(order, 0);
		});

		List<RecentActivityResponse> recentActivity = getRecentActivityForStudents(students);

		return TeacherDashboardResponse.builder().teacherInfo(profile).assignedClasses(assignedClasses)
				.totalStudents(totalStudents).averageProgress(avgProgress).weeklyCompletion(weeklyCompletion)
				.completedStudents((int) completedStudentsCount).skillPerformance(skillPerformance)
				.studentsRequiringAttention(attentionStudents).recentActivity(recentActivity).build();
	}

	private int getSeverityOrder(String severity) {
		if ("high".equals(severity))
			return 3;
		if ("medium".equals(severity))
			return 2;
		return 1;
	}

	@Override
	public TeacherStudentsListResponse getStudents(String search, Status status) {
		User teacher = getCurrentTeacher();
		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<User> students = getStudentsInClassesFiltered(classes, search, status);

		List<AssignedClassResponse> assignedClasses = classes.stream().map(c -> AssignedClassResponse.builder().id(c.getId())
				.name(c.getName()).grade(c.getGrade()).academicYear(c.getAcademicYear()).status(c.getStatus()).build())
				.collect(Collectors.toList());

		List<TeacherStudentSummaryResponse> studentSummaries = students.stream().map(this::mapToStudentSummary)
				.collect(Collectors.toList());

		return TeacherStudentsListResponse.builder().assignedClasses(assignedClasses).totalStudents(students.size())
				.students(studentSummaries).build();
	}

	private TeacherStudentSummaryResponse mapToStudentSummary(User student) {
		Progress progress = progressRepository.findByUser(student).orElse(null);
		double overallProgress = progress != null && progress.getXp() != null ? progress.getXp() : 0;

		Double grammarScore = getAverageGrammarScore(student);
		Double vocabularyScore = progress != null && progress.getTotalVocabularyWords() != null
				? progress.getTotalVocabularyWords().doubleValue()
				: 0.0;
		Double speakingScore = getAverageSpeakingScore(student);
		Double listeningScore = getAverageListeningScore(student);

		return TeacherStudentSummaryResponse.builder().id(student.getId()).firstName(student.getFirstName())
				.lastName(student.getLastName()).email(student.getEmail()).rollNumber(student.getRollNumber())
				.overallProgress(overallProgress).grammarScore(grammarScore).vocabularyScore(vocabularyScore)
				.speakingScore(speakingScore).listeningScore(listeningScore).lastActive(student.getUpdatedAt())
				.status(student.getStatus()).build();
	}

	@Override
	public TeacherStudentDetailResponse getStudentDetail(Long studentId) {
		User teacher = getCurrentTeacher();
		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<Long> classIds = classes.stream().map(ClassRoom::getId).collect(Collectors.toList());
		List<ClassStudent> classStudents = classStudentRepository.findByClassIdIn(classIds);
		boolean belongsToTeacherClass = classStudents.stream().anyMatch(cs -> cs.getStudentId().equals(studentId));
		if (!belongsToTeacherClass) {
			throw new RuntimeException("Student not found in your classes");
		}

		User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
		if (student.getRole() != Role.STUDENT) {
			throw new RuntimeException("User is not a student");
		}

		Progress progress = progressRepository.findByUser(student).orElse(null);
		ProfileResponse profile = buildProfileResponse(student, progress);

		Double grammarScore = getAverageGrammarScore(student);
		Double vocabularyScore = progress != null && progress.getTotalVocabularyWords() != null
				? progress.getTotalVocabularyWords().doubleValue()
				: 0.0;
		Double speakingScore = getAverageSpeakingScore(student);
		Double listeningScore = getAverageListeningScore(student);
		int lessonsCompleted = (int) lessonProgressRepository.countByUserIdAndCompletedTrue(student.getId());
		int totalSpeakingSessions = (int) speakingSessionRepository.countByUserIdAndCreatedAtBetween(student.getId(),
				LocalDateTime.now().minusYears(100), LocalDateTime.now());

		PerformanceSummaryResponse performance = PerformanceSummaryResponse.builder().overallScore(speakingScore)
				.grammarScore(grammarScore).vocabularyScore(vocabularyScore).speakingScore(speakingScore)
				.listeningScore(listeningScore).lessonsCompleted(lessonsCompleted)
				.totalSpeakingSessions(totalSpeakingSessions).build();

		List<RecentActivityResponse> recentActivity = new ArrayList<>();
		LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);

		List<SpeakingSession> sessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(student.getId(), oneWeekAgo,
				LocalDateTime.now());
		for (SpeakingSession s : sessions) {
			recentActivity.add(RecentActivityResponse.builder().id("speaking-" + s.getId()).type("speaking").icon("mic")
					.title(s.getTopic() != null ? "Speaking Session: " + s.getTopic() : "Speaking Session")
					.time(s.getCreatedAt()).xp(15).build());
		}

		List<Vocabulary> vocabs = vocabularyRepository.findByUserOrderByCreatedAtDesc(student);
		for (Vocabulary v : vocabs) {
			if (v.getCreatedAt() != null && !v.getCreatedAt().isBefore(oneWeekAgo)) {
				recentActivity.add(RecentActivityResponse.builder().id("vocabulary-" + v.getId()).type("vocabulary")
						.icon("library")
						.title(v.getWord() != null ? "Vocabulary Practice: " + v.getWord() : "Vocabulary Practice")
						.time(v.getCreatedAt()).xp(8).build());
			}
		}

		List<GrammarHistory> grammars = grammarHistoryRepository.findByUserIdAndCreatedAtBetween(student.getId(), oneWeekAgo,
				LocalDateTime.now());
		for (GrammarHistory g : grammars) {
			recentActivity.add(RecentActivityResponse.builder().id("grammar-" + g.getId()).type("grammar").icon("text")
					.title("Grammar Practice").time(g.getCreatedAt()).xp(10).build());
		}

		recentActivity.sort((a, b) -> b.getTime().compareTo(a.getTime()));
		if (recentActivity.size() > 10) {
			recentActivity = recentActivity.subList(0, 10);
		}

		List<StrengthImprovementResponse> strengths = new ArrayList<>();
		List<StrengthImprovementResponse> improvements = new ArrayList<>();

		if (grammarScore != null && grammarScore >= 70) {
			strengths.add(StrengthImprovementResponse.builder().skill("Grammar").score(grammarScore).label("Strong").build());
		} else if (grammarScore != null && grammarScore < 50) {
			improvements.add(StrengthImprovementResponse.builder().skill("Grammar").score(grammarScore).label("Needs Improvement").build());
		}

		if (vocabularyScore >= 70) {
			strengths.add(StrengthImprovementResponse.builder().skill("Vocabulary").score(vocabularyScore).label("Strong").build());
		} else if (vocabularyScore < 50) {
			improvements.add(StrengthImprovementResponse.builder().skill("Vocabulary").score(vocabularyScore).label("Needs Improvement").build());
		}

		if (speakingScore != null && speakingScore >= 70) {
			strengths.add(StrengthImprovementResponse.builder().skill("Speaking").score(speakingScore).label("Strong").build());
		} else if (speakingScore != null && speakingScore < 50) {
			improvements.add(StrengthImprovementResponse.builder().skill("Speaking").score(speakingScore).label("Needs Improvement").build());
		}

		if (listeningScore != null && listeningScore >= 70) {
			strengths.add(StrengthImprovementResponse.builder().skill("Listening").score(listeningScore).label("Strong").build());
		} else if (listeningScore != null && listeningScore < 50) {
			improvements.add(StrengthImprovementResponse.builder().skill("Listening").score(listeningScore).label("Needs Improvement").build());
		}

		List<Achievement> unlockedAchievements = achievementRepository.findByUserIdAndUnlockedTrueOrderByUnlockedAtDesc(student.getId());
		List<AchievementResponse> achievements = unlockedAchievements.stream()
				.map(a -> AchievementResponse.builder().id(a.getId()).title(a.getTitle()).description(a.getDescription())
						.xpReward(a.getXpReward()).tier(a.getTier() != null ? a.getTier() : 1).unlocked(a.getUnlocked())
						.unlockedAt(a.getUnlockedAt()).createdAt(a.getCreatedAt()).build())
				.collect(Collectors.toList());

		int totalPracticeMinutes = progress != null && progress.getTotalPracticeMinutes() != null ? progress.getTotalPracticeMinutes() : 0;
		int totalGrammarChecks = progress != null && progress.getTotalGrammarChecks() != null ? progress.getTotalGrammarChecks() : 0;
		int totalVocabularyWords = progress != null && progress.getTotalVocabularyWords() != null ? progress.getTotalVocabularyWords() : 0;
		double averageSessionScore = speakingScore != null ? speakingScore : 0;

		PracticeStatisticsResponse practiceStatistics = PracticeStatisticsResponse.builder()
				.totalSpeakingSessions(totalSpeakingSessions).totalPracticeMinutes(totalPracticeMinutes)
				.totalGrammarChecks(totalGrammarChecks).totalVocabularyWords(totalVocabularyWords)
				.totalLessonsCompleted(lessonsCompleted).averageSessionScore(averageSessionScore).build();

		List<WeeklyProgressResponse> weeklyCompletion = getWeeklyProgressForUser(student);

		Integer currentStreak = progress != null ? progress.getCurrentStreak() : 0;

		LocalDateTime lastPracticeDate = null;
		List<SpeakingSession> allSessions = speakingSessionRepository.findByUserOrderByCreatedAtDesc(student);
		if (!allSessions.isEmpty()) {
			lastPracticeDate = allSessions.get(0).getCreatedAt();
		} else {
			List<LessonProgress> lessonProgresses = lessonProgressRepository.findByUserOrderByLastOpenedAtDesc(student);
			if (!lessonProgresses.isEmpty() && lessonProgresses.get(0).getLastOpenedAt() != null) {
				lastPracticeDate = lessonProgresses.get(0).getLastOpenedAt();
			}
		}

		return TeacherStudentDetailResponse.builder().profile(profile).performance(performance).recentActivity(recentActivity)
				.strengths(strengths).improvementAreas(improvements).achievements(achievements)
				.practiceStatistics(practiceStatistics).weeklyCompletion(weeklyCompletion).currentStreak(currentStreak)
				.lastPracticeDate(lastPracticeDate).build();
	}

	@Override
	public TeacherAnalyticsResponse getAnalytics() {
		User teacher = getCurrentTeacher();
		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<User> students = getStudentsInClasses(classes);

		List<ClassPerformanceResponse> classPerformance = classes.stream().map(c -> {
			List<ClassStudent> classStudents = classStudentRepository.findByClassId(c.getId());
			List<Long> studentIds = classStudents.stream().map(ClassStudent::getStudentId).distinct()
					.collect(Collectors.toList());
			List<User> classStudentUsers = userRepository.findAllById(studentIds).stream()
					.filter(u -> u.getRole() == Role.STUDENT).collect(Collectors.toList());

			double avgProgress = 0;
			double avgScore = 0;
			int completedLessons = 0;
			long activeStudents = 0;

			if (!classStudentUsers.isEmpty()) {
				double totalXp = classStudentUsers.stream().mapToDouble(s -> {
					Progress p = progressRepository.findByUser(s).orElse(null);
					return p != null && p.getXp() != null ? p.getXp() : 0;
				}).sum();
				avgProgress = totalXp / classStudentUsers.size();

				List<Double> scores = new ArrayList<>();
				for (User s : classStudentUsers) {
					Double sp = getAverageSpeakingScore(s);
					if (sp != null)
						scores.add(sp);
					completedLessons += lessonProgressRepository.countByUserIdAndCompletedTrue(s.getId());
				}
				if (!scores.isEmpty()) {
					avgScore = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
				}

				activeStudents = classStudentUsers.stream().filter(s -> s.getStatus() == Status.ACTIVE).count();
			}

			return ClassPerformanceResponse.builder().classId(c.getId()).className(c.getName()).grade(c.getGrade())
					.totalStudents(classStudentUsers.size()).averageProgress(avgProgress).averageScore(avgScore)
					.completedLessons((int) completedLessons).activeStudents((int) activeStudents).build();
		}).collect(Collectors.toList());

		double avgGrammar = 0;
		double avgVocabulary = 0;
		double avgSpeaking = 0;
		double avgListening = 0;

		List<Double> grammarScores = new ArrayList<>();
		List<Double> vocabularyScores = new ArrayList<>();
		List<Double> speakingScores = new ArrayList<>();
		List<Double> listeningScores = new ArrayList<>();

		for (User student : students) {
			Double g = getAverageGrammarScore(student);
			if (g != null)
				grammarScores.add(g);
			Double v = (double) (progressRepository.findByUser(student).map(Progress::getTotalVocabularyWords).orElse(0));
			if (v > 0)
				vocabularyScores.add(v);
			Double sp = getAverageSpeakingScore(student);
			if (sp != null)
				speakingScores.add(sp);
			Double l = getAverageListeningScore(student);
			if (l != null)
				listeningScores.add(l);
		}

		if (!grammarScores.isEmpty()) {
			avgGrammar = grammarScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		}
		if (!vocabularyScores.isEmpty()) {
			avgVocabulary = vocabularyScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		}
		if (!speakingScores.isEmpty()) {
			avgSpeaking = speakingScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		}
		if (!listeningScores.isEmpty()) {
			avgListening = listeningScores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
		}

		SkillPerformanceSummaryResponse skillPerformance = SkillPerformanceSummaryResponse.builder()
				.grammar(avgGrammar).vocabulary(avgVocabulary).speaking(avgSpeaking).listening(avgListening).build();

		List<PerformanceTrendResponse> performanceTrends = new ArrayList<>();
		String[] periods = { "Week 1", "Week 2", "Week 3", "Week 4" };
		for (String period : periods) {
			performanceTrends.add(PerformanceTrendResponse.builder().period(period).averageScore(avgSpeaking)
					.sessionsCompleted(students.size() / 4).lessonsCompleted(students.size() / 5).build());
		}

		List<TopPerformerResponse> topPerformers = students.stream()
				.map(s -> {
					Double sp = getAverageSpeakingScore(s);
					return new Object[] { s, sp };
				})
				.sorted((a, b) -> {
					Double sa = (Double) a[1];
					Double sb = (Double) b[1];
					if (sa == null && sb == null)
						return 0;
					if (sa == null)
						return 1;
					if (sb == null)
						return -1;
					return sb.compareTo(sa);
				})
				.limit(5)
				.map(arr -> {
					User s = (User) arr[0];
					Double sp = (Double) arr[1];
					return TopPerformerResponse.builder().studentId(s.getId())
							.studentName(s.getFirstName() + " " + s.getLastName()).score(sp != null ? sp : 0)
							.metric("Speaking Score").build();
				})
				.collect(Collectors.toList());

		List<StudentAttentionItemResponse> attentionStudents = new ArrayList<>();
		for (User student : students) {
			List<String> reasons = new ArrayList<>();
			String severity = "low";

			Progress p = progressRepository.findByUser(student).orElse(null);
			int xp = p != null && p.getXp() != null ? p.getXp() : 0;

			if (xp < 50) {
				reasons.add("Very low progress");
				severity = "high";
			}

			Double speakingScore = getAverageSpeakingScore(student);
			if (speakingScore != null && speakingScore < 30) {
				reasons.add("Low speaking performance");
				if ("high".equals(severity))
					severity = "high";
				else
					severity = "medium";
			}

			List<SpeakingSession> recentSessions = speakingSessionRepository.findByUserIdAndCreatedAtBetween(student.getId(),
					LocalDateTime.now().minusDays(7), LocalDateTime.now());
			if (recentSessions.isEmpty() && student.getStatus() == Status.ACTIVE) {
				reasons.add("No practice this week");
				if ("low".equals(severity))
					severity = "medium";
			}

			if (student.getStatus() == Status.INACTIVE) {
				reasons.add("Account inactive");
				severity = "high";
			}

			if (!reasons.isEmpty()) {
				attentionStudents.add(StudentAttentionItemResponse.builder().studentId(student.getId())
						.studentName(student.getFirstName() + " " + student.getLastName()).reason(String.join(", ", reasons))
						.severity(severity).build());
			}
		}
		attentionStudents.sort((a, b) -> {
			int order = getSeverityOrder(b.getSeverity()) - getSeverityOrder(a.getSeverity());
			return Integer.compare(order, 0);
		});

		List<AiLearningInsightResponse> aiInsights = new ArrayList<>();
		aiInsights.add(AiLearningInsightResponse.builder()
				.insight("Grammar accuracy improved by 12% this week across all classes")
				.type("improvement").recommendation("Continue with grammar exercises").build());
		aiInsights.add(AiLearningInsightResponse.builder()
				.insight("Speaking session duration increased by 5 minutes on average")
				.type("trend").recommendation("Encourage more speaking practice").build());
		aiInsights.add(AiLearningInsightResponse.builder()
				.insight("Vocabulary retention rate is 78% for words practiced in last 30 days")
				.type("retention").recommendation("Introduce spaced repetition").build());

		return TeacherAnalyticsResponse.builder().classPerformance(classPerformance).skillPerformance(skillPerformance)
				.performanceTrends(performanceTrends).topPerformers(topPerformers)
				.studentsRequiringAttention(attentionStudents).aiLearningInsights(aiInsights).build();
	}

	@Override
	public TeacherReportsResponse getReports() {
		User teacher = getCurrentTeacher();
		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<User> students = getStudentsInClasses(classes);

		List<ReportCategoryResponse> reportCategories = List.of(
				ReportCategoryResponse.builder().id("class").name("Class Performance")
						.description("Detailed performance analysis per class").icon("bar-chart").build(),
				ReportCategoryResponse.builder().id("student").name("Student Progress")
						.description("Individual student progress reports").icon("person").build(),
				ReportCategoryResponse.builder().id("skill").name("Skill Analysis")
						.description("Grammar, vocabulary, speaking and listening analytics").icon("trending-up")
						.build(),
				ReportCategoryResponse.builder().id("attendance").name("Attendance Report")
						.description("Student attendance and participation tracking").icon("calendar").build());

		List<RecentReportResponse> recentReports = List.of(
				RecentReportResponse.builder().id("rpt-001").title("Class Performance - Grade 10A").type("class")
						.status("completed").generatedAt(LocalDateTime.now().minusDays(1)).downloadUrl("/reports/rpt-001")
						.build(),
				RecentReportResponse.builder().id("rpt-002").title("Student Progress - Monthly").type("student")
						.status("completed").generatedAt(LocalDateTime.now().minusDays(3)).downloadUrl("/reports/rpt-002")
						.build(),
				RecentReportResponse.builder().id("rpt-003").title("Skill Analysis - Q3").type("skill")
						.status("processing").generatedAt(LocalDateTime.now().minusHours(5)).downloadUrl(null).build());

		ReportStatusResponse reportStatus = ReportStatusResponse.builder().pending("1").completed("2").failed("0").build();

		double avgProgress = 0;
		if (!students.isEmpty()) {
			double totalXp = students.stream().mapToDouble(s -> {
				Progress p = progressRepository.findByUser(s).orElse(null);
				return p != null && p.getXp() != null ? p.getXp() : 0;
			}).sum();
			avgProgress = totalXp / students.size();
		}

		StatisticsResponse performanceSummary = StatisticsResponse.builder()
				.totalLessons((int) lessonProgressRepository.countByUserIdAndCompletedTrue(teacher.getId()))
				.completedLessons((int) lessonProgressRepository.countByUserIdAndCompletedTrue(teacher.getId()))
				.speakingSessions((int) speakingSessionRepository.countByUserIdAndCreatedAtBetween(teacher.getId(),
						LocalDateTime.now().minusYears(100), LocalDateTime.now()))
				.vocabularyLearned((int) vocabularyRepository.findByUser(teacher).stream().count())
				.grammarExercises((int) grammarHistoryRepository.countByUserIdAndCreatedAtBetween(teacher.getId(),
						LocalDateTime.now().minusYears(100), LocalDateTime.now()))
				.totalStudyHours(0.0).currentStreak(0).longestStreak(0).averageScore((int) Math.round(avgProgress))
				.build();

		List<UpcomingReportResponse> upcomingReports = List.of(
				UpcomingReportResponse.builder().id("rpt-upcoming-1").title("Monthly Progress Report").type("student")
						.dueDate(LocalDate.now().plusDays(5)).status("scheduled").build(),
				UpcomingReportResponse.builder().id("rpt-upcoming-2").title("Term End Assessment").type("class")
						.dueDate(LocalDate.now().plusDays(20)).status("scheduled").build());

		LocalDate today = LocalDate.now();
		LocalDate sessionStart = LocalDate.of(today.getYear(), 6, 1);
		LocalDate sessionEnd = LocalDate.of(today.getYear() + 1, 5, 31);
		long weeksBetween = java.time.temporal.ChronoUnit.WEEKS.between(today, sessionEnd);
		AcademicSessionResponse academicSession = AcademicSessionResponse.builder().name("2025-2026")
				.startDate(sessionStart).endDate(sessionEnd).currentTerm("Term 1")
				.totalWeeks(52)
				.remainingWeeks((int) weeksBetween).build();

		return TeacherReportsResponse.builder().reportCategories(reportCategories).recentReports(recentReports)
				.reportStatus(reportStatus).performanceSummary(performanceSummary).upcomingReports(upcomingReports)
				.academicSession(academicSession).build();
	}

	@Override
	public TeacherProfileResponse getProfile() {
		User teacher = getCurrentTeacher();
		Progress progress = progressRepository.findByUser(teacher).orElse(null);
		com.rslsolution.speakmateai.entity.Settings settings = settingsRepository.findByUser(teacher).orElse(null);

		IdentityResponse identity = IdentityResponse.builder().id(teacher.getId()).firstName(teacher.getFirstName())
				.lastName(teacher.getLastName()).email(teacher.getEmail()).avatar(teacher.getAvatar())
				.role(teacher.getRole().name()).build();

		ProfessionalInfoResponse professionalInfo = ProfessionalInfoResponse.builder()
				.employeeId("TCH-" + teacher.getId()).department("Languages").designation("Teacher")
				.qualification(teacher.getEnglishLevel()).experience("2 years")
				.joinedAt(teacher.getCreatedAt()).build();

		List<ClassRoom> classes = getTeacherClasses(teacher.getId());
		List<User> students = getStudentsInClasses(classes);
		int totalLessonsAssigned = (int) lessonProgressRepository.findByUser(teacher).stream().count();

		TeachingOverviewResponse teachingOverview = TeachingOverviewResponse.builder().totalClasses(classes.size())
				.totalStudents(students.size()).totalLessonsAssigned(totalLessonsAssigned).averageClassPerformance(0.0)
				.build();

		ContactInfoResponse contactInfo = ContactInfoResponse.builder().phone(teacher.getPhone()).alternatePhone(null)
				.address(teacher.getSchoolName()).city(null).state(null).country(null).build();

		AccountInfoResponse accountInfo = AccountInfoResponse.builder().active(teacher.isActive())
				.emailVerified(teacher.isEmailVerified()).createdAt(teacher.getCreatedAt()).updatedAt(teacher.getUpdatedAt())
				.lastLogin(null).build();

		UserPreferencesResponse userPreferences = UserPreferencesResponse.builder()
				.darkMode(settings != null ? settings.getDarkMode() : false)
				.notificationsEnabled(settings != null ? settings.getNotificationsEnabled() : true)
				.language(settings != null ? settings.getLanguage() : "English")
				.aiVoice(settings != null ? settings.getAiVoice() : "Female")
				.soundEffects(settings != null ? settings.getSoundEffects() : true)
				.autoPlayAudio(settings != null ? settings.getAutoPlayAudio() : true)
				.dailyReminder(settings != null ? settings.getDailyReminder() : true).build();

		return TeacherProfileResponse.builder().identity(identity).professionalInfo(professionalInfo)
				.teachingOverview(teachingOverview).contactInfo(contactInfo).accountInfo(accountInfo)
				.userPreferences(userPreferences).build();
	}
}
