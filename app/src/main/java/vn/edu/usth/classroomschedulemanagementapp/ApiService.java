package vn.edu.usth.classroomschedulemanagementapp;

import retrofit2.Call;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.usth.classroomschedulemanagementapp.Login.LoginRequest;
import vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse.Subject;

public interface ApiService {
    // đường dẫn phải khớp với đường dẫn trong server.js( very quan trọng!!!)
    @POST("/api/login")
    Call<User> login(@Body LoginRequest request);

    @GET("/api/subjects")
    Call<List<Subject>> getSubjects();

    @POST("/api/enroll-auto")
    Call<Void> enrollAuto(@Body EnrollRequest request);

    @GET("/api/my-courses/{userId}")
    Call<List<Subject>> getMyCourses(@Path("userId") String userId);
}