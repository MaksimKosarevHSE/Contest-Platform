package com.maksim.testingService.service.model;

import com.maksim.testingService.enums.Status;
import jdk.jfr.DataAmount;
import lombok.*;


@Data
@ToString
public class VerdictInfo {
    private Status status;
    private Integer executionTime;
    private Integer usedMemory;
    private Integer testNum;
    private String checkerMessage;
}
