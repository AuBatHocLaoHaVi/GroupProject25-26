package vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse;

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
import vn.edu.usth.classroomschedulemanagementapp.R;
import vn.edu.usth.classroomschedulemanagementapp.RetrofitClient;

public class AllCoursesFragment extends Fragment {

    private RecyclerView rvCourses;
    private AllCourseAdapter adapter;
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
        adapter = new AllCourseAdapter(getContext(), subjectList);
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

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.e("AllCourses", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Can load subject list", Toast.LENGTH_SHORT).show();
            }
        });
    }
}