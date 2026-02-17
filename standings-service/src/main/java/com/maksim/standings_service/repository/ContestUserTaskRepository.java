package com.maksim.standings_service.repository;

import com.maksim.standings_service.entity.ContestUserTask;
import com.maksim.standings_service.entity.key.ContestUserTaskId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestUserTaskRepository extends JpaRepository<ContestUserTask, ContestUserTaskId> {
}
