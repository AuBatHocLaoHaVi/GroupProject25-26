package vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse;

import android.content.Context;
import android.content.SharedPreferences;
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
    private String userId; // [MỚI]

    public AllCoursesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_all_course, container, false);

        rvCourses = view.findViewById(R.id.rvCourses);
        rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectList = new ArrayList<>();

        // 1. Lấy UserID từ SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getString("USER_ID", "");

        // 2. Truyền userId vào Adapter
        adapter = new AllCourseAdapter(getContext(), subjectList, userId);
        rvCourses.setAdapter(adapter);

        // 3. Gọi API
        fetchSubjects();

        return view;
    }
    private void fetchSubjects() {
        if (userId.isEmpty()) {
            Toast.makeText(getContext(), "User not found, please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        // [QUAN TRỌNG] Gọi API có tham số userId để check trạng thái Enroll
        // Đảm bảo bên ApiService đã sửa thành: getSubjects(@Query("userId") String userId)
        RetrofitClient.getService().getSubjects(userId).enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subjectList.clear();
                    subjectList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                Log.e("AllCourses", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}