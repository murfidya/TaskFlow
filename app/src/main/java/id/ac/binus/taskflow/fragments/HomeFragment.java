package id.ac.binus.taskflow.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import id.ac.binus.taskflow.R;
import id.ac.binus.taskflow.adapters.TaskAdapter;
import id.ac.binus.taskflow.models.Category;
import id.ac.binus.taskflow.models.Task;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private List<Task> filteredTaskList;
    private EditText searchEditText;
    private Spinner categorySpinner, statusSpinner, prioritySpinner;
    private LinearLayout emptyTextView;
    private TextView welcomeTextView;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private List<Category> categoryList;
    private List<String> categoryNames;

    private String selectedCategory = "All";
    private String selectedStatus = "All";
    private String selectedPriority = "All";
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        initViews(view);
        loadUserName();
        loadCategories();
        setupSpinners();
        setupSearch();
        loadTasks();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_tasks);
        searchEditText = view.findViewById(R.id.search_edit_text);
        categorySpinner = view.findViewById(R.id.spinner_category);
        statusSpinner = view.findViewById(R.id.spinner_status);
        prioritySpinner = view.findViewById(R.id.spinner_priority);
        emptyTextView = view.findViewById(R.id.empty_text_view);
        welcomeTextView = view.findViewById(R.id.welcome_text_view);

        taskList = new ArrayList<>();
        filteredTaskList = new ArrayList<>();
        categoryList = new ArrayList<>();
        categoryNames = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(getContext(), filteredTaskList);
        recyclerView.setAdapter(taskAdapter);
    }

    private void loadUserName() {
        if (userId != null) {
            firestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            if (username != null && !username.isEmpty()) {
                                welcomeTextView.setText("Hello, " + username + "!");
                            }
                        }
                    });
        }
    }

    private void loadCategories() {
        categoryNames.clear();
        categoryNames.add("All");

        if (userId != null) {
            firestore.collection("users").document(userId)
                    .collection("categories")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        categoryList.clear();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Category category = doc.toObject(Category.class);
                            if (category != null) {
                                category.setId(doc.getId());
                                categoryList.add(category);
                                categoryNames.add(category.getName());
                            }
                        }
                        setupCategorySpinner();
                    });
        }
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupSpinners() {
        // Status Spinner
        String[] statuses = {"All", "Pending", "In Progress", "Completed"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Priority Spinner
        String[] priorities = {"All", "High", "Medium", "Low"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                priorities
        );
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Set listeners
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryNames.get(position);
                filterTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statuses[position];
                filterTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriority = priorities[position];
                filterTasks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString().toLowerCase().trim();
                filterTasks();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadTasks() {
        if (userId == null) return;

        firestore.collection("users").document(userId)
                .collection("tasks")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        taskList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Task task = doc.toObject(Task.class);
                            if (task != null) {
                                task.setId(doc.getId());
                                taskList.add(task);
                            }
                        }
                        filterTasks();
                    }
                });
    }

    private void filterTasks() {
        filteredTaskList.clear();

        for (Task task : taskList) {
            boolean matchesCategory = selectedCategory.equals("All") ||
                    (task.getCategory() != null && task.getCategory().equals(selectedCategory));
            boolean matchesStatus = selectedStatus.equals("All") ||
                    (task.getStatus() != null && task.getStatus().equals(selectedStatus));
            boolean matchesPriority = selectedPriority.equals("All") ||
                    (task.getPriority() != null && task.getPriority().equals(selectedPriority));
            boolean matchesSearch = searchQuery.isEmpty() ||
                    (task.getTitle() != null && task.getTitle().toLowerCase().contains(searchQuery)) ||
                    (task.getDescription() != null && task.getDescription().toLowerCase().contains(searchQuery));

            if (matchesCategory && matchesStatus && matchesPriority && matchesSearch) {
                filteredTaskList.add(task);
            }
        }

        taskAdapter.notifyDataSetChanged();

        if (filteredTaskList.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
}
