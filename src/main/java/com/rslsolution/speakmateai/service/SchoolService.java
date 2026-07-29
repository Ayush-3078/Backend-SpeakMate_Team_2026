package com.rslsolution.speakmateai.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rslsolution.speakmateai.dto.request.SchoolRequest;
import com.rslsolution.speakmateai.dto.request.SchoolStatusRequest;
import com.rslsolution.speakmateai.dto.request.SchoolUpdateRequest;
import com.rslsolution.speakmateai.dto.response.ImpersonateResponse;
import com.rslsolution.speakmateai.dto.response.SchoolAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.SchoolResponse;
import com.rslsolution.speakmateai.dto.response.StudentResponse;
import com.rslsolution.speakmateai.dto.response.TeacherResponse;

public interface SchoolService {

	SchoolResponse createSchool(SchoolRequest request);

	Page<SchoolResponse> getAllSchools(String keyword, org.springframework.data.domain.Pageable pageable);

	SchoolResponse getSchoolById(Long id);

	SchoolResponse updateSchool(Long id, SchoolUpdateRequest request);

	void deleteSchool(Long id);

	SchoolResponse updateSchoolStatus(Long id, SchoolStatusRequest request);

	List<StudentResponse> getStudents(Long schoolId);

	List<TeacherResponse> getTeachers(Long schoolId);

	SchoolAnalyticsResponse getAnalytics(Long schoolId);

	ImpersonateResponse impersonateSchool(Long schoolId);

}
