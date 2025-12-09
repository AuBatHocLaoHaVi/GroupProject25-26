package vn.edu.usth.classroomschedulemanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Subject> subjectList;

    public CourseAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_all_course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Subject subject = subjectList.get(position);

        // Gán dữ liệu vào View
        holder.tvCourseName.setText(subject.getName());
        holder.tvCredits.setText("Credits: " + subject.getCredits());

        // Lưu ý: Database bảng 'subject' chưa có cột Professor, tạm thời mình để tên Khoa (Faculty)
        holder.tvProfessor.setText("Faculty: " + subject.getFaculty());

        holder.btnAction.setText("Enroll");

        // Xử lý sự kiện bấm nút Enroll (để sau làm tiếp)
        holder.btnAction.setOnClickListener(v -> {
            // TODO: Code tính năng đăng ký môn học sau
        });
    }

    @Override
    public int getItemCount() {
        return subjectList != null ? subjectList.size() : 0;
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvProfessor, tvCredits;
        Button btnAction;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ file xml student_all_course_item
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvProfessor = itemView.findViewById(R.id.tvProfessor);
            tvCredits = itemView.findViewById(R.id.tvCredits);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}