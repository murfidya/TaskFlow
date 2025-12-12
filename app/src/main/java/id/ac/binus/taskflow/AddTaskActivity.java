package id.ac.binus.taskflow;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.ac.binus.taskflow.models.Category;

public class AddTaskActivity extends AppCompatActivity {

    private static final String TAG = "AddTaskActivity";

    private ImageView backButton;
    private EditText titleEditText, descriptionEditText;
    private Spinner categorySpinner, prioritySpinner, statusSpinner;
    private TextView dueDateTextView;
    private RelativeLayout saveButton, datePickerContainer;
    private ProgressBar progressBar;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private List<Category> categoryList;
    private List<String> categoryNames;
    private Date selectedDueDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            Log.d(TAG, "User ID: " + userId);
        } else {
            Log.e(TAG, "User is null!");
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
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
        saveButton = findViewById(R.id.btn_save);
        progressBar = findViewById(R.id.progress_bar);

        categoryList = new ArrayList<>();
        categoryNames = new ArrayList<>();
    }

    private void loadCategories() {
        if (userId == null) {
            Log.e(TAG, "Cannot load categories: userId is null");
            setupDefaultCategories();
            return;
        }

        Log.d(TAG, "Loading categories for user: " + userId);

        firestore.collection("users").document(userId)
                .collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Categories query successful. Count: " + queryDocumentSnapshots.size());
                    categoryList.clear();
                    categoryNames.clear();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Category category = doc.toObject(Category.class);
                        if (category != null) {
                            category.setId(doc.getId());
                            categoryList.add(category);
                            categoryNames.add(category.getName());
                            Log.d(TAG, "Category loaded: " + category.getName());
                        }
                    }

                    if (categoryNames.isEmpty()) {
                        Log.w(TAG, "No categories found, creating defaults");
                        createDefaultCategoriesInFirestore();
                    } else {
                        setupCategorySpinner();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load categories", e);
                    Toast.makeText(this, "Failed to load categories: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    setupDefaultCategories();
                });
    }

    private void createDefaultCategoriesInFirestore() {
        Log.d(TAG, "Creating default categories in Firestore");

        WriteBatch batch = firestore.batch();

        String[][] defaultCategories = {
                {"Personal", "#4CAF50", "person"},
                {"Work", "#2196F3", "work"},
                {"Study", "#FF9800", "school"},
                {"Other", "#9E9E9E", "category"}
        };

        for (String[] cat : defaultCategories) {
            Map<String, Object> category = new HashMap<>();
            category.put("name", cat[0]);
            category.put("color", cat[1]);
            category.put("icon", cat[2]);

            batch.set(firestore.collection("users").document(userId)
                    .collection("categories").document(), category);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Default categories created successfully");
                    // Reload categories
                    loadCategories();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to create default categories", e);
                    setupDefaultCategories();
                });
    }

    private void setupDefaultCategories() {
        // Use hardcoded categories if Firestore fails
        Log.d(TAG, "Setting up default categories locally");
        categoryNames.clear();
        categoryNames.add("Personal");
        categoryNames.add("Work");
        categoryNames.add("Study");
        categoryNames.add("Other");
        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        Log.d(TAG, "Setting up category spinner with " + categoryNames.size() + " items");

        if (categoryNames.isEmpty()) {
            categoryNames.add("General");
        }

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
        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                priorities
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Status Spinner
        String[] statuses = {"Pending", "In Progress", "Completed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        datePickerContainer.setOnClickListener(v -> showDatePicker());

        saveButton.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
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

    private void saveTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem() != null ?
                categorySpinner.getSelectedItem().toString() : "General";
        String priority = prioritySpinner.getSelectedItem().toString();
        String status = statusSpinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Saving task: " + title + ", Category: " + category);

        progressBar.setVisibility(View.VISIBLE);
        saveButton.setEnabled(false);

        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("category", category);
        task.put("priority", priority);
        task.put("status", status);
        task.put("dueDate", selectedDueDate != null ? new Timestamp(selectedDueDate) : null);
        task.put("createdAt", Timestamp.now());
        task.put("updatedAt", Timestamp.now());

        firestore.collection("users").document(userId)
                .collection("tasks")
                .add(task)
                .addOnCompleteListener(task1 -> {
                    // Hide progress on main thread
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        saveButton.setEnabled(true);
                    });

                    if (task1.isSuccessful()) {
                        Log.d(TAG, "Task added successfully: " + task1.getResult().getId());
                        runOnUiThread(() -> {
                            Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    } else {
                        Log.e(TAG, "Failed to add task", task1.getException());
                        runOnUiThread(() -> {
                            String errorMsg = task1.getException() != null ?
                                task1.getException().getMessage() : "Unknown error";
                            Toast.makeText(AddTaskActivity.this, "Failed to add task: " + errorMsg, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }
}
