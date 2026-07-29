package com.rslsolution.speakmateai.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.rslsolution.speakmateai.dto.request.StudentRequest;
import com.rslsolution.speakmateai.dto.response.StudentImportResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;
import com.rslsolution.speakmateai.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/school/students")
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SCHOOL_ADMIN')")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public List<StudentResponse> getAllStudents() {
		return studentService.getAllStudents();
	}

	@GetMapping("/{id}")
	public StudentResponse getStudentById(@PathVariable Long id) {
		return studentService.getStudentById(id);
	}

	@PostMapping
	public StudentResponse createStudent(@RequestBody StudentRequest request) {
		return studentService.createStudent(request);
	}

	@PutMapping("/{id}")
	public StudentResponse updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
		return studentService.updateStudent(id, request);
	}

	@DeleteMapping("/{id}")
	public void deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
	}

	@PostMapping("/import")
	public StudentImportResponse importStudents(@RequestBody List<StudentRequest> students) {
		return studentService.importStudents(students);
	}

	@GetMapping("/export")
	public void exportStudents(@RequestParam String format, HttpServletResponse response) throws IOException {
		byte[] data = studentService.exportStudents(format);

		String contentType = "csv".equalsIgnoreCase(format) ? "text/csv" : "application/vnd.ms-excel";
		String fileName = "students." + format.toLowerCase();

		response.setContentType(contentType);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
		response.getOutputStream().write(data);
		response.getOutputStream().flush();
	}

	@PostMapping("/{id}/reset-password")
	public Map<String, String> resetStudentPassword(@PathVariable Long id) {
		return studentService.resetStudentPassword(id);
	}
}
