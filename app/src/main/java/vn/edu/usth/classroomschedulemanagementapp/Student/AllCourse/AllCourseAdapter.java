package vn.edu.usth.classroomschedulemanagementapp.Student.AllCourse;

import android.content.Context;
import android.content.SharedPreferences;
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

    public AllCourseAdapter(Context context, List<Subject> subjectList) {
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

        holder.tvCourseName.setText(subject.getName());
        holder.tvCredits.setText("Credits: " + subject.getCredits());
        holder.tvProfessor.setText("Lecturer: " + subject.getLecturer());


        holder.btnAction.setText("Enroll");
        holder.btnAction.setEnabled(true);
        holder.btnAction.setBackgroundColor(Color.parseColor("#0A2A57"));

        // enroll
        holder.btnAction.setOnClickListener(v -> {
            enrollCourse(subject.getId(), holder.btnAction);
        });
    }

    private void enrollCourse(String subjectId, Button btn) {
        //khóa nút
        btn.setEnabled(false);
        btn.setText("...");

        //User ID
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(context, "Please Login again!", Toast.LENGTH_SHORT).show();
            btn.setEnabled(true);
            btn.setText("Enroll");
            return;
        }

        //request
        EnrollRequest request = new EnrollRequest(userId, subjectId);

        //call API
        RetrofitClient.getService().enrollAuto(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Enroll Success!", Toast.LENGTH_SHORT).show();
                    btn.setText("Enrolled");
                    btn.setBackgroundColor(Color.GRAY);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(context, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                        if (errorBody.toLowerCase().contains("already")) {
                            btn.setText("Enrolled");
                            btn.setBackgroundColor(Color.GRAY);
                        } else {
                            btn.setEnabled(true);
                            btn.setText("Enroll");
                        }
                    } catch (Exception e) {
                        btn.setEnabled(true);
                        btn.setText("Enroll");
                        Toast.makeText(context, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Lỗi mạng: Mở lại nút
                btn.setEnabled(true);
                btn.setText("Enroll");
                Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }
}