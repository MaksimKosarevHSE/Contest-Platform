package com.maksim.auth_service.service;

import com.maksim.auth_service.entity.User;
import com.maksim.auth_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HandleService {
    private final UserRepository userRepository;

    public String getUserHandle(Integer userId) {
        return userRepository.findById(userId).map(User::getHandle).orElse(null);
    }

    public Map<Integer, String> getUsersHandles(List<Integer> ids) {
        var list = userRepository.findByIdIn(ids);
        return list.stream().collect(Collectors.toMap(User::getId, User::getHandle));
    }
}
