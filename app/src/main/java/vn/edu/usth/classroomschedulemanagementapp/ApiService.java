package vn.edu.usth.classroomschedulemanagementapp;

import retrofit2.Call;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.usth.classroomschedulemanagementapp.Calendar.ScheduleResponse;
import vn.edu.usth.classroomschedulemanagementapp.Login.LoginRequest;
import vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse.Subject;
import vn.edu.usth.classroomschedulemanagementapp.Student.MyCourse.CourseDetail.Attendance;
import vn.edu.usth.classroomschedulemanagementapp.Student.Account.UserProfile;
import vn.edu.usth.classroomschedulemanagementapp.Student.Account.StudentGrade;

public interface ApiService {
    @POST("/api/login")
    Call<User> login(@Body LoginRequest request);

    @GET("/api/profile/{userId}")
    Call<UserProfile> getProfile(@Path("userId") String userId);

    @GET("/api/grades/{studentId}")
    Call<List<StudentGrade>> getGrades(@Path("studentId") String studentId);

    // --- SỬA DÒNG NÀY ---
    // Gọi API subject có truyền userId để check trạng thái enroll
    @GET("/api/subjects")
    Call<List<Subject>> getSubjects(@Query("userId") String userId);

    @POST("/api/enroll-auto")
    Call<Void> enrollAuto(@Body EnrollRequest request);

    @GET("/api/my-courses/{userId}")
    Call<List<Subject>> getMyCourses(@Path("userId") String userId);

    @GET("/api/schedule/{userId}")
    Call<List<ScheduleResponse>> getStudentSchedule(@Path("userId") String userId);

    @GET("/api/attendance")
    Call<List<Attendance>> getAttendance(@Query("classId") String classId, @Query("studentId") String studentId);
}