package vn.edu.usth.classroomschedulemanagementapp;

public class EnrollRequest {
    private String userId;
    private String subjectId;

    public EnrollRequest(String userId, String subjectId) {
        this.userId = userId;
        this.subjectId = subjectId;
    }
}