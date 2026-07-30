package com.rslsolution.speakmateai.controller;

import com.rslsolution.speakmateai.dto.response.TeacherAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherDashboardResponse;
import com.rslsolution.speakmateai.dto.response.TeacherProfileResponse;
import com.rslsolution.speakmateai.dto.response.TeacherReportsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentDetailResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentsListResponse;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasRole('TEACHER')")
public class TeacherController {

	private final TeacherService teacherService;

	@GetMapping("/dashboard")
	public ResponseEntity<TeacherDashboardResponse> getTeacherDashboard() {
		return ResponseEntity.ok(teacherService.getTeacherDashboard());
	}

	@GetMapping("/students")
	public ResponseEntity<TeacherStudentsListResponse> getStudents(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) Status status) {
		return ResponseEntity.ok(teacherService.getStudents(search, status));
	}

	@GetMapping("/students/{studentId}")
	public ResponseEntity<TeacherStudentDetailResponse> getStudentDetail(@PathVariable Long studentId) {
		return ResponseEntity.ok(teacherService.getStudentDetail(studentId));
	}

	@GetMapping("/analytics")
	public ResponseEntity<TeacherAnalyticsResponse> getAnalytics() {
		return ResponseEntity.ok(teacherService.getAnalytics());
	}

	@GetMapping("/reports")
	public ResponseEntity<TeacherReportsResponse> getReports() {
		return ResponseEntity.ok(teacherService.getReports());
	}

	@GetMapping("/profile")
	public ResponseEntity<TeacherProfileResponse> getProfile() {
		return ResponseEntity.ok(teacherService.getProfile());
	}
}
