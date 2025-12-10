package vn.edu.usth.classroomschedulemanagementapp.Student.MyCourse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.usth.classroomschedulemanagementapp.R;
import vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse.Subject; // Import đúng model

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.CourseViewHolder> {

    private final List<Subject> courseList;

    public MyCoursesAdapter(List<Subject> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_my_course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Subject subject = courseList.get(position);

        // hiển thị dữ liệu từ Server
        holder.tvCourseName.setText(subject.getName());
        holder.tvCredits.setText("Credits: " + subject.getCredits());
        holder.tvProfessor.setText("Lecturer: " + subject.getLecturer());

        holder.btnAction.setText("Detail");
        holder.btnAction.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Xem chi tiết môn: " + subject.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return courseList.size(); }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvProfessor, tvCredits;
        Button btnAction;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvProfessor  = itemView.findViewById(R.id.tvProfessor);
            tvCredits    = itemView.findViewById(R.id.tvCredits);
            btnAction    = itemView.findViewById(R.id.btnDetail);
        }
    }
}