package com.maksim.problemService.entity;

import com.maksim.problemService.entity.keys.ContestUserId;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "contest_user")
@Entity
@Data
public class ContestUser {
    @EmbeddedId
    private ContestUserId id;
    @ManyToOne
    @MapsId("contestId")
    @JoinColumn(name = "contest_id")
    private Contest contest;
}
