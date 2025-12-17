package vn.edu.usth.classroomschedulemanagementapp.Student.MyCourse.CourseDetail;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vn.edu.usth.classroomschedulemanagementapp.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private final List<Attendance> list;

    public AttendanceAdapter(List<Attendance> list) { this.list = list; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance item = list.get(position);
        holder.tvDate.setText(item.getScheduleDate());
        holder.tvTime.setText(item.getTimeRange());

        String status = item.getStatus();
        if (status == null) status = "Not Yet";

        holder.tvStatus.setText(status);
        if ("Present".equalsIgnoreCase(status)) holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
        else if ("Absent".equalsIgnoreCase(status)) holder.tvStatus.setTextColor(Color.RED);
        else holder.tvStatus.setTextColor(Color.GRAY);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}