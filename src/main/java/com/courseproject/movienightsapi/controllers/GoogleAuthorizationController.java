package com.courseproject.movienightsapi.controllers;

import com.courseproject.movienightsapi.models.users.User;
import com.courseproject.movienightsapi.repositories.UserRepository;
import com.courseproject.movienightsapi.services.GoogleService;
import com.courseproject.movienightsapi.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class GoogleAuthorizationController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/storeauthcode", method = RequestMethod.POST)
    public HttpStatus storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return HttpStatus.UNAUTHORIZED;
        }

        // Get GoogleTokenResponse...
        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = getGoogleToken(code);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get profile info from ID token (Obtained at the last step of OAuth2)
        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Create a user, assign tokens and save to db...
        userService.createUser(payload, tokenResponse);

        return HttpStatus.OK;
    }

    @GetMapping("/refreshtoken/{id}")
    public ResponseEntity<?> refreshToken(@PathVariable String id) throws IOException {
        User user = userRepository.findById(id).get();
        googleService.refreshToken(user);
        return new ResponseEntity<>(userRepository.findById(id).get(), HttpStatus.OK);
    }

    private GoogleTokenResponse getGoogleToken(String code) throws IOException {
        return new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://www.googleapis.com/oauth2/v4/token",
                CLIENT_ID,
                CLIENT_SECRET,
                code,
                "http://localhost:8080")
                .execute();
    }
}
