package vn.edu.usth.classroomschedulemanagementapp;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("role")
    private String role;



    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}