package vn.edu.usth.classroomschedulemanagementapp.Student.Account;

import com.google.gson.annotations.SerializedName;

public class StudentGrade {
    @SerializedName("subjectName") private String subjectName;
    @SerializedName("gradeItemName") private String gradeItemName; // Ví dụ: Midterm, Final
    @SerializedName("score") private Double score;
    @SerializedName("weight") private Double weight;

    public String getSubjectName() { return subjectName; }
    public String getGradeItemName() { return gradeItemName; }
    public Double getScore() { return score; }
    public String getWeight() { return (weight * 100) + "%"; }
}