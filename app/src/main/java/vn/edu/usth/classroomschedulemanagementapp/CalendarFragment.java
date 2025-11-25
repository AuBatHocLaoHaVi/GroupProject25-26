package vn.edu.usth.classroomschedulemanagementapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.CalendarMonth;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.core.WeekDay;
import com.kizitonwose.calendar.view.CalendarView;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;
import com.kizitonwose.calendar.view.WeekCalendarView;
import com.kizitonwose.calendar.view.WeekDayBinder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {

    // Views
    private CalendarView monthCalendarView;
    private WeekCalendarView weekCalendarView;
    private TextView tvMonthYear, tvDateSelected;
    private ImageButton btnNext, btnPrev;
    private CheckBox cbToggle;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;

    // Data
    private LocalDate selectedDate = LocalDate.now();
    private HashMap<LocalDate, List<Schedule>> database = new HashMap<>();

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Ánh xạ Views
        monthCalendarView = view.findViewById(R.id.calendarView);
        weekCalendarView = view.findViewById(R.id.weekCalendarView);
        tvMonthYear = view.findViewById(R.id.tvMonthYear);
        tvDateSelected = view.findViewById(R.id.tvDateSelected);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrev = view.findViewById(R.id.btnPrevious);
        cbToggle = view.findViewById(R.id.cbToggleWeekMonth);
        recyclerView = view.findViewById(R.id.recyclerViewSchedule);

        // Setup RecyclerView & Data
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        initFakeData();

        // --- CẤU HÌNH LỊCH THÁNG ---
        class MonthDayViewContainer extends ViewContainer {
            TextView textView;
            View dotView, frameLayout;
            CalendarDay day;

            public MonthDayViewContainer(View view) {
                super(view);
                textView = view.findViewById(R.id.calendarDayText);
                dotView = view.findViewById(R.id.calendarDayDot);
                frameLayout = view.findViewById(R.id.exOneDayFrame);
                view.setOnClickListener(v -> {
                    if (day.getPosition() == DayPosition.MonthDate) {
                        selectDate(day.getDate());
                    }
                });
            }
        }

        monthCalendarView.setDayBinder(new MonthDayBinder<MonthDayViewContainer>() {
            @NonNull
            @Override
            public MonthDayViewContainer create(@NonNull View view) {
                return new MonthDayViewContainer(view);
            }

            @Override
            public void bind(@NonNull MonthDayViewContainer container, CalendarDay calendarDay) {
                container.day = calendarDay;
                bindDayView(container.textView, container.frameLayout, container.dotView,
                        calendarDay.getDate(), calendarDay.getPosition() == DayPosition.MonthDate);
            }
        });

        // --- CẤU HÌNH LỊCH TUẦN ---
        class WeekDayViewContainer extends ViewContainer {
            TextView textView;
            View dotView, frameLayout;
            WeekDay day;

            public WeekDayViewContainer(View view) {
                super(view);
                textView = view.findViewById(R.id.calendarDayText);
                dotView = view.findViewById(R.id.calendarDayDot);
                frameLayout = view.findViewById(R.id.exOneDayFrame);
                view.setOnClickListener(v -> selectDate(day.getDate()));
            }
        }

        weekCalendarView.setDayBinder(new WeekDayBinder<WeekDayViewContainer>() {
            @NonNull
            @Override
            public WeekDayViewContainer create(@NonNull View view) {
                return new WeekDayViewContainer(view);
            }

            @Override
            public void bind(@NonNull WeekDayViewContainer container, WeekDay weekDay) {
                container.day = weekDay;
                bindDayView(container.textView, container.frameLayout, container.dotView,
                        weekDay.getDate(), true);
            }
        });

        // --- SETUP THỜI GIAN ---
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(24);
        YearMonth endMonth = currentMonth.plusMonths(24);

        // Setup Month Calendar
        monthCalendarView.setup(startMonth, endMonth, DayOfWeek.MONDAY);
        monthCalendarView.scrollToMonth(currentMonth);

        // Setup Week Calendar (Dùng LocalDate để setup)
        LocalDate startWeek = startMonth.atDay(1);
        LocalDate endWeek = endMonth.atEndOfMonth();
        weekCalendarView.setup(startWeek, endWeek, DayOfWeek.MONDAY);
        weekCalendarView.scrollToWeek(LocalDate.now());

        // --- SỰ KIỆN SCROLL (ĐỂ CẬP NHẬT TIÊU ĐỀ THÁNG) ---
        monthCalendarView.setMonthScrollListener(calendarMonth -> {
            updateMonthHeader(calendarMonth.getYearMonth());
            return null;
        });

        weekCalendarView.setWeekScrollListener(week -> {
            // Lấy ngày đầu tiên của tuần để hiện tháng
            LocalDate firstDate = week.getDays().get(0).getDate();
            updateMonthHeader(YearMonth.from(firstDate));
            return null;
        });

        // --- XỬ LÝ CÁC NÚT BẤM ---

        // 1. Toggle Tuần/Tháng
        cbToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // Chuyển sang Tuần
                monthCalendarView.setVisibility(View.GONE);
                weekCalendarView.setVisibility(View.VISIBLE);
                weekCalendarView.scrollToWeek(selectedDate); // Đồng bộ vị trí
            } else { // Chuyển sang Tháng
                weekCalendarView.setVisibility(View.GONE);
                monthCalendarView.setVisibility(View.VISIBLE);
                monthCalendarView.scrollToMonth(YearMonth.from(selectedDate)); // Đồng bộ vị trí
            }
        });

        // 2. Nút Next/Previous (Xử lý logic cho cả 2 loại lịch)
        btnNext.setOnClickListener(v -> {
            if (monthCalendarView.getVisibility() == View.VISIBLE) {
                // Logic cho tháng: +1 tháng
                CalendarMonth current = monthCalendarView.findFirstVisibleMonth();
                if (current != null) {
                    monthCalendarView.smoothScrollToMonth(current.getYearMonth().plusMonths(1));
                }
            } else {
                // Logic cho tuần: +1 tuần
                weekCalendarView.smoothScrollToWeek(weekCalendarView.findFirstVisibleWeek().getDays().get(0).getDate().plusWeeks(1));
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (monthCalendarView.getVisibility() == View.VISIBLE) {
                // Logic cho tháng: -1 tháng
                CalendarMonth current = monthCalendarView.findFirstVisibleMonth();
                if (current != null) {
                    monthCalendarView.smoothScrollToMonth(current.getYearMonth().minusMonths(1));
                }
            } else {
                // Logic cho tuần: -1 tuần
                weekCalendarView.smoothScrollToWeek(weekCalendarView.findFirstVisibleWeek().getDays().get(0).getDate().minusWeeks(1));
            }
        });

        // Khởi tạo
        selectDate(LocalDate.now());
        return view;
    }

    // --- CÁC HÀM PHỤ TRỢ DÙNG CHUNG ---

    // Hàm bind giao diện ngày (Dùng chung cho cả Tuần và Tháng để code gọn)
    private void bindDayView(TextView textView, View frameLayout, View dotView, LocalDate date, boolean isCurrentMonth) {
        textView.setText(String.valueOf(date.getDayOfMonth()));

        if (isCurrentMonth) {
            textView.setTextColor(Color.BLACK);

            // Check Selected
            if (date.equals(selectedDate)) {
                frameLayout.setBackgroundResource(R.drawable.ic_dot);
                frameLayout.getBackground().setTint(getResources().getColor(R.color.deep_blue, null));
                textView.setTextColor(Color.WHITE);
            } else {
                frameLayout.setBackground(null);
            }

            // Check Dot
            if (database.containsKey(date)) {
                dotView.setVisibility(View.VISIBLE);
            } else {
                dotView.setVisibility(View.GONE);
            }
        } else {
            textView.setTextColor(Color.GRAY);
            dotView.setVisibility(View.GONE);
            frameLayout.setBackground(null);
        }
    }

    // Hàm chọn ngày
    private void selectDate(LocalDate date) {
        if (selectedDate.equals(date)) return; // Tránh refresh nếu bấm lại ngày cũ

        LocalDate oldDate = selectedDate;
        selectedDate = date;

        // Refresh cả 2 lịch để cập nhật vòng tròn tím
        monthCalendarView.notifyDateChanged(oldDate);
        monthCalendarView.notifyDateChanged(selectedDate);
        weekCalendarView.notifyDateChanged(oldDate);
        weekCalendarView.notifyDateChanged(selectedDate);

        // Cập nhật list
        updateAdapterForDate(selectedDate);
    }

    private void updateMonthHeader(YearMonth yearMonth) {
        String title = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        tvMonthYear.setText(title);
    }

    private void updateAdapterForDate(LocalDate date) {
        tvDateSelected.setText("Schedule: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (database.containsKey(date)) {
            adapter.updateData(database.get(date));
        } else {
            adapter.updateData(new ArrayList<>());
        }
    }

    private void initFakeData() {
        LocalDate today = LocalDate.now();
        List<Schedule> list1 = new ArrayList<>();
        list1.add(new Schedule("OOP", "02:00 - 19:00", "Room 302"));
        database.put(today, list1);

        LocalDate tomorrow = today.plusDays(1);
        List<Schedule> list2 = new ArrayList<>();
        list2.add(new Schedule("Calculus 1", "09:00 - 11:30", "A21- Auditorium"));
        database.put(tomorrow, list2);
    }
}
