package vn.edu.usth.classroomschedulemanagementapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton; // Thêm import này
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import vn.edu.usth.classroomschedulemanagementapp.R;

// Đảm bảo bạn implement NavigationView.OnNavigationItemSelectedListener
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton btnOpenDrawer; // Thêm nút của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        btnOpenDrawer = findViewById(R.id.btn_menu);

        // click mở drawer
        btnOpenDrawer.setOnClickListener(v -> {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        // click
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_calender) {
            Toast.makeText(this, "Calendar", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_all_courses) {
            Toast.makeText(this, "All Courses", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_my_courses) {
            Toast.makeText(this, "My Courses", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notif) {
            Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
        }

        // Đóng drawer sau khi click
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    // 6. Xử lý khi nhấn nút Back (Giữ nguyên, vẫn rất hữu ích)
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}