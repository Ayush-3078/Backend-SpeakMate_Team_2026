package com.rslsolution.speakmateai.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rslsolution.speakmateai.dto.request.SchoolRequest;
import com.rslsolution.speakmateai.dto.request.SchoolStatusRequest;
import com.rslsolution.speakmateai.dto.request.SchoolUpdateRequest;
import com.rslsolution.speakmateai.dto.response.ImpersonateResponse;
import com.rslsolution.speakmateai.dto.response.SchoolAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.SchoolResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;
import com.rslsolution.speakmateai.dto.response.TeacherResponse;
import com.rslsolution.speakmateai.service.SchoolService;

@RestController
@RequestMapping("/api/v1/admin/schools")
@PreAuthorize("hasRole('SCHOOL_ADMIN')")
public class SchoolController {

	private final SchoolService schoolService;

	public SchoolController(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	@PostMapping
	public SchoolResponse createSchool(@RequestBody SchoolRequest request) {
		return schoolService.createSchool(request);
	}

	@GetMapping
	public Page<SchoolResponse> getAllSchools(
			@RequestParam(required = false) String keyword,
			Pageable pageable) {
		return schoolService.getAllSchools(keyword, pageable);
	}

	@GetMapping("/{id}")
	public SchoolResponse getSchoolById(@PathVariable Long id) {
		return schoolService.getSchoolById(id);
	}

	@PutMapping("/{id}")
	public SchoolResponse updateSchool(@PathVariable Long id, @RequestBody SchoolUpdateRequest request) {
		return schoolService.updateSchool(id, request);
	}
	@DeleteMapping("/{id}")
	public void deleteSchool(@PathVariable Long id) {
		schoolService.deleteSchool(id);
	}

	@PatchMapping("/{id}/status")
	public SchoolResponse updateSchoolStatus(@PathVariable Long id, @RequestBody SchoolStatusRequest request) {
		return schoolService.updateSchoolStatus(id, request);
	}

	@GetMapping("/{id}/students")
	public List<StudentResponse> getStudents(@PathVariable Long id) {
		return schoolService.getStudents(id);
	}

	@GetMapping("/{id}/teachers")
	public List<TeacherResponse> getTeachers(@PathVariable Long id) {
		return schoolService.getTeachers(id);
	}

	@GetMapping("/{id}/analytics")
	public SchoolAnalyticsResponse getAnalytics(@PathVariable Long id) {
		return schoolService.getAnalytics(id);
	}

	@PostMapping("/{id}/impersonate")
	public ImpersonateResponse impersonateSchool(@PathVariable Long id) {
		return schoolService.impersonateSchool(id);
	}

}
