package vn.edu.usth.classroomschedulemanagementapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesFragment extends Fragment {

    RecyclerView rcvCourse;
    MyCoursesAdapter adapter;
    List<String> courseList = new ArrayList<>();

    public MyCoursesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_course, container, false);
        rcvCourse = view.findViewById(R.id.rcvCourse);

        // Fake data
        courseList.add("Calculus 1");
        courseList.add("Mobile Development");
        courseList.add("Computer Network");
        courseList.add("AI Fundamentals");

        adapter = new MyCoursesAdapter(courseList, "Detail");

        rcvCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCourse.setAdapter(adapter);

        return view;
    }
}
