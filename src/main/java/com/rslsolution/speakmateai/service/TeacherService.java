package com.rslsolution.speakmateai.service;

import com.rslsolution.speakmateai.dto.response.TeacherAnalyticsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherDashboardResponse;
import com.rslsolution.speakmateai.dto.response.TeacherProfileResponse;
import com.rslsolution.speakmateai.dto.response.TeacherReportsResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentDetailResponse;
import com.rslsolution.speakmateai.dto.response.TeacherStudentsListResponse;
import com.rslsolution.speakmateai.enums.Status;

public interface TeacherService {

    TeacherDashboardResponse getTeacherDashboard();

    TeacherStudentsListResponse getStudents(String search, Status status);

    TeacherStudentDetailResponse getStudentDetail(Long studentId);

    TeacherAnalyticsResponse getAnalytics();

    TeacherReportsResponse getReports();

    TeacherProfileResponse getProfile();
}
