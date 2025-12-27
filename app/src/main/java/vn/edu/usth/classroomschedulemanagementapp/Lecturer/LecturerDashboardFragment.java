package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.classroomschedulemanagementapp.Calendar.ScheduleResponse;
import vn.edu.usth.classroomschedulemanagementapp.R;
import vn.edu.usth.classroomschedulemanagementapp.RetrofitClient;

public class LecturerDashboardFragment extends Fragment {
    // SỬA: Bỏ 'private' để tránh lỗi IllegalAccessError khi truy cập từ Callback
    RecyclerView recyclerView;
    LecturerClassAdapter adapter;
    List<ClassModel> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lecturer_dashboard_classes, container, false);

        recyclerView = view.findViewById(R.id.recyclerItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LecturerClassAdapter(dataList);
        recyclerView.setAdapter(adapter);

        loadDataFromApi();

        return view;
    }

    private void loadDataFromApi() {
        // SỬA: Lấy ID linh hoạt từ SharedPreferences giống phần Student
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", "");

        if (userId.isEmpty()) {
            Toast.makeText(getContext(), "Lỗi: Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getService().getStudentSchedule(userId).enqueue(new Callback<List<ScheduleResponse>>() {
            @Override
            public void onResponse(Call<List<ScheduleResponse>> call, Response<List<ScheduleResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataList.clear();
                    for (ScheduleResponse item : response.body()) {
                        // SỬA: Gán item.getSubjectName() vào constructor để hiển thị tên môn học
                        ClassModel model = new ClassModel("id", item.getSubjectName(), "Major");
                        model.setRoomName(item.getRoomName());
                        model.setStartTime(item.getStartTime());
                        model.setEndTime(item.getEndTime());

                        dataList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    Log.d("API_SUCCESS", "Tải thành công: " + dataList.size() + " lớp.");
                } else {
                    Log.e("API_ERROR", "Mã lỗi: " + response.code());
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu cho ID: " + userId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}