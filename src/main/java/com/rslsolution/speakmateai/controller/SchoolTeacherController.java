package com.rslsolution.speakmateai.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rslsolution.speakmateai.dto.request.SchoolTeacherRequest;
import com.rslsolution.speakmateai.dto.response.SchoolTeacherResponse;
import com.rslsolution.speakmateai.service.SchoolTeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/school/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SCHOOL_ADMIN')")
public class SchoolTeacherController {

	private final SchoolTeacherService schoolTeacherService;

	@GetMapping
	public ResponseEntity<List<SchoolTeacherResponse>> getAllTeachers() {
		return ResponseEntity.ok(schoolTeacherService.getAllTeachers());
	}

	@GetMapping("/search")
	public ResponseEntity<List<SchoolTeacherResponse>> searchTeachers(@RequestParam String q) {
		return ResponseEntity.ok(schoolTeacherService.searchTeachers(q));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SchoolTeacherResponse> getTeacherById(@PathVariable Long id) {
		return ResponseEntity.ok(schoolTeacherService.getTeacherById(id));
	}

	@PostMapping
	public ResponseEntity<SchoolTeacherResponse> createTeacher(@Valid @RequestBody SchoolTeacherRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(schoolTeacherService.createTeacher(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SchoolTeacherResponse> updateTeacher(@PathVariable Long id, @Valid @RequestBody SchoolTeacherRequest request) {
		return ResponseEntity.ok(schoolTeacherService.updateTeacher(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deactivateTeacher(@PathVariable Long id) {
		schoolTeacherService.deactivateTeacher(id);
		return ResponseEntity.noContent().build();
	}
}
