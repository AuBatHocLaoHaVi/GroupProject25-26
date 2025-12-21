package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

public class Class {
    private String id;
    private String name;
    private String room;
    private String time;
    private String date;

    public Class(String id, String name, String room, String time, String date) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.time = time;
        this.date = date;
    }

    // Getters
    public String getName() { return name; }
    public String getRoom() { return room; }
    public String getTime() { return time; }
    public String getDate() { return date; }
}