package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUserToDB(User user) {
        if (!userRepository.existsByUserId(user.getUserId())) {
            userRepository.save(user);
        }
    }
}
