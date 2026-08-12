package com.rslsolution.speakmateai.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.request.SchoolTeacherRequest;
import com.rslsolution.speakmateai.dto.response.SchoolTeacherResponse;
import com.rslsolution.speakmateai.entity.Progress;
import com.rslsolution.speakmateai.entity.School;
import com.rslsolution.speakmateai.entity.Settings;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.ProgressRepository;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SettingsRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.SchoolTeacherService;

import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class SchoolTeacherServiceImpl implements SchoolTeacherService {

	private final UserRepository userRepository;
	private final SchoolRepository schoolRepository;
	private final SettingsRepository settingsRepository;
	private final ProgressRepository progressRepository;
	private final PasswordEncoder passwordEncoder;

	public SchoolTeacherServiceImpl(UserRepository userRepository, SchoolRepository schoolRepository,
			SettingsRepository settingsRepository, ProgressRepository progressRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.schoolRepository = schoolRepository;
		this.settingsRepository = settingsRepository;
		this.progressRepository = progressRepository;
		this.passwordEncoder = passwordEncoder;
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	private SchoolTeacherResponse mapToResponse(User teacher) {
		School school = teacher.getSchoolId() != null ? schoolRepository.findById(teacher.getSchoolId()).orElse(null) : null;
		return SchoolTeacherResponse.builder().id(teacher.getId()).firstName(teacher.getFirstName()).lastName(teacher.getLastName())
				.email(teacher.getEmail()).phone(teacher.getPhone()).schoolId(teacher.getSchoolId())
				.schoolName(school != null ? school.getName() : null).active(teacher.isActive()).role(teacher.getRole().name())
				.build();
	}

	@Override
	public SchoolTeacherResponse createTeacher(SchoolTeacherRequest request) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can create teachers");
		}

		if (currentUser.getSchoolId() == null) {
			throw new RuntimeException("School Admin is not associated with any school");
		}

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		User teacher = User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(Role.TEACHER).schoolId(currentUser.getSchoolId())
				.phone(request.getPhone()).active(request.isActive()).userType(com.rslsolution.speakmateai.enums.UserType.SCHOOL)
				.status(Status.ACTIVE).build();

		User savedTeacher = userRepository.save(teacher);

		Settings settings = Settings.builder().user(savedTeacher).build();
		settingsRepository.save(settings);

		Progress progress = Progress.builder().user(savedTeacher).build();
		progressRepository.save(progress);

		return mapToResponse(savedTeacher);
	}

	@Override
	public List<SchoolTeacherResponse> getAllTeachers() {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can view teachers");
		}

		if (currentUser.getSchoolId() == null) {
			throw new RuntimeException("School Admin is not associated with any school");
		}

		List<User> teachers = userRepository.findBySchoolIdAndRole(currentUser.getSchoolId(), Role.TEACHER);
		return teachers.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<SchoolTeacherResponse> searchTeachers(String query) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can search teachers");
		}

		if (currentUser.getSchoolId() == null) {
			throw new RuntimeException("School Admin is not associated with any school");
		}

		String lowerQuery = query.toLowerCase();
		List<User> teachers = userRepository.findBySchoolIdAndRole(currentUser.getSchoolId(), Role.TEACHER).stream()
				.filter(t -> (t.getFirstName() != null && t.getFirstName().toLowerCase().contains(lowerQuery))
						|| (t.getLastName() != null && t.getLastName().toLowerCase().contains(lowerQuery))
						|| (t.getEmail() != null && t.getEmail().toLowerCase().contains(lowerQuery)))
				.collect(Collectors.toList());

		return teachers.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public SchoolTeacherResponse getTeacherById(Long id) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can view teachers");
		}

		User teacher = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Teacher not found"));

		if (teacher.getRole() != Role.TEACHER) {
			throw new RuntimeException("User is not a teacher");
		}

		if (!teacher.getSchoolId().equals(currentUser.getSchoolId())) {
			throw new RuntimeException("Unauthorized: Teacher does not belong to your school");
		}

		return mapToResponse(teacher);
	}

	@Override
	public SchoolTeacherResponse updateTeacher(Long id, SchoolTeacherRequest request) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can update teachers");
		}

		User teacher = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Teacher not found"));

		if (teacher.getRole() != Role.TEACHER) {
			throw new RuntimeException("User is not a teacher");
		}

		if (!teacher.getSchoolId().equals(currentUser.getSchoolId())) {
			throw new RuntimeException("Unauthorized: Teacher does not belong to your school");
		}

		teacher.setFirstName(request.getFirstName());
		teacher.setLastName(request.getLastName());
		teacher.setPhone(request.getPhone());
		teacher.setActive(request.isActive());

		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			teacher.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		User updatedTeacher = userRepository.save(teacher);
		return mapToResponse(updatedTeacher);
	}

	@Override
	public void deactivateTeacher(Long id) {
		User currentUser = getCurrentUser();

		if (currentUser.getRole() != Role.SCHOOL_ADMIN) {
			throw new RuntimeException("Unauthorized: Only School Admin can deactivate teachers");
		}

		User teacher = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Teacher not found"));

		if (teacher.getRole() != Role.TEACHER) {
			throw new RuntimeException("User is not a teacher");
		}

		if (!teacher.getSchoolId().equals(currentUser.getSchoolId())) {
			throw new RuntimeException("Unauthorized: Teacher does not belong to your school");
		}

		teacher.setActive(false);
		userRepository.save(teacher);
	}
}
