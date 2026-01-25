package vn.edu.usth.classroomschedulemanagementapp.Lecturer.Attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import vn.edu.usth.classroomschedulemanagementapp.R;

public class AttendanceCheckAdapter extends RecyclerView.Adapter<AttendanceCheckAdapter.ViewHolder> {

    private List<AttendanceRecord> attendanceList;
    private Set<Integer> selectedPositions = new HashSet<>();
    private boolean isEditMode = false;
    private SelectionListener selectionListener;

    public interface SelectionListener {
        void onSelectionChanged(int count);
    }

    public AttendanceCheckAdapter(List<AttendanceRecord> attendanceList, SelectionListener listener) {
        this.attendanceList = attendanceList;
        this.selectionListener = listener;
    }

    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        if (!editMode) {
            selectedPositions.clear();
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(0);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public List<Integer> getSelectedPositions() {
        return new ArrayList<>(selectedPositions);
    }

    public int getSelectedCount() {
        return selectedPositions.size();
    }

    public void clearSelections() {
        selectedPositions.clear();
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(0);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = attendanceList.get(position);
        holder.tvName.setText(record.getStudentName());
        holder.tvId.setText("ID: " + record.getStudentCode());

        // Handle checkbox visibility and state
        holder.cbSelect.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setChecked(selectedPositions.contains(position));

        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            if (isChecked) {
                selectedPositions.add(currentPos);
            } else {
                selectedPositions.remove(currentPos);
            }

            if (selectionListener != null) {
                selectionListener.onSelectionChanged(selectedPositions.size());
            }
        });

        // Handle RadioGroup status
        holder.rgStatus.setOnCheckedChangeListener(null);
        if ("Present".equalsIgnoreCase(record.getStatus())) {
            holder.rbPresent.setChecked(true);
        } else if ("Absent".equalsIgnoreCase(record.getStatus())) {
            holder.rbAbsent.setChecked(true);
        } else {
            holder.rgStatus.clearCheck();
        }

        holder.rgStatus.setOnCheckedChangeListener((group, checkedId) -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            AttendanceRecord currentRecord = attendanceList.get(currentPos);
            if (checkedId == R.id.rbPresent) {
                currentRecord.setStatus("Present");
            } else if (checkedId == R.id.rbAbsent) {
                currentRecord.setStatus("Absent");
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendanceList != null ? attendanceList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        RadioGroup rgStatus;
        RadioButton rbPresent, rbAbsent;
        CheckBox cbSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            rgStatus = itemView.findViewById(R.id.rgStatus);
            rbPresent = itemView.findViewById(R.id.rbPresent);
            rbAbsent = itemView.findViewById(R.id.rbAbsent);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
}