package vn.edu.usth.classroomschedulemanagementapp;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("role")
    private String role;

    @SerializedName("studentCode")
    private String studentCode;

    // Getter
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
}