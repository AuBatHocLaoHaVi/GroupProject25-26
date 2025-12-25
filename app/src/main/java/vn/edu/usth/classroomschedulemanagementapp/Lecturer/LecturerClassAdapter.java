package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Thêm import này
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; // Thêm import này
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.usth.classroomschedulemanagementapp.R;

public class LecturerClassAdapter extends RecyclerView.Adapter<LecturerClassAdapter.ViewHolder> {
    private List<ClassModel> classList;

    public LecturerClassAdapter(List<ClassModel> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng lecturer_class_item.xml đã định nghĩa nút btnDetails
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecturer_class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassModel item = classList.get(position);
        if (item != null) {
            // Giữ nguyên logic hiển thị cũ
            holder.tvClassName.setText(item.getName() != null ? item.getName() : "N/A");
            String roomDisplay = item.getRoomName() != null ? "Room " + item.getRoomName() : item.getMajor();
            holder.tvRoom.setText(roomDisplay != null ? roomDisplay : "No Room");
            holder.tvTime.setText(item.getTimeRange());

            // THÊM: Logic xử lý nút Detail
            holder.btnDetails.setOnClickListener(v -> {
                // Ép kiểu context về AppCompatActivity để quản lý Fragment
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                // Chuyển sang fragment_course_detail.xml thông qua lớp quản lý nó
                // Giả sử bạn đặt tên Fragment mới là CourseDetailFragment
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CourseDetailFragment())
                        .addToBackStack(null) // Cho phép quay lại Dashboard
                        .commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return classList != null ? classList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvClassName, tvRoom, tvTime;
        Button btnDetails; // Thêm biến nút

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvTime = itemView.findViewById(R.id.tvTime);
            // Ánh xạ ID từ file XML lecturer_class_item.xml
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}