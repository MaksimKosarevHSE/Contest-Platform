package com.maksim.standings_service.entity;

import com.maksim.standings_service.entity.key.ContestUserId;
import com.maksim.standings_service.entity.key.ContestUserTaskId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "contest_user_task")
@Data
public class ContestUserTask {
    @EmbeddedId
    private ContestUserTaskId id;
    @MapsId("contestId")
    private int contestId;
    @MapsId("userId")
    private int userId;
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private int taskId;
    private Boolean isSolved;
    private int attempts;
    private int score;
    private int fine;
    private LocalDateTime solutionTime;

    public ContestUserTask(ContestUserTaskId id) {
        this.id = id;
        this.isSolved = false;
    }

    public void addFine(int x){
        fine += x;
    }

}
