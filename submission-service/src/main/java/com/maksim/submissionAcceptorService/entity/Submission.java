package com.maksim.submissionAcceptorService.entity;

import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "problem_id")
    private Integer problemId;

    @Column(name = "contest_id")
    private Integer contestId;

    @Column(name = "is_upsolving")
    private Boolean isUpsolving;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "source", length = 50_000)
    private String source;

    @Column(name = "language")
    private ProgrammingLanguage programmingLanguage;

    @Column(name = "status")
    private Status status;

    @Column(name = "execution_time")
    private Integer executionTime;

    @Column(name = "used_memory")
    private Integer usedMemory;

    @Column(name = "test_num")
    private Integer testNum;

    @Column(name = "checker_message")
    private String checkerMessage;

}
