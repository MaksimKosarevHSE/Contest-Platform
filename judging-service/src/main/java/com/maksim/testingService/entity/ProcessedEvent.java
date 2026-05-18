package com.maksim.testingService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id
    @Column(name = "submission_id")
    private Long submissionId;

    public ProcessedEvent(Long submissionId) {
        this.submissionId = submissionId;
    }

}
