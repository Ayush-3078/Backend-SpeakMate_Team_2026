package com.rslsolution.speakmateai.service;

import java.util.List;

import com.rslsolution.speakmateai.dto.response.AdminAiUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminDashboardResponse;
import com.rslsolution.speakmateai.dto.response.AdminSchoolGrowthResponse;
import com.rslsolution.speakmateai.dto.response.AdminUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminUserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.SuperAdminDashboardResponse;
import com.rslsolution.speakmateai.dto.response.UserResponse;

public interface AdminService {

	// Dashboard
	AdminDashboardResponse getDashboard();

	SuperAdminDashboardResponse getSuperAdminDashboard();

	AdminUserGrowthResponse getUserGrowth();

	AdminSchoolGrowthResponse getSchoolGrowth();

	AdminUsageResponse getUsage();

	AdminAiUsageResponse getAiUsage();

	// User Management
	List<UserResponse> getAllUsers();

	UserResponse getUserById(Long id);

	UserResponse activateUser(Long id);

	UserResponse deactivateUser(Long id);

}