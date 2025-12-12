package id.ac.binus.taskflow;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.ac.binus.taskflow.models.Category;
import id.ac.binus.taskflow.models.Task;

public class EditTaskActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText titleEditText, descriptionEditText;
    private Spinner categorySpinner, prioritySpinner, statusSpinner;
    private TextView dueDateTextView;
    private RelativeLayout updateButton, deleteButton, datePickerContainer;
    private ProgressBar progressBar;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String taskId;

    private List<Category> categoryList;
    private List<String> categoryNames;
    private Date selectedDueDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    private String[] priorities = {"Low", "Medium", "High"};
    private String[] statuses = {"Pending", "In Progress", "Completed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskId = getIntent().getStringExtra("taskId");
        if (taskId == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        initViews();
        setupSpinners();
        loadCategories();
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btn_back);
        titleEditText = findViewById(R.id.edit_title);
        descriptionEditText = findViewById(R.id.edit_description);
        categorySpinner = findViewById(R.id.spinner_category);
        prioritySpinner = findViewById(R.id.spinner_priority);
        statusSpinner = findViewById(R.id.spinner_status);
        dueDateTextView = findViewById(R.id.text_due_date);
        datePickerContainer = findViewById(R.id.date_picker_container);
        updateButton = findViewById(R.id.btn_update);
        deleteButton = findViewById(R.id.btn_delete);
        progressBar = findViewById(R.id.progress_bar);

        categoryList = new ArrayList<>();
        categoryNames = new ArrayList<>();
    }

    private void loadCategories() {
        if (userId == null) return;

        firestore.collection("users").document(userId)
                .collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList.clear();
                    categoryNames.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Category category = doc.toObject(Category.class);
                        if (category != null) {
                            category.setId(doc.getId());
                            categoryList.add(category);
                            categoryNames.add(category.getName());
                        }
                    }
                    setupCategorySpinner();
                    loadTaskData();
                });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupSpinners() {
        // Priority Spinner
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorities
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Status Spinner
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
    }

    private void loadTaskData() {
        if (userId == null || taskId == null) return;

        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("users").document(userId)
                .collection("tasks").document(taskId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (documentSnapshot.exists()) {
                        Task task = documentSnapshot.toObject(Task.class);
                        if (task != null) {
                            populateFields(task);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Failed to load task", Toast.LENGTH_SHORT).show();
                });
    }

    private void populateFields(Task task) {
        titleEditText.setText(task.getTitle());
        descriptionEditText.setText(task.getDescription());

        // Set Category Spinner
        if (task.getCategory() != null) {
            int categoryIndex = categoryNames.indexOf(task.getCategory());
            if (categoryIndex >= 0) {
                categorySpinner.setSelection(categoryIndex);
            }
        }

        // Set Priority Spinner
        if (task.getPriority() != null) {
            int priorityIndex = Arrays.asList(priorities).indexOf(task.getPriority());
            if (priorityIndex >= 0) {
                prioritySpinner.setSelection(priorityIndex);
            }
        }

        // Set Status Spinner
        if (task.getStatus() != null) {
            int statusIndex = Arrays.asList(statuses).indexOf(task.getStatus());
            if (statusIndex >= 0) {
                statusSpinner.setSelection(statusIndex);
            }
        }

        // Set Due Date
        if (task.getDueDate() != null) {
            selectedDueDate = task.getDueDate().toDate();
            dueDateTextView.setText(dateFormat.format(selectedDueDate));
        }
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        datePickerContainer.setOnClickListener(v -> showDatePicker());

        updateButton.setOnClickListener(v -> updateTask());

        deleteButton.setOnClickListener(v -> confirmDelete());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDueDate != null) {
            calendar.setTime(selectedDueDate);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    selectedDueDate = selectedCalendar.getTime();
                    dueDateTextView.setText(dateFormat.format(selectedDueDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem() != null ?
                categorySpinner.getSelectedItem().toString() : "";
        String priority = prioritySpinner.getSelectedItem().toString();
        String status = statusSpinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);
        updates.put("description", description);
        updates.put("category", category);
        updates.put("priority", priority);
        updates.put("status", status);
        updates.put("dueDate", selectedDueDate != null ? new Timestamp(selectedDueDate) : null);
        updates.put("updatedAt", Timestamp.now());

        firestore.collection("users").document(userId)
                .collection("tasks").document(taskId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    });

                    if (task.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(EditTaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            String errorMsg = task.getException() != null ?
                                task.getException().getMessage() : "Unknown error";
                            Toast.makeText(EditTaskActivity.this, "Failed to update task: " + errorMsg, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> deleteTask())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask() {
        progressBar.setVisibility(View.VISIBLE);
        deleteButton.setEnabled(false);
        updateButton.setEnabled(false);

        firestore.collection("users").document(userId)
                .collection("tasks").document(taskId)
                .delete()
                .addOnCompleteListener(task -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                    });

                    if (task.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(EditTaskActivity.this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            deleteButton.setEnabled(true);
                            updateButton.setEnabled(true);
                            String errorMsg = task.getException() != null ?
                                task.getException().getMessage() : "Unknown error";
                            Toast.makeText(EditTaskActivity.this, "Failed to delete task: " + errorMsg, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }
}
