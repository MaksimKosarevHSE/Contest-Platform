package com.maksim.auth_service.controller;


import com.maksim.auth_service.service.HandleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HandlesController {
    private final HandleService handleService;

    @GetMapping("/users/{userId}/hanlde")
    public String getUserHandle(@PathVariable Integer userId) {
        return handleService.getUserHandle(userId);
    }

    @GetMapping("/users/handles")
    public ResponseEntity<?> getUserHandles(@RequestParam("ids") List<Integer> ids) {
        return ResponseEntity.ok(handleService.getUsersHandles(ids));
    }
}
