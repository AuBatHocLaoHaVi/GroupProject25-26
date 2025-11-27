package vn.edu.usth.classroomschedulemanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyCoursesAdapter extends RecyclerView.Adapter<MyCoursesAdapter.CourseViewHolder> {


    private final List<String> courseList;
    private final String mode;

    public MyCoursesAdapter(List<String> courseList, String mode) {
        this.courseList = courseList;
        this.mode = mode;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mycourseitem, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        holder.tvCourseName.setText(courseList.get(position));

        if (mode.equals("course")) {
            holder.btnAction.setText("Detail");

            holder.btnAction.setOnClickListener(v -> {
                Toast.makeText(v.getContext(),
                        "Open detail of " + courseList.get(position),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView tvCourseName, tvProfessor, tvCredits;
        Button btnAction;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvProfessor  = itemView.findViewById(R.id.tvProfessor);
            tvCredits    = itemView.findViewById(R.id.tvCredits);
            btnAction    = itemView.findViewById(R.id.btnAction);
        }
    }
}

