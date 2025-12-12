package id.ac.binus.taskflow;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private RelativeLayout changePasswordButton;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();

        initViews();
        setupListeners();
    }

    private void initViews() {
        backButton = findViewById(R.id.btn_back);
        currentPasswordEditText = findViewById(R.id.edit_current_password);
        newPasswordEditText = findViewById(R.id.edit_new_password);
        confirmPasswordEditText = findViewById(R.id.edit_confirm_password);
        changePasswordButton = findViewById(R.id.btn_change_password);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        changePasswordButton.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (currentPassword.isEmpty()) {
            Toast.makeText(this, "Please enter current password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 7) {
            Toast.makeText(this, "New password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        changePasswordButton.setEnabled(false);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility(View.GONE);
                                                changePasswordButton.setEnabled(true);

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this,
                                                            "Password changed successfully",
                                                            Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(ChangePasswordActivity.this,
                                                            "Failed to change password: " + task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                changePasswordButton.setEnabled(true);
                                Toast.makeText(ChangePasswordActivity.this,
                                        "Current password is incorrect",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
