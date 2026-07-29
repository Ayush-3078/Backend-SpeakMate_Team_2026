package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.request.SchoolRequest;
import com.rslsolution.speakmateai.dto.request.SchoolStatusRequest;
import com.rslsolution.speakmateai.dto.request.SchoolUpdateRequest;
import com.rslsolution.speakmateai.dto.response.ImpersonateResponse;
import com.rslsolution.speakmateai.dto.response.SchoolAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.SchoolResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;
import com.rslsolution.speakmateai.dto.response.TeacherResponse;
import com.rslsolution.speakmateai.entity.School;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.SchoolStatus;
import com.rslsolution.speakmateai.exception.DuplicateSchoolCodeException;
import com.rslsolution.speakmateai.exception.SchoolNotFoundException;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.repository.ChatSessionRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.SchoolService;
import com.rslsolution.speakmateai.util.JwtUtil;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

	private final SchoolRepository schoolRepository;
	private final UserRepository userRepository;
	private final ChatSessionRepository chatSessionRepository;
	private final SpeakingSessionRepository speakingSessionRepository;
	private final LessonProgressRepository lessonProgressRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final JwtUtil jwtUtil;

	public SchoolServiceImpl(SchoolRepository schoolRepository, UserRepository userRepository,
			ChatSessionRepository chatSessionRepository, SpeakingSessionRepository speakingSessionRepository,
			LessonProgressRepository lessonProgressRepository, ChatMessageRepository chatMessageRepository,
			JwtUtil jwtUtil) {
		this.schoolRepository = schoolRepository;
		this.userRepository = userRepository;
		this.chatSessionRepository = chatSessionRepository;
		this.speakingSessionRepository = speakingSessionRepository;
		this.lessonProgressRepository = lessonProgressRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public SchoolResponse createSchool(SchoolRequest request) {
		if (schoolRepository.existsBySchoolCode(request.getSchoolCode())) {
			throw new DuplicateSchoolCodeException("School code already exists: " + request.getSchoolCode());
		}

		School school = School.builder()
				.schoolName(request.getSchoolName())
				.schoolCode(request.getSchoolCode())
				.location(request.getLocation())
				.admin(request.getAdmin())
				.totalStudents(request.getTotalStudents() != null ? request.getTotalStudents() : 0)
				.subscriptionPlan(request.getSubscriptionPlan())
				.subscriptionStatus(request.getSubscriptionStatus())
				.status(request.getStatus() != null ? request.getStatus() : SchoolStatus.ACTIVE)
				.build();

		School saved = schoolRepository.save(school);
		return mapToResponse(saved);
	}

	@Override
	public Page<SchoolResponse> getAllSchools(String keyword, Pageable pageable) {
		Specification<School> spec = (root, query, cb) -> {
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			String likeKeyword = "%" + keyword.trim().toLowerCase() + "%";
			return cb.or(
					cb.like(cb.lower(root.get("schoolName")), likeKeyword),
					cb.like(cb.lower(root.get("schoolCode")), likeKeyword),
					cb.like(cb.lower(root.get("location")), likeKeyword),
					cb.like(cb.lower(root.get("admin")), likeKeyword)
			);
		};

		return schoolRepository.findAll(spec, pageable).map(this::mapToResponse);
	}

	@Override
	public SchoolResponse getSchoolById(Long id) {
		School school = schoolRepository.findById(id)
				.orElseThrow(() -> new SchoolNotFoundException("School not found with id: " + id));
		return mapToResponse(school);
	}

	@Override
	public SchoolResponse updateSchool(Long id, SchoolUpdateRequest request) {
		School school = schoolRepository.findById(id)
				.orElseThrow(() -> new SchoolNotFoundException("School not found with id: " + id));

		if (request.getSchoolName() != null) {
			school.setSchoolName(request.getSchoolName());
		}
		if (request.getLocation() != null) {
			school.setLocation(request.getLocation());
		}
		if (request.getAdmin() != null) {
			school.setAdmin(request.getAdmin());
		}
		if (request.getTotalStudents() != null) {
			school.setTotalStudents(request.getTotalStudents());
		}
		if (request.getSubscriptionPlan() != null) {
			school.setSubscriptionPlan(request.getSubscriptionPlan());
		}
		if (request.getSubscriptionStatus() != null) {
			school.setSubscriptionStatus(request.getSubscriptionStatus());
		}
		if (request.getStatus() != null) {
			school.setStatus(request.getStatus());
		}

		School updated = schoolRepository.save(school);
		return mapToResponse(updated);
	}

	@Override
	public void deleteSchool(Long id) {
		if (!schoolRepository.existsById(id)) {
			throw new SchoolNotFoundException("School not found with id: " + id);
		}
		schoolRepository.deleteById(id);
	}

	@Override
	public SchoolResponse updateSchoolStatus(Long id, SchoolStatusRequest request) {
		School school = schoolRepository.findById(id)
				.orElseThrow(() -> new SchoolNotFoundException("School not found with id: " + id));
		school.setStatus(request.getStatus());
		School updated = schoolRepository.save(school);
		return mapToResponse(updated);
	}

	@Override
	public List<StudentResponse> getStudents(Long schoolId) {
		List<User> students = userRepository.findBySchoolIdAndUserType(schoolId, "Student");
		return students.stream()
				.map(user -> StudentResponse.builder()
						.id(user.getId())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.email(user.getEmail())
						.role(user.getRole())
						.active(user.isActive())
						.userType(user.getUserType())
						.build())
				.toList();
	}

	@Override
	public List<TeacherResponse> getTeachers(Long schoolId) {
		List<User> teachers = userRepository.findBySchoolIdAndUserType(schoolId, "Teacher");
		return teachers.stream()
				.map(user -> TeacherResponse.builder()
						.id(user.getId())
						.firstName(user.getFirstName())
						.lastName(user.getLastName())
						.email(user.getEmail())
						.role(user.getRole())
						.active(user.isActive())
						.userType(user.getUserType())
						.build())
				.toList();
	}

	@Override
	public SchoolAnalyticsResponse getAnalytics(Long schoolId) {
		School school = schoolRepository.findById(schoolId)
				.orElseThrow(() -> new SchoolNotFoundException("School not found with id: " + schoolId));

		long totalStudents = userRepository.countBySchoolId(schoolId);
		long activeStudents = userRepository.countBySchoolIdAndActiveTrue(schoolId);
		long lessonsCompleted = lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(schoolId);
		long aiConversations = chatSessionRepository.countByUserSchoolId(schoolId);
		long speakingSessions = speakingSessionRepository.countByUserSchoolId(schoolId);
		Integer totalDurationSeconds = speakingSessionRepository.sumDurationByUserSchoolId(schoolId);
		long practiceMinutes = totalDurationSeconds != null
				? Math.round(totalDurationSeconds / 60.0f)
				: 0L;

		return SchoolAnalyticsResponse.builder()
				.totalStudents(totalStudents)
				.activeStudents(activeStudents)
				.lessonsCompleted(lessonsCompleted)
				.aiConversations(aiConversations)
				.speakingSessions(speakingSessions)
				.practiceMinutes(practiceMinutes)
				.build();
	}

	@Override
	public ImpersonateResponse impersonateSchool(Long schoolId) {
		School school = schoolRepository.findById(schoolId)
				.orElseThrow(() -> new SchoolNotFoundException("School not found with id: " + schoolId));

		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		String impersonatedEmail = "impersonate_" + school.getSchoolCode() + "_" + adminEmail;

		String token = jwtUtil.generateToken(impersonatedEmail);

		return ImpersonateResponse.builder()
				.token(token)
				.message("Impersonation token generated for school: " + school.getSchoolName())
				.impersonatedEmail(impersonatedEmail)
				.build();
	}

	private SchoolResponse mapToResponse(School school) {
		return SchoolResponse.builder()
				.id(school.getId())
				.schoolName(school.getSchoolName())
				.schoolCode(school.getSchoolCode())
				.location(school.getLocation())
				.admin(school.getAdmin())
				.totalStudents(school.getTotalStudents())
				.subscriptionPlan(school.getSubscriptionPlan())
				.subscriptionStatus(school.getSubscriptionStatus())
				.status(school.getStatus())
				.createdDate(school.getCreatedDate())
				.build();
	}
}
