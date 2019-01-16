package com.courseproject.movienightsapi.services;

import com.courseproject.movienightsapi.models.Google.GoogleUserProfile;
import com.courseproject.movienightsapi.models.Google.Tokens;
import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private Tokens tokens;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(GoogleUserProfile userProfile, GoogleTokenResponse tokenResponse) {
        User newUser = new User(userProfile);
        Tokens tokens = new Tokens(tokenResponse);
        newUser.setAccessToken(tokens.getAccessToken());
        newUser.setRefreshToken(tokens.getRefreshToken());
        newUser.setAccessTokenExpiresAt(tokens.getExpiresAt());
        addUserToDB(newUser);
    }

    private void addUserToDB(User user) {
        if (!userRepository.existsByUserId(user.getUserId())) {
            userRepository.save(user);
        }
    }

    public void updateAccessToken(User user, String accessToken, Long accessTokenExpiresAt) {
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setAccessToken(accessToken);
        userToUpdate.setAccessTokenExpiresAt(accessTokenExpiresAt);
        userRepository.save(userToUpdate);
    }
}
