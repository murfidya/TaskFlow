package id.ac.binus.taskflow.models;

import com.google.firebase.Timestamp;

public class User {
    private String id;
    private String username;
    private String email;
    private String photoUrl;
    private boolean darkMode;
    private Timestamp createdAt;

    public User() {}

    public User(String id, String username, String email, String photoUrl, boolean darkMode, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.photoUrl = photoUrl;
        this.darkMode = darkMode;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
