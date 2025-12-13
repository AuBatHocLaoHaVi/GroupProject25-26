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

    @SerializedName("isEnrolled")
    private boolean isEnrolled;

    public Subject(String id, String name, int credits, String lecturer, boolean isEnrolled) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.lecturer = lecturer;
        this.isEnrolled = isEnrolled;
    }

    public String getName() { return name; }
    public int getCredits() { return credits; }
    public String getLecturer() { return lecturer; }
    public String getId() { return id; }
    public boolean isEnrolled() { return isEnrolled; }
    public void setEnrolled(boolean enrolled) { isEnrolled = enrolled; }
}