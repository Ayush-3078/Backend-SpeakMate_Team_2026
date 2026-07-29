package com.rslsolution.speakmateai.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.request.StudentRequest;
import com.rslsolution.speakmateai.dto.response.StudentImportResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.exception.UserNotFoundException;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.StudentService;
import com.rslsolution.speakmateai.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public StudentServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	private User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
			throw new UserNotFoundException("User not authenticated");
		}
		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	private boolean isSuperAdmin() {
		return getCurrentUser().getRole() == Role.SUPER_ADMIN;
	}

	@Override
	public List<StudentResponse> getAllStudents() {
		logger.debug("[StudentService] Fetching all students");
		User currentUser = getCurrentUser();
		List<User> students;

		if (isSuperAdmin()) {
			students = userRepository.findAllByRole(Role.STUDENT);
		} else {
			Long schoolId = currentUser.getSchoolId();
			students = userRepository.findAllByRoleAndSchoolId(Role.STUDENT, schoolId);
		}

		return students.stream()
				.map(this::mapToStudentResponse)
				.collect(Collectors.toList());
	}

	@Override
	public StudentResponse getStudentById(Long id) {
		logger.debug("[StudentService] Fetching student by id: {}", id);
		User currentUser = getCurrentUser();
		User student;

		if (isSuperAdmin()) {
			student = userRepository.findByIdAndRole(id, Role.STUDENT)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		} else {
			Long schoolId = currentUser.getSchoolId();
			student = userRepository.findByIdAndRoleAndSchoolId(id, Role.STUDENT, schoolId)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		}

		return mapToStudentResponse(student);
	}

	@Override
	public StudentResponse createStudent(StudentRequest request) {
		logger.debug("[StudentService] Creating student: {}", request.getEmail());
		User currentUser = getCurrentUser();

		Long schoolId;
		if (isSuperAdmin()) {
			schoolId = request.getSchoolId();
			if (schoolId == null) {
				throw new IllegalArgumentException("schoolId is required for SUPER_ADMIN");
			}
		} else {
			schoolId = currentUser.getSchoolId();
		}

		if (userRepository.existsByStudentId(request.getStudentId())) {
			throw new IllegalArgumentException("Student ID already exists: " + request.getStudentId());
		}

		User student = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(request.getPassword())
				.role(Role.STUDENT)
				.schoolId(schoolId)
				.studentId(request.getStudentId())
				.status(request.getStatus() != null ? request.getStatus() : Status.ACTIVE)
				.active(true)
				.userType("Student")
				.build();

		User saved = userRepository.save(student);
		return mapToStudentResponse(saved);
	}

	@Override
	public StudentResponse updateStudent(Long id, StudentRequest request) {
		logger.debug("[StudentService] Updating student: {}", id);
		User currentUser = getCurrentUser();
		User student;

		if (isSuperAdmin()) {
			student = userRepository.findByIdAndRole(id, Role.STUDENT)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		} else {
			Long schoolId = currentUser.getSchoolId();
			student = userRepository.findByIdAndRoleAndSchoolId(id, Role.STUDENT, schoolId)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		}

		if (request.getFirstName() != null) {
			student.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			student.setLastName(request.getLastName());
		}
		if (request.getEmail() != null) {
			student.setEmail(request.getEmail());
		}
		if (request.getStudentId() != null) {
			student.setStudentId(request.getStudentId());
		}
		if (request.getStatus() != null) {
			student.setStatus(request.getStatus());
		}

		User updated = userRepository.save(student);
		return mapToStudentResponse(updated);
	}

	@Override
	public void deleteStudent(Long id) {
		logger.debug("[StudentService] Deleting student: {}", id);
		User currentUser = getCurrentUser();
		User student;

		if (isSuperAdmin()) {
			student = userRepository.findByIdAndRole(id, Role.STUDENT)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		} else {
			Long schoolId = currentUser.getSchoolId();
			student = userRepository.findByIdAndRoleAndSchoolId(id, Role.STUDENT, schoolId)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		}

		userRepository.delete(student);
	}

	@Override
	public StudentImportResponse importStudents(List<StudentRequest> students) {
		logger.debug("[StudentService] Importing {} students", students.size());
		User currentUser = getCurrentUser();
		List<String> errors = new ArrayList<>();
		int successCount = 0;

		for (StudentRequest request : students) {
			try {
				Long schoolId;
				if (isSuperAdmin()) {
					schoolId = request.getSchoolId();
					if (schoolId == null) {
						errors.add("Student " + request.getEmail() + ": schoolId is required for SUPER_ADMIN");
						continue;
					}
				} else {
					schoolId = currentUser.getSchoolId();
				}

				if (userRepository.existsByStudentId(request.getStudentId())) {
					errors.add("Student " + request.getEmail() + ": Student ID already exists");
					continue;
				}

				User student = User.builder()
						.firstName(request.getFirstName())
						.lastName(request.getLastName())
						.email(request.getEmail())
						.password(request.getPassword())
						.role(Role.STUDENT)
						.schoolId(schoolId)
						.studentId(request.getStudentId())
						.status(request.getStatus() != null ? request.getStatus() : Status.ACTIVE)
						.active(true)
						.userType("Student")
						.build();

				userRepository.save(student);
				successCount++;
			} catch (Exception ex) {
				errors.add("Student " + request.getEmail() + ": " + ex.getMessage());
			}
		}

		return StudentImportResponse.builder()
				.totalProcessed(students.size())
				.successCount(successCount)
				.failureCount(errors.size())
				.errors(errors)
				.build();
	}

	@Override
	public byte[] exportStudents(String format) {
		logger.debug("[StudentService] Exporting students in format: {}", format);
		List<StudentResponse> students = getAllStudents();

		if ("csv".equalsIgnoreCase(format)) {
			return exportToCsv(students);
		} else if ("excel".equalsIgnoreCase(format)) {
			return exportToExcel(students);
		} else {
			throw new IllegalArgumentException("Unsupported format: " + format);
		}
	}

	@Override
	public Map<String, String> resetStudentPassword(Long id) {
		logger.debug("[StudentService] Resetting password for student: {}", id);
		User currentUser = getCurrentUser();
		User student;

		if (isSuperAdmin()) {
			student = userRepository.findByIdAndRole(id, Role.STUDENT)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		} else {
			Long schoolId = currentUser.getSchoolId();
			student = userRepository.findByIdAndRoleAndSchoolId(id, Role.STUDENT, schoolId)
					.orElseThrow(() -> new UserNotFoundException("Student not found with id: " + id));
		}

		String tempPassword = "Temp" + (int) (Math.random() * 1000000);
		student.setPassword(tempPassword);
		userRepository.save(student);

		return Map.of(
				"studentId", String.valueOf(student.getId()),
				"email", student.getEmail(),
				"temporaryPassword", tempPassword
		);
	}

	private StudentResponse mapToStudentResponse(User user) {
		return StudentResponse.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.role(user.getRole())
				.active(user.isActive())
				.userType(user.getUserType())
				.schoolId(user.getSchoolId())
				.studentId(user.getStudentId())
				.status(user.getStatus())
				.build();
	}

	private byte[] exportToCsv(List<StudentResponse> students) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(out, true, StandardCharsets.UTF_8);
		writer.println("ID,First Name,Last Name,Email,Student ID,School ID,Status,Active,User Type");

		for (StudentResponse s : students) {
			writer.printf("%d,%s,%s,%s,%s,%s,%s,%b,%s%n",
					s.getId(),
					escapeCsv(s.getFirstName()),
					escapeCsv(s.getLastName()),
					escapeCsv(s.getEmail()),
					escapeCsv(s.getStudentId()),
					s.getSchoolId() != null ? s.getSchoolId() : "",
					s.getStatus() != null ? s.getStatus() : "",
					s.getActive() != null ? s.getActive() : false,
					escapeCsv(s.getUserType()));
		}

		writer.flush();
		return out.toByteArray();
	}

	private byte[] exportToExcel(List<StudentResponse> students) {
		StringBuilder sb = new StringBuilder();
		sb.append("ID\tFirst Name\tLast Name\tEmail\tStudent ID\tSchool ID\tStatus\tActive\tUser Type\r\n");

		for (StudentResponse s : students) {
			sb.append(String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s\t%b\t%s\r\n",
					s.getId(),
					s.getFirstName(),
					s.getLastName(),
					s.getEmail(),
					s.getStudentId(),
					s.getSchoolId() != null ? s.getSchoolId() : "",
					s.getStatus() != null ? s.getStatus() : "",
					s.getActive() != null ? s.getActive() : false,
					s.getUserType()));
		}

		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	private String escapeCsv(String value) {
		if (value == null) return "";
		if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
			return "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return value;
	}
}
