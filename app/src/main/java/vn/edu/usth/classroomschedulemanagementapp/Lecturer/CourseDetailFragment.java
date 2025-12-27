package vn.edu.usth.classroomschedulemanagementapp.Lecturer;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.classroomschedulemanagementapp.ApiService;
import vn.edu.usth.classroomschedulemanagementapp.R;
import vn.edu.usth.classroomschedulemanagementapp.RetrofitClient;

public class CourseDetailFragment extends Fragment {

    private TextView tvDetailTitle, tvNoDocs;
    private ImageButton btnBack;
    private LinearLayout btnAddDoc;
    private RecyclerView rcvDocuments, rcvAttendance;
    private String courseName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);

        btnBack = view.findViewById(R.id.btnBack);
        tvDetailTitle = view.findViewById(R.id.tvDetailTitle);
        btnAddDoc = view.findViewById(R.id.btnAddDoc);
        rcvDocuments = view.findViewById(R.id.rcvDocuments);
        tvNoDocs = view.findViewById(R.id.tvNoDocs);
        rcvAttendance = view.findViewById(R.id.rcvAttendance);

        if (getArguments() != null) {
            courseName = getArguments().getString("COURSE_NAME");
            tvDetailTitle.setText(courseName);
        }

        rcvDocuments.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvAttendance.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnAddDoc.setOnClickListener(v -> showAddDocumentDialog());

        return view;
    }

    private void showAddDocumentDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_document, null);
        EditText edtTitle = dialogView.findViewById(R.id.edtDocTitle);
        EditText edtUrl = dialogView.findViewById(R.id.edtDocUrl);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setPositiveButton("UPLOAD", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(Color.WHITE);
        btnPositive.setBackgroundColor(Color.parseColor("#14345E"));
        btnPositive.setPadding(30, 0, 30, 0);

        btnPositive.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String url = edtUrl.getText().toString().trim();

            if (!title.isEmpty() && !url.isEmpty()) {
                handleUploadDocument(title, url, dialog);
            } else {
                Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleUploadDocument(String title, String url, AlertDialog dialog) {
        ApiService.DocumentRequest request = new ApiService.DocumentRequest(courseName, title, url);

        RetrofitClient.getService().uploadDocument(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Upload Successful!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}