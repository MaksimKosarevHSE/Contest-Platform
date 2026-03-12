package com.maksim.testingService.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, unique = true)
    private String messageId;

    @Column
    private int userId;

    public ProcessedEvent(String messageId, int userId) {
        this.messageId = messageId;
        this.userId = userId;
    }

}
