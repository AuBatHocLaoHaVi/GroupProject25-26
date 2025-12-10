package vn.edu.usth.classroomschedulemanagementapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // dùng máy ảo thì dùng dòng này
    //private static final String BASE_URL = "http://10.0.2.2:3000/";

    // chạy trên dt thì thay ip máy vào đây
    private static final String BASE_URL = "http://192.168.100.3:3000/";

    private static Retrofit retrofit = null;

    //gọi ở các Activity để lấy kết nối
    public static ApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}