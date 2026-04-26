package com.maksim.problemService.service;

import com.maksim.problemService.dto.PageResponseDto;
import com.maksim.problemService.dto.standings.UserProgressResponseDto;
import com.maksim.problemService.event.StandingsUpdateEvent;

public interface StandingsService {
    void handleUpdateEvent(StandingsUpdateEvent event);

    PageResponseDto<UserProgressResponseDto> getLeaderboard(Integer contestId, Integer page, Integer pageSize);

    UserProgressResponseDto getUserStandings(Integer contestId, Integer userId);
}
