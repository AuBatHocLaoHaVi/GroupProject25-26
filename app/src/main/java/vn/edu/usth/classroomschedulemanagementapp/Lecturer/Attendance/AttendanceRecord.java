package vn.edu.usth.classroomschedulemanagementapp.Lecturer.Attendance;

public class AttendanceRecord {
    private String id;
    private String scheduleId;
    private String studentId;
    private String studentCode;
    private String studentName; // Dùng để hiển thị UI
    private String status;      // "Present" hoặc "Absent"
    private long checkInTime;

    public AttendanceRecord(String id, String scheduleId, String studentId, String studentCode, String studentName, String status, long checkInTime) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.studentName = studentName;
        this.status = status;
        this.checkInTime = checkInTime;
    }

    // Getters và Setters
    public String getScheduleId() { return scheduleId; }
    public String getStudentName() { return studentName; }
    public String getStudentId() { return studentId; }
    public String getStudentCode() { return studentCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}