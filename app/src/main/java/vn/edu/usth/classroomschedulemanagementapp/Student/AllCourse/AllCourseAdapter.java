package vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.classroomschedulemanagementapp.EnrollRequest;
import vn.edu.usth.classroomschedulemanagementapp.R;
import vn.edu.usth.classroomschedulemanagementapp.RetrofitClient;

public class AllCourseAdapter extends RecyclerView.Adapter<AllCourseAdapter.CourseViewHolder> {

    private Context context;
    private List<Subject> subjectList;
    private String currentUserId;

    public AllCourseAdapter(Context context, List<Subject> subjectList, String currentUserId) {
        this.context = context;
        this.subjectList = subjectList;
        this.currentUserId = currentUserId;
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

        holder.tvCourseName.setText(subject.getName());
        holder.tvCredits.setText("Credits: " + subject.getCredits());
        holder.tvProfessor.setText("Lecturer: " + subject.getLecturer());
        holder.btnAction.setOnClickListener(null);

        if (subject.isEnrolled()) {
            // TRẠNG THÁI ĐÃ ENROLL
            holder.btnAction.setText("Enrolled");
            holder.btnAction.setEnabled(false);

            // Set màu xám cho background
            holder.btnAction.setBackgroundColor(Color.GRAY);
            holder.btnAction.setTextColor(Color.WHITE);
        } else {
            holder.btnAction.setText("Enroll");
            holder.btnAction.setEnabled(true);

            holder.btnAction.setBackgroundColor(Color.parseColor("#0A2A57"));
            holder.btnAction.setTextColor(Color.WHITE);

            holder.btnAction.setOnClickListener(v -> {
                enrollCourse(subject, holder, position);
            });
        }
    }

    private void enrollCourse(Subject subject, CourseViewHolder holder, int position) {
        holder.btnAction.setEnabled(false);
        holder.btnAction.setText("...");

        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(context, "Missing UserID", Toast.LENGTH_SHORT).show();
            notifyItemChanged(position);
            return;
        }

        EnrollRequest request = new EnrollRequest(currentUserId, subject.getId());

        RetrofitClient.getService().enrollAuto(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Enroll Success!", Toast.LENGTH_SHORT).show();
                    //Cập nhật dữ liệu trong List
                    subject.setEnrolled(true);
                    notifyItemChanged(position);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        // Nếu server báo đã đăng ký rồi -> Cũng cập nhật thành Enrolled luôn
                        if (errorBody.toLowerCase().contains("already")) {
                            Toast.makeText(context, "Already Enrolled!", Toast.LENGTH_SHORT).show();
                            subject.setEnrolled(true);
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(context, "Failed: " + errorBody, Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position); // Reset lại nút về ban đầu
                        }
                    } catch (Exception e) {
                        notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                notifyItemChanged(position); // Reset lại nút nếu mất mạng
            }
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
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvProfessor = itemView.findViewById(R.id.tvProfessor);
            tvCredits = itemView.findViewById(R.id.tvCredits);
            btnAction = itemView.findViewById(R.id.btnAction); // Đảm bảo ID này đúng trong XML item
        }
    }
}