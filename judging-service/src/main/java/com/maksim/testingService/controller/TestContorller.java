package com.maksim.testingService.controller;

import com.maksim.testingService.enums.Status;
import com.maksim.testingService.event.JudgingProgress;
import com.maksim.testingService.event.SolutionJudgedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContorller {
    @Autowired
    private ReactiveRedisTemplate<String, JudgingProgress> redisTemplate;

    @GetMapping("/test")
    public String test() {
        redisTemplate.opsForValue().set("sub:1234", new JudgingProgress(), 3).doOnSuccess(result -> System.out.println("SUCCESS")).subscribe();
        return "hi";
//        return "redirect:/notify.html";
    }

}
