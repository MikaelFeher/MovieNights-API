package com.courseproject.movienightsapi.models.users;

import com.courseproject.movienightsapi.models.calendars.CalendarEventsList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
    @Id
    private String id;
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String locale;
    private String picture;
    private Boolean emailVerified;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresAt;
    private CalendarEventsList calendarEventsList;

    public User(String userId, String email, String firstName, String lastName, String locale, String picture, Boolean emailVerified) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.locale = locale;
        this.picture = picture;
        this.emailVerified = emailVerified;
    }

    public String getId() {
        return id;
    }

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

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public CalendarEventsList getCalendarEventsList() {
        return calendarEventsList;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessTokenExpiresAt(Long accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public void setCalendarEventsList(CalendarEventsList calendarEventsList) {
        this.calendarEventsList = calendarEventsList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", locale='" + locale + '\'' +
                ", picture='" + picture + '\'' +
                ", emailVerified=" + emailVerified +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessTokenExpiresAt=" + accessTokenExpiresAt +
                ", calendarEventsList=" + calendarEventsList +
                '}';
    }
}
