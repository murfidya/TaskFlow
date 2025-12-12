package id.ac.binus.taskflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import id.ac.binus.taskflow.ChangePasswordActivity;
import id.ac.binus.taskflow.MainActivity;
import id.ac.binus.taskflow.R;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private LinearLayout changePasswordLayout;
    private RelativeLayout logoutLayout;
    private SwitchCompat darkModeSwitch;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }

        initViews(view);
        loadUserData();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        usernameTextView = view.findViewById(R.id.text_username);
        emailTextView = view.findViewById(R.id.text_email);
        changePasswordLayout = view.findViewById(R.id.layout_change_password);
        logoutLayout = view.findViewById(R.id.layout_logout);
        darkModeSwitch = view.findViewById(R.id.switch_dark_mode);
    }

    private void loadUserData() {
        if (userId == null) return;

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            emailTextView.setText(user.getEmail());
        }

        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        Boolean darkMode = documentSnapshot.getBoolean("darkMode");

                        if (username != null) {
                            usernameTextView.setText(username);
                        }
                        if (darkMode != null) {
                            darkModeSwitch.setChecked(darkMode);
                        }
                    }
                });
    }

    private void setupListeners() {
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Save preference to Firestore
            if (userId != null) {
                firestore.collection("users").document(userId)
                        .update("darkMode", isChecked);
            }
        });

        changePasswordLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        logoutLayout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        firebaseAuth.signOut();
                        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
