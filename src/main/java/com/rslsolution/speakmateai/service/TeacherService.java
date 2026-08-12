package com.rslsolution.speakmateai.service;

import java.util.List;

import com.rslsolution.speakmateai.dto.request.TeacherRequest;
import com.rslsolution.speakmateai.dto.response.TeacherResponse;
import org.springframework.web.multipart.MultipartFile;
import com.rslsolution.speakmateai.dto.response.StudentImportResponse;

public interface TeacherService {

    List<TeacherResponse> getAllTeachers();

    TeacherResponse createTeacher(TeacherRequest request);

    TeacherResponse getTeacherById(Long id);

    TeacherResponse updateTeacher(Long id, TeacherRequest request);

    void deleteTeacher(Long id);

    void resetPassword(Long id, String newPassword);
}
