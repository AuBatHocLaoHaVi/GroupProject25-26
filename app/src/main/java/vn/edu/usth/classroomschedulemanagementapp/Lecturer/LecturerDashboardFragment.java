package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import vn.edu.usth.classroomschedulemanagementapp.R;

public class LecturerDashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private LecturerClassAdapter adapter;
    private List<ClassModel> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lecturer_dashboard_classes, container, false);
        recyclerView = view.findViewById(R.id.recyclerItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LecturerClassAdapter(dataList);
        recyclerView.setAdapter(adapter);

        loadData("CLASSES");
        return view;
    }

    private void loadData(String type) {
        dataList.clear();
        if (type.equals("CLASSES")) {
            // Sử dụng hàm createMockClass để gán đầy đủ thông tin
            dataList.add(createMockClass("Mobile Development", "402", "2025-12-18T08:00:00Z", "2025-12-18T10:30:00Z"));
            dataList.add(createMockClass("Database Management", "501", "2025-12-19T13:30:00Z", "2025-12-19T16:00:00Z"));
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private ClassModel createMockClass(String name, String room, String start, String end) {
        // Gọi Constructor 3 tham số mới tạo
        ClassModel model = new ClassModel("id", name, room);
        model.setRoomName(room);
        model.setStartTime(start);
        model.setEndTime(end);
        return model;
    }
}