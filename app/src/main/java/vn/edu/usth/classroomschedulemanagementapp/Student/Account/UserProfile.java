package vn.edu.usth.classroomschedulemanagementapp.Student.Account;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("fullName") private String fullName;
    @SerializedName("studentCode") private String studentCode;
    @SerializedName("email") private String email;
    @SerializedName("major") private String major;
    @SerializedName("cohort") private String cohort;

    public String getFullName() { return fullName; }
    public String getStudentCode() { return studentCode; }
    public String getEmail() { return email; }
    public String getMajor() { return major; }
    public String getCohort() { return cohort; }
}