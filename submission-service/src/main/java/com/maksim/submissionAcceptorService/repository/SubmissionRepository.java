package com.maksim.submissionAcceptorService.repository;

import com.maksim.common.enums.Status;
import com.maksim.submissionAcceptorService.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.id = :id AND s.contestId = :contestId")
    Optional<Submission> findContestSubmissionById(Long id, Integer contestId);

    @Query("SELECT s FROM Submission s WHERE s.id = :id AND s.contestId IS NULL")
    Optional<Submission> findProblemSetSubmissionById(Long id);

    @Modifying
    @Query("""
            UPDATE Submission s
            SET s.status = :newStatus,
                s.executionTime = :executionTime,
                s.usedMemory = :usedMemory,
                s.testNum = :testNum,
                s.checkerMessage = :checkerMessage
            WHERE s.id = :submissionId
              AND s.status = :expectedStatus
            """)
    int updateFinalResultIfStatus(Long submissionId,
                                  Status newStatus,
                                  Integer executionTime,
                                  Integer usedMemory,
                                  Integer testNum,
                                  String checkerMessage,
                                  Status expectedStatus);

    @Query("SELECT s FROM Submission s " +
            "WHERE (:userId IS NULL OR s.userId = :userId) " +
            "AND (:problemId IS NULL OR s.problemId = :problemId) " +
            "AND (:contestId IS NULL OR s.contestId = :contestId) " +
            "AND (:status IS NULL OR s.status = :status)")
    Page<Submission> findAllFiltered(Integer contestId,
                                     Integer problemId,
                                     Integer userId,
                                     Status status,
                                     Pageable pageable);
}
