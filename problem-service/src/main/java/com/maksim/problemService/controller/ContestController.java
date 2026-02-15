package com.maksim.problemService.controller;

import com.maksim.problemService.dto.*;
import com.maksim.problemService.entity.Contest;
import com.maksim.problemService.entity.Problem;
import com.maksim.problemService.entity.ProblemConstraints;
import com.maksim.problemService.service.ContestService;
import com.maksim.problemService.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class ContestController {
    private ContestService contestService;
    private ProblemService problemService;

    private Integer PAGE_SIZE = 20;

    public ContestController(ProblemService service, ContestService contestService) {
        this.contestService = contestService;
        this.problemService = service;
    }

    // сигнатуры задач
    @GetMapping("/contest/{contestId}/problems")
    public ResponseEntity<Object> getSignatures(@PathVariable Integer contestId) {
        List<ProblemSignature> constraints = contestService.getAllProblemSignatures(contestId);
        return ResponseEntity.ok(constraints);
    }

    // полное описание заадачи
    @GetMapping("/contest/{contestId}/problem/{problemId}")
    public ResponseEntity<Object> getProblemById(@PathVariable Integer contestId, Integer problemId) {
        Problem problem = contestService.getProblem(contestId, problemId);
        return ResponseEntity.ok(problem);
    }

    //все публичные контесты
    @GetMapping("/contests")
    public ResponseEntity<Object> getAllPublicContests(@RequestParam(defaultValue = "1") Integer page) {
        List<ContestSignatureDto> list = contestService.getPublicContests(page, PAGE_SIZE);
        return ResponseEntity.ok().build();
    }

    // все котесты в которых участвовал / зарегестрирован / участвует текущий пользователь
    @GetMapping("/contests/my")
    public ResponseEntity<Object> getUserContests(@RequestParam(defaultValue = "1") Integer page) {
        int userId = 1;
        List<ContestSignatureDto> list = contestService.getUserContests(userId, page, PAGE_SIZE);
        return ResponseEntity.ok(list);
    }

    // Вот это скорее в другой сервис нужно, но пока что так
    @PostMapping("/contest/create")
    public ResponseEntity<Object> createContest(@ModelAttribute CreateContestDto dto) {
        int userId = 1;
        int id = contestService.createContest(dto, userId);
        return ResponseEntity.ok(id);
    }


    @GetMapping("contest/{contestId}/problem/{problemId}/constraints")
    public ResponseEntity<Object> getConstraintsById(@PathVariable Integer contestId, @PathVariable Integer problemId) {
        ProblemConstraints constraints = contestService.getConstraints(contestId, problemId);
        if (constraints == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No problem found with id " + problemId));
        }
        return ResponseEntity.ok(constraints);
    }


    //    // сабмит решения  TODO: перенести в сабмишн сервис
//    @PostMapping("/contest/{contestId}/problem/{problemId}/submit")
//    public ResponseEntity<Object> submitSolution() {
//        return ResponseEntity.ok().build();
//    }


}

