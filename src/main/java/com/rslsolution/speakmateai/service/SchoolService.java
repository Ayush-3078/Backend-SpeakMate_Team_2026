package com.rslsolution.speakmateai.service;

import com.rslsolution.speakmateai.dto.request.SchoolRequest;
import com.rslsolution.speakmateai.dto.response.SchoolResponse;

import java.util.List;

public interface SchoolService {
    SchoolResponse createSchool(SchoolRequest request);
    List<SchoolResponse> getAllSchools();
    SchoolResponse getSchoolById(Long id);
}
