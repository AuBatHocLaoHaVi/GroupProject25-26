package vn.edu.usth.classroomschedulemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailBox;
    private EditText passwordBox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout); // Đảm bảo tên file xml đúng

        // 1. Ánh xạ View theo ID trong file login_layout.xml bạn gửi
        emailBox = findViewById(R.id.email_box);
        passwordBox = findViewById(R.id.password_box);
        loginButton = findViewById(R.id.login_button);

        // 2. Xử lý sự kiện bấm nút Login
        loginButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = emailBox.getText().toString().trim();
        String password = passwordBox.getText().toString().trim();

        // Kiểm tra nhập liệu
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request
        LoginRequest request = new LoginRequest(email, password);

        // Gọi API (Server trung gian kết nối Neon)
        // Lưu ý: Bạn cần thay thế RetrofitClient bằng class bạn đã tạo ở bước trước
        RetrofitClient.getService().login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String role = user.getRole();

                    Toast.makeText(LoginActivity.this, "Welcome " + user.getFullName(), Toast.LENGTH_SHORT).show();

                    navigateBasedOnRole(role);

                } else {
                    // Login thất bại (Sai pass hoặc email)
                    Toast.makeText(LoginActivity.this, "Login failed! Check credentials.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginError", t.getMessage());
            }
        });
    }

    private void navigateBasedOnRole(String role) {
        Intent intent;

        // Kiểm tra Role khớp với Database (STUDENT / LECTURER)
        if ("STUDENT".equalsIgnoreCase(role)) {
            // Chuyển sang màn hình StudentMainActivity mà bạn đã gửi
            intent = new Intent(LoginActivity.this, StudentMainActivity.class);
        } else if ("LECTURER".equalsIgnoreCase(role)) {
            // Chuyển sang màn hình Giảng viên (Bạn cần tạo file này nếu chưa có)
            // intent = new Intent(LoginActivity.this, LecturerMainActivity.class);
            Toast.makeText(this, "Redirecting to Lecturer Screen...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "Unknown Role: " + role, Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
        finish(); // Đóng LoginActivity để user không back lại được
    }
}