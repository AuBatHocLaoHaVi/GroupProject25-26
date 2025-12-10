package vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse;

import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("credits")
    private int credits;

    @SerializedName("lecturer")
    private String lecturer;

    // Constructor
    public Subject(String id, String name, int credits, String lecturer) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.lecturer = lecturer;
    }

    // Getters
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public String getLecturer() { return lecturer; }
    public String getId() { return id; }
}