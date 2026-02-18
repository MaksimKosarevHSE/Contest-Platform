package com.maksim.standings_service.repository;

import com.maksim.standings_service.entity.ContestUserTask;
import com.maksim.standings_service.entity.key.ContestUserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestUserTaskRepository extends JpaRepository<ContestUserTask, ContestUserTaskId> {

    List<ContestUserTask> getUserStatistics
}
