package com.example.cheersfor.models;

import java.util.List;

public class User {

    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private int currentCompanyId;
    private List<Integer> previousCompanyIds;
    private int points;
    private String linkedinUrl;

    public int getId() {
        return id;
    }

    public User(int id, String name, String email, String phoneNumber, String profilePicture, int currentCompanyId, List<Integer> previousCompanyIds, int points, String linkedinUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.currentCompanyId = currentCompanyId;
        this.previousCompanyIds = previousCompanyIds;
        this.points = points;
        this.linkedinUrl = linkedinUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getCurrentCompanyId() {
        return currentCompanyId;
    }

    public void setCurrentCompanyId(int currentCompanyId) {
        this.currentCompanyId = currentCompanyId;
    }

    public List<Integer> getPreviousCompanyIds() {
        return previousCompanyIds;
    }

    public void setPreviousCompanyIds(List<Integer> previousCompanyIds) {
        this.previousCompanyIds = previousCompanyIds;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }
}
