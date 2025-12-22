package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Đảm bảo dùng androidx để không bị lỗi 'cannot be applied'

import vn.edu.usth.classroomschedulemanagementapp.R;

public class CourseDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_course_detail.xml
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);

        // 1. Ánh xạ các View từ XML
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        TextView tvDetailTitle = view.findViewById(R.id.tvDetailTitle);

        // 2. Xử lý sự kiện nút Back (Quay lại danh sách Dashboard)
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Quay lại Fragment trước đó trong BackStack
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Bạn có thể thiết lập thêm dữ liệu cho tiêu đề ở đây nếu cần
        // tvDetailTitle.setText("Chi tiết môn học");

        return view;
    }
}