package com.maksim.standings_service.entity.key;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestUserId implements Serializable {
    @Column(name = "contest_id")
    private int contestId;
    @Column(name = "user_id")
    private int userId;

}
