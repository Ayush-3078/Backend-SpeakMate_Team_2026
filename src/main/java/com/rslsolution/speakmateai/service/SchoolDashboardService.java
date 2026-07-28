package com.rslsolution.speakmateai.service;

import com.rslsolution.speakmateai.dto.response.ActivityResponse;
import com.rslsolution.speakmateai.dto.response.LeaderboardResponse;
import com.rslsolution.speakmateai.dto.response.SchoolProgressResponse;
import com.rslsolution.speakmateai.dto.response.SchoolDashboardResponse;
import com.rslsolution.speakmateai.dto.response.SkillScoresResponse;

public interface SchoolDashboardService {

	SchoolDashboardResponse getSchoolDashboard();

	ActivityResponse getActivity();

	SchoolProgressResponse getProgress();

	SkillScoresResponse getSkillScores();

	LeaderboardResponse getLeaderboard();

}
