package com.maksim.submissionAcceptorService.repository;

import com.maksim.submissionAcceptorService.dto.SubmissionResponseDto;
import com.maksim.submissionAcceptorService.enums.ProgrammingLanguage;
import com.maksim.submissionAcceptorService.enums.Status;
import com.maksim.submissionAcceptorService.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s " +
            "WHERE (:userId IS NULL OR s.userId = :userId) " +
            "AND (:problemId IS NULL OR s.problemId = :problemId) " +
            "AND (:contestId IS NULL OR s.contestId = :contestId) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (:language IS NULL OR s.programmingLanguage = :language) " +
            "ORDER BY s.id DESC")
    Page<Submission> findFiltered(@Param("contestId") Integer contestId,
                                  @Param("problemId") Integer problemId,
                                  @Param("userId") Integer userId,
                                  @Param("status") Status status,
                                  @Param("language") ProgrammingLanguage language,
                                  Pageable pageable);

    Optional<Submission> findByIdAndContestId(long id, Integer contestId);
}
