package vn.edu.usth.classroomschedulemanagementapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCoursesFragment extends Fragment {

    private RecyclerView rvCourses;
    private CourseAdapter adapter;
    private List<Subject> subjectList;

    public AllCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_all_course, container, false);

        // 1. Ánh xạ RecyclerView
        rvCourses = view.findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));

        // 2. Khởi tạo list rỗng để tránh lỗi null
        subjectList = new ArrayList<>();
        adapter = new CourseAdapter(getContext(), subjectList);
        rvCourses.setAdapter(adapter);

        // 3. Gọi API lấy dữ liệu
        fetchSubjects();

        return view;
    }

    private void fetchSubjects() {
        RetrofitClient.getService().getSubjects().enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xóa dữ liệu cũ, thêm dữ liệu mới
                    subjectList.clear();
                    subjectList.addAll(response.body());

                    // Thông báo cho Adapter cập nhật giao diện
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.e("AllCourses", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Không tải được danh sách môn!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}