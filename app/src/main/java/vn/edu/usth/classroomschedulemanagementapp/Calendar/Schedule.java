package vn.edu.usth.classroomschedulemanagementapp.Calendar;

public class Schedule {

    String id;
    String subject;
    String time;
    String room;
    String lecturer;
    String date;

    public Schedule(String id, String subject, String time, String room, String lecturer, String date) {
        this.id = id;
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.lecturer = lecturer;
        this.date = date;
    }
    public String getId() { return id; }
    public String getDate() { return date; }
}