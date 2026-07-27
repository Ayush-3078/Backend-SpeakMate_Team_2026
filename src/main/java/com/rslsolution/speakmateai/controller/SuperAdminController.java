package com.rslsolution.speakmateai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rslsolution.speakmateai.dto.response.AdminAiUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminSchoolGrowthResponse;
import com.rslsolution.speakmateai.dto.response.AdminUsageResponse;
import com.rslsolution.speakmateai.dto.response.AdminUserGrowthResponse;
import com.rslsolution.speakmateai.dto.response.SuperAdminDashboardResponse;
import com.rslsolution.speakmateai.service.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class SuperAdminController {

	private final AdminService adminService;

	public SuperAdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/dashboard")
	public SuperAdminDashboardResponse getDashboard() {
		return adminService.getSuperAdminDashboard();
	}

	@GetMapping("/dashboard/user-growth")
	public AdminUserGrowthResponse getUserGrowth() {
		return adminService.getUserGrowth();
	}

	@GetMapping("/dashboard/school-growth")
	public AdminSchoolGrowthResponse getSchoolGrowth() {
		return adminService.getSchoolGrowth();
	}

	@GetMapping("/dashboard/usage")
	public AdminUsageResponse getUsage() {
		return adminService.getUsage();
	}

	@GetMapping("/dashboard/ai-usage")
	public AdminAiUsageResponse getAiUsage() {
		return adminService.getAiUsage();
	}

}
