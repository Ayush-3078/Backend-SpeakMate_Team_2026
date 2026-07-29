package com.rslsolution.speakmateai.service;

import java.util.List;

import com.rslsolution.speakmateai.dto.request.StudentRequest;
import com.rslsolution.speakmateai.dto.response.StudentImportResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;

public interface StudentService {

	List<StudentResponse> getAllStudents();

	StudentResponse getStudentById(Long id);

	StudentResponse createStudent(StudentRequest request);

	StudentResponse updateStudent(Long id, StudentRequest request);

	void deleteStudent(Long id);

	StudentImportResponse importStudents(java.util.List<StudentRequest> students);

	byte[] exportStudents(String format);

	java.util.Map<String, String> resetStudentPassword(Long id);
}
