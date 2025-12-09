package vn.edu.usth.classroomschedulemanagementapp;

import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("credits")
    private int credits;

    @SerializedName("faculty")
    private String faculty;

    // Constructor
    public Subject(String id, String name, int credits, String faculty) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.faculty = faculty;
    }

    // Getters
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public String getFaculty() { return faculty; }
    public String getId() { return id; }
}