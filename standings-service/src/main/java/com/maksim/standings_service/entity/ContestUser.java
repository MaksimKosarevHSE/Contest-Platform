package com.maksim.standings_service.entity;


import com.maksim.standings_service.entity.key.ContestUserId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contest_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContestUser {
    @EmbeddedId
    private ContestUserId id;
    @MapsId("contestId")
    @JoinColumn(name = "contest_id")
    private int contestId;
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private int userId;
    private int totalScore;
    private int taskSolved;

    public ContestUser(ContestUserId cuId) {
        this.id = cuId;
    }

    public void addScore(int x){
        totalScore += x;
    }
    public void incrementTaskSolved(){
        taskSolved++;
    }
}

