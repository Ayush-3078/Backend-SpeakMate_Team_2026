package com.rslsolution.speakmateai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
import com.rslsolution.speakmateai.enums.SubscriptionPlan;
import com.rslsolution.speakmateai.enums.SubscriptionStatus;
import com.rslsolution.speakmateai.exception.DuplicateSchoolCodeException;
import com.rslsolution.speakmateai.exception.SchoolNotFoundException;
import com.rslsolution.speakmateai.repository.ChatMessageRepository;
import com.rslsolution.speakmateai.repository.ChatSessionRepository;
import com.rslsolution.speakmateai.repository.LessonProgressRepository;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SpeakingSessionRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.impl.SchoolServiceImpl;
import com.rslsolution.speakmateai.util.JwtUtil;

import jakarta.persistence.criteria.Predicate;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceImplTest {

	@Mock
	private SchoolRepository schoolRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ChatSessionRepository chatSessionRepository;

	@Mock
	private SpeakingSessionRepository speakingSessionRepository;

	@Mock
	private LessonProgressRepository lessonProgressRepository;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private SchoolServiceImpl schoolService;

	@Test
	void createSchool_ReturnsSchoolResponse() {
		SchoolRequest request = SchoolRequest.builder()
				.schoolName("Test School")
				.schoolCode("SCH001")
				.location("City")
				.admin("John Doe")
				.totalStudents(100)
				.subscriptionPlan(SubscriptionPlan.PREMIUM)
				.subscriptionStatus(SubscriptionStatus.ACTIVE)
				.status(SchoolStatus.ACTIVE)
				.build();

		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.location("City")
				.admin("John Doe")
				.totalStudents(100)
				.subscriptionPlan(SubscriptionPlan.PREMIUM)
				.subscriptionStatus(SubscriptionStatus.ACTIVE)
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		when(schoolRepository.existsBySchoolCode("SCH001")).thenReturn(false);
		when(schoolRepository.save(any(School.class))).thenReturn(school);

		SchoolResponse response = schoolService.createSchool(request);

		assertNotNull(response);
		assertEquals("Test School", response.getSchoolName());
		assertEquals("SCH001", response.getSchoolCode());
		assertEquals(100, response.getTotalStudents());
	}

	@Test
	void createSchool_DuplicateCode_ThrowsException() {
		SchoolRequest request = SchoolRequest.builder()
				.schoolName("Test School")
				.schoolCode("SCH001")
				.build();

		when(schoolRepository.existsBySchoolCode("SCH001")).thenReturn(true);

		assertThrows(DuplicateSchoolCodeException.class, () -> schoolService.createSchool(request));
	}

	@Test
	void getAllSchools_ReturnsPagedResults() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		Pageable pageable = PageRequest.of(0, 10);
		Page<School> schoolPage = new PageImpl<>(List.of(school), pageable, 1);

		when(schoolRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(schoolPage);

		Page<SchoolResponse> response = schoolService.getAllSchools(null, pageable);

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals("Test School", response.getContent().get(0).getSchoolName());
	}

	@Test
	void getAllSchools_WithSearch_ReturnsFilteredResults() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<School> schoolPage = new PageImpl<>(List.of(), pageable, 0);

		when(schoolRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(schoolPage);

		Page<SchoolResponse> response = schoolService.getAllSchools("Test", pageable);

		assertNotNull(response);
		assertEquals(0, response.getTotalElements());
	}

	@Test
	void getSchoolById_ReturnsSchool() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

		SchoolResponse response = schoolService.getSchoolById(1L);

		assertNotNull(response);
		assertEquals("Test School", response.getSchoolName());
	}

	@Test
	void getSchoolById_NotFound_ThrowsException() {
		when(schoolRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(SchoolNotFoundException.class, () -> schoolService.getSchoolById(999L));
	}

	@Test
	void updateSchool_ReturnsUpdatedSchool() {
		School existing = School.builder()
				.id(1L)
				.schoolName("Old Name")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		SchoolUpdateRequest request = SchoolUpdateRequest.builder()
				.schoolName("New Name")
				.location("New Location")
				.admin("New Admin")
				.totalStudents(200)
				.subscriptionPlan(SubscriptionPlan.ENTERPRISE)
				.subscriptionStatus(SubscriptionStatus.ACTIVE)
				.status(SchoolStatus.ACTIVE)
				.build();

		when(schoolRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(schoolRepository.save(any(School.class))).thenReturn(existing);

		SchoolResponse response = schoolService.updateSchool(1L, request);

		assertNotNull(response);
		assertEquals("New Name", response.getSchoolName());
		assertEquals("New Location", response.getLocation());
		assertEquals("New Admin", response.getAdmin());
		assertEquals(200, response.getTotalStudents());
		assertEquals(SubscriptionPlan.ENTERPRISE, response.getSubscriptionPlan());
	}

	@Test
	void updateSchool_NotFound_ThrowsException() {
		SchoolUpdateRequest request = SchoolUpdateRequest.builder().schoolName("New Name").build();
		when(schoolRepository.findById(999L)).thenReturn(Optional.empty());

		assertThrows(SchoolNotFoundException.class, () -> schoolService.updateSchool(999L, request));
	}

	@Test
	void deleteSchool_Success() {
		when(schoolRepository.existsById(1L)).thenReturn(true);

		schoolService.deleteSchool(1L);
	}

	@Test
	void deleteSchool_NotFound_ThrowsException() {
		when(schoolRepository.existsById(999L)).thenReturn(false);

		assertThrows(SchoolNotFoundException.class, () -> schoolService.deleteSchool(999L));
	}

	@Test
	void updateSchoolStatus_ReturnsUpdatedSchool() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		SchoolStatusRequest request = SchoolStatusRequest.builder()
				.status(SchoolStatus.SUSPENDED)
				.build();

		when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
		when(schoolRepository.save(any(School.class))).thenReturn(school);

		SchoolResponse response = schoolService.updateSchoolStatus(1L, request);

		assertNotNull(response);
		assertEquals(SchoolStatus.SUSPENDED, response.getStatus());
	}

	@Test
	void getStudents_ReturnsStudentList() {
		User student = User.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.email("john@test.com")
				.role(Role.USER)
				.active(true)
				.userType("Student")
				.build();

		when(userRepository.findBySchoolIdAndUserType(1L, "Student")).thenReturn(List.of(student));

		List<StudentResponse> response = schoolService.getStudents(1L);

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals("John", response.get(0).getFirstName());
		assertEquals("Student", response.get(0).getUserType());
	}

	@Test
	void getTeachers_ReturnsTeacherList() {
		User teacher = User.builder()
				.id(2L)
				.firstName("Jane")
				.lastName("Smith")
				.email("jane@test.com")
				.role(Role.USER)
				.active(true)
				.userType("Teacher")
				.build();

		when(userRepository.findBySchoolIdAndUserType(1L, "Teacher")).thenReturn(List.of(teacher));

		List<TeacherResponse> response = schoolService.getTeachers(1L);

		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals("Jane", response.get(0).getFirstName());
		assertEquals("Teacher", response.get(0).getUserType());
	}

	@Test
	void getAnalytics_ReturnsAnalytics() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
		when(userRepository.countBySchoolId(1L)).thenReturn(100L);
		when(userRepository.countBySchoolIdAndActiveTrue(1L)).thenReturn(80L);
		when(lessonProgressRepository.countByUserSchoolIdAndCompletedTrue(1L)).thenReturn(50L);
		when(chatSessionRepository.countByUserSchoolId(1L)).thenReturn(200L);
		when(speakingSessionRepository.countByUserSchoolId(1L)).thenReturn(500L);
		when(speakingSessionRepository.sumDurationByUserSchoolId(1L)).thenReturn(360000);

		SchoolAnalyticsResponse response = schoolService.getAnalytics(1L);

		assertNotNull(response);
		assertEquals(100, response.getTotalStudents());
		assertEquals(80, response.getActiveStudents());
		assertEquals(50, response.getLessonsCompleted());
		assertEquals(200, response.getAiConversations());
		assertEquals(500, response.getSpeakingSessions());
		assertEquals(6000, response.getPracticeMinutes());
	}

	@Test
	void impersonateSchool_ReturnsToken() {
		School school = School.builder()
				.id(1L)
				.schoolName("Test School")
				.schoolCode("SCH001")
				.status(SchoolStatus.ACTIVE)
				.createdDate(LocalDateTime.now())
				.build();

		mockAuthentication("admin@test.com");
		when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));
		when(jwtUtil.generateToken(any(String.class))).thenReturn("mock-jwt-token");

		ImpersonateResponse response = schoolService.impersonateSchool(1L);

		assertNotNull(response);
		assertEquals("mock-jwt-token", response.getToken());
		assertEquals("Impersonation token generated for school: Test School", response.getMessage());
	}

	private void mockAuthentication(String email) {
		when(authentication.getName()).thenReturn(email);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
	}
}
