package vn.edu.usth.classroomschedulemanagementapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // QUAN TRỌNG: Địa chỉ này dành cho máy ảo
    // Máy ảo sẽ coi máy tính là 10.0.2.2 thay vì localhost
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    // Nếu chạy app trên điện thoại thật, hãy thay bằng IP mạng LAN của máy tính
    //private static final String BASE_URL = "http://192.168.1.5:3000/";

    private static Retrofit retrofit = null;

    // Hàm này sẽ được gọi ở các Activity để lấy kết nối
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