package com.rslsolution.speakmateai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rslsolution.speakmateai.dto.response.ActivityResponse;
import com.rslsolution.speakmateai.dto.response.LeaderboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolProgressResponse;
import com.rslsolution.speakmateai.dto.response.SchoolDashboardResponse;
import com.rslsolution.speakmateai.dto.response.SkillScoresResponse;
import com.rslsolution.speakmateai.service.SchoolDashboardService;

@RestController
@RequestMapping("/api/v1/school/dashboard")
@PreAuthorize("hasRole('SCHOOL_ADMIN')")
public class SchoolDashboardController {

	private final SchoolDashboardService schoolDashboardService;

	public SchoolDashboardController(SchoolDashboardService schoolDashboardService) {
		this.schoolDashboardService = schoolDashboardService;
	}

	@GetMapping
	public SchoolDashboardResponse getSchoolDashboard() {
		return schoolDashboardService.getSchoolDashboard();
	}

	@GetMapping("/activity")
	public ActivityResponse getActivity() {
		return schoolDashboardService.getActivity();
	}

	@GetMapping("/progress")
	public SchoolProgressResponse getProgress() {
		return schoolDashboardService.getProgress();
	}

	@GetMapping("/skill-scores")
	public SkillScoresResponse getSkillScores() {
		return schoolDashboardService.getSkillScores();
	}

	@GetMapping("/leaderboard")
	public LeaderboardResponse getLeaderboard() {
		return schoolDashboardService.getLeaderboard();
	}

}
