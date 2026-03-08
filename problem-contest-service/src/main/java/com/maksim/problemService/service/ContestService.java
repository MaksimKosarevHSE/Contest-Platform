package com.maksim.problemService.service;

import com.maksim.problemService.dto.contest.ContestSignatureDto;
import com.maksim.problemService.dto.contest.CreateContestDto;
import com.maksim.problemService.dto.mapper.ContestMapper;
import com.maksim.problemService.dto.mapper.ProblemMapper;
import com.maksim.problemService.dto.problem.ProblemSignature;
import com.maksim.problemService.entity.Contest;
import com.maksim.problemService.entity.ContestProblem;
import com.maksim.problemService.entity.Problem;
import com.maksim.problemService.entity.ProblemConstraints;
import com.maksim.problemService.exception.ResourceNotFoundException;
import com.maksim.problemService.repository.ContestRepository;
import com.maksim.problemService.repository.ProblemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class ContestService {
    private final ProblemRepository problemRepository;

    private final ContestRepository contestRepository;

    private final ContestMapper contestMapper;

    private final AuthServiceClient authServiceClient;

    private final ProblemMapper problemMapper;

    public List<ProblemSignature> getAllProblemSignatures(Integer contestId) {
        var contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new ResourceNotFoundException("No contest found with id " + contestId));
        return contest.getProblems().stream().map(problemMapper::toProblemSignature).toList();
    }

    public Problem getProblem(Integer contestId, Integer problemId) {
        return contestRepository.getProblem(contestId, problemId)
                .orElseThrow(() -> new ResourceNotFoundException("No problem found with id " + problemId));
    }


    public Page<ContestSignatureDto> getPublicContests(Integer page, Integer pageSize) {
        Page<ContestSignatureDto> dto = contestRepository.getAll(PageRequest.of(page - 1, pageSize));
        setHandles(dto.getContent());
        return dto;
    }


    public Page<ContestSignatureDto> getUserContests(int userId, Integer page, Integer pageSize) {
        Page<ContestSignatureDto> dtoList = contestRepository.getUserContests(userId, PageRequest.of(page - 1, pageSize));
        setHandles(dtoList.getContent());
        return dtoList;
    }

    private void setHandles(List<ContestSignatureDto> contests) {
        List<Integer> authorIds = contests.stream()
                .map(ContestSignatureDto::getAuthorId)
                .distinct()
                .toList();
        Map<Integer, String> handles = authServiceClient.getUsersHandles(authorIds);
        contests.forEach(dto -> dto.setAuthorHandle(handles.get(dto.getAuthorId())));
    }


    public int createContest(CreateContestDto dto, int userId) {
        List<Integer> distinctProblemsId = dto.getProblemsId().stream().distinct().toList();
        // пересекаем то что хочет юзер добавить в контест и то, что он может добавить
        List<Problem> userProblemIntersection = contestRepository.getAuthorProblemsList(userId, distinctProblemsId);

        if (userProblemIntersection.size() != distinctProblemsId.size()) {
            // ограничения маленькие, О(n^2)
            Problem tmp = new Problem();
            StringBuilder message = new StringBuilder("No problems found with ids: [");
            for (var taskId : distinctProblemsId) {
                tmp.setId(taskId);
                if (!userProblemIntersection.contains(tmp)) message.append(taskId).append(", ");
            }
            message.setLength(message.length() - 2);
            message.append("]");
            throw new ResourceNotFoundException(message.toString());
        }

        dto.setProblemsId(distinctProblemsId);

        Contest contest = contestMapper.toEntity(dto);
        contest.setAuthorId(userId);

        return contestRepository.save(contest).getId();
    }


    public ProblemConstraints getConstraints(Integer contestId, Integer problemId) {
        return contestRepository.getProblemConstraints(contestId, problemId)
                .orElseThrow(() -> new ResourceNotFoundException("No problem found with id " + problemId));
    }
}
