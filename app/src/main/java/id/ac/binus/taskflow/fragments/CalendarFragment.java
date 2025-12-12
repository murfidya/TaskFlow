package id.ac.binus.taskflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.ac.binus.taskflow.R;
import id.ac.binus.taskflow.adapters.TaskAdapter;
import id.ac.binus.taskflow.models.Task;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private TextView selectedDateTextView;
    private LinearLayout emptyTextView;
    private TaskAdapter taskAdapter;
    private List<Task> allTasks;
    private List<Task> filteredTasks;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private long selectedDateMillis;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        initViews(view);
        setupCalendar();
        loadAllTasks();

        return view;
    }

    private void initViews(View view) {
        calendarView = view.findViewById(R.id.calendar_view);
        recyclerView = view.findViewById(R.id.recycler_calendar_tasks);
        selectedDateTextView = view.findViewById(R.id.selected_date_text);
        emptyTextView = view.findViewById(R.id.empty_calendar_text);

        allTasks = new ArrayList<>();
        filteredTasks = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(getContext(), filteredTasks);
        recyclerView.setAdapter(taskAdapter);

        selectedDateMillis = System.currentTimeMillis();
        selectedDateTextView.setText("Tasks for " + dateFormat.format(new Date(selectedDateMillis)));
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            selectedDateMillis = calendar.getTimeInMillis();

            selectedDateTextView.setText("Tasks for " + dateFormat.format(new Date(selectedDateMillis)));
            filterTasksByDate();
        });
    }

    private void loadAllTasks() {
        if (userId == null) return;

        firestore.collection("users").document(userId)
                .collection("tasks")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        allTasks.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Task task = doc.toObject(Task.class);
                            if (task != null) {
                                task.setId(doc.getId());
                                allTasks.add(task);
                            }
                        }
                        filterTasksByDate();
                    }
                });
    }

    private void filterTasksByDate() {
        filteredTasks.clear();

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.setTimeInMillis(selectedDateMillis);
        int selectedYear = selectedCalendar.get(Calendar.YEAR);
        int selectedMonth = selectedCalendar.get(Calendar.MONTH);
        int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        for (Task task : allTasks) {
            Timestamp dueDate = task.getDueDate();
            if (dueDate != null) {
                Calendar taskCalendar = Calendar.getInstance();
                taskCalendar.setTime(dueDate.toDate());

                if (taskCalendar.get(Calendar.YEAR) == selectedYear &&
                        taskCalendar.get(Calendar.MONTH) == selectedMonth &&
                        taskCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay) {
                    filteredTasks.add(task);
                }
            }
        }

        taskAdapter.notifyDataSetChanged();

        if (filteredTasks.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
