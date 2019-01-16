package com.courseproject.movienightsapi.models.Google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public class GoogleUserProfile {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String locale;
    private String picture;
    private Boolean emailVerified;


    public GoogleUserProfile(String userId, String email, String firstName, String lastName, String locale, String picture, Boolean emailVerified) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.locale = locale;
        this.picture = picture;
        this.emailVerified = emailVerified;
    }

//    public GoogleUserProfile(GoogleIdToken.Payload payload) {
//        this.userId = payload.getSubject();
//        this.email = payload.getEmail();
//        this.firstName = (String)payload.get("given_name");
//        this.lastName = (String) payload.get("family_name");
//        this.locale = (String) payload.get("locale");
//        this.picture = (String) payload.get("picture");
//        this.emailVerified = Boolean.valueOf(payload.getEmailVerified());
//    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLocale() {
        return locale;
    }

    public String getPicture() {
        return picture;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }
}
