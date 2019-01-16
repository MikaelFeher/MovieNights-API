package com.courseproject.movienightsapi.models.Google;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

public class Tokens {
    private String accessToken;
    private String refreshToken;
    private Long expiresAt;

    public Tokens(GoogleTokenResponse tokenResponse) {
        this.accessToken = tokenResponse.getAccessToken();
        this.refreshToken = tokenResponse.getRefreshToken();
        this.expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }
}
