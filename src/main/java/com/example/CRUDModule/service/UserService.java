package com.example.CRUDModule.service;


import com.example.CRUDModule.dto.UserRequest;
import com.example.CRUDModule.entity.User;
import com.example.CRUDModule.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User createUser(UserRequest userRequest) {
        User user = User.builder()
                .name(userRequest.getName())
                .build();
        userRepo.save(user);

        return user;
    }
}
