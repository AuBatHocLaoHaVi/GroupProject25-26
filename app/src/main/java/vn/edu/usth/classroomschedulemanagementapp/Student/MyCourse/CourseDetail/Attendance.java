package vn.edu.usth.classroomschedulemanagementapp.Student.MyCourse.CourseDetail;

import com.google.gson.annotations.SerializedName;

public class Attendance {
    @SerializedName("status")
    private String status; // Present, Absent, etc.

    @SerializedName("checkInTime")
    private String checkInTime;

    @SerializedName("scheduleDate")
    private String scheduleDate;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    // Getters
    public String getStatus() { return status; }
    public String getCheckInTime() { return checkInTime; }
    public String getScheduleDate() { return scheduleDate; }
    public String getTimeRange() { return startTime + " - " + endTime; }
}