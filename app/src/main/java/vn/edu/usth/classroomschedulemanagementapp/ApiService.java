package vn.edu.usth.classroomschedulemanagementapp;

import retrofit2.Call;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
public interface ApiService {
    // Đường dẫn này phải khớp với đường dẫn trong server.js
    @POST("/api/login")
    Call<User> login(@Body LoginRequest request);

    @GET("/api/subjects")
    Call<List<Subject>> getSubjects();
}