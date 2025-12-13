package vn.edu.usth.classroomschedulemanagementapp.Calendar;

public class Schedule {
    String subject;
    String time;
    String room;
    String lecturer;

    public Schedule(String subject, String time, String room, String lecturer) {
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.lecturer = lecturer;
    }
}