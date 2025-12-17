package vn.edu.usth.classroomschedulemanagementapp.Student.Account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.usth.classroomschedulemanagementapp.R;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private final List<StudentGrade> list;

    public GradeAdapter(List<StudentGrade> list) { this.list = list; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentGrade item = list.get(position);
        holder.tvSubject.setText(item.getSubjectName());
        holder.tvItemName.setText(item.getGradeItemName() + " (" + item.getWeight() + ")");
        holder.tvScore.setText(String.valueOf(item.getScore()));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvItemName, tvScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tvSubject);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvScore = itemView.findViewById(R.id.tvScore);
        }
    }
}