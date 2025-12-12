package id.ac.binus.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText msignuppassword, msignupemail, msignupusername;
    private RelativeLayout msignup;
    private ImageView mgotologin;
    private ProgressBar mprogressbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        msignupusername = findViewById(R.id.signupusername);
        msignupemail = findViewById(R.id.signupemail);
        msignuppassword = findViewById(R.id.signuppassword);
        msignup = findViewById(R.id.signup);
        mgotologin = findViewById(R.id.gotologin);
        mprogressbar = findViewById(R.id.progressbarofsignup);

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = msignupusername.getText().toString().trim();
                String mail = msignupemail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();

                if (username.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 3) {
                    Toast.makeText(getApplicationContext(), "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 7) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
                } else {
                    // Disable button to prevent double clicks
                    msignup.setEnabled(false);
                    mprogressbar.setVisibility(View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(mail, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = authResult.getUser();
                                    if (user != null) {
                                        Log.d(TAG, "User created successfully: " + user.getUid());
                                        saveUserToFirestore(user.getUid(), username, mail);
                                    } else {
                                        hideProgressAndEnableButton();
                                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Registration failed", e);
                                    hideProgressAndEnableButton();
                                    Toast.makeText(getApplicationContext(), "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void hideProgressAndEnableButton() {
        mprogressbar.setVisibility(View.INVISIBLE);
        msignup.setEnabled(true);
    }

    private void saveUserToFirestore(String userId, String username, String email) {
        Log.d(TAG, "Saving user to Firestore: " + userId);

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("photoUrl", "");
        user.put("darkMode", false);
        user.put("createdAt", com.google.firebase.Timestamp.now());

        firestore.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User data saved successfully");
                        createDefaultCategories(userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to save user data", e);
                        hideProgressAndEnableButton();
                        Toast.makeText(getApplicationContext(), "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createDefaultCategories(String userId) {
        Log.d(TAG, "Creating default categories");

        // Use WriteBatch to create all categories at once
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

            // Create a new document reference for each category
            batch.set(firestore.collection("users").document(userId)
                    .collection("categories").document(), category);
        }

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Categories created successfully");
                        sendEmailVerification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to create categories", e);
                        // Continue anyway - categories can be created later
                        sendEmailVerification();
                    }
                });
    }

    private void sendEmailVerification() {
        Log.d(TAG, "Sending email verification");

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Verification email sent");
                            hideProgressAndEnableButton();
                            Toast.makeText(getApplicationContext(), "Registration Successful! Please check your email for verification.", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(signup.this, MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to send verification email: " + e.getMessage(), e);
                            hideProgressAndEnableButton();

                            // Account is created, just email verification failed
                            // User can still login (we'll handle verification check in login)
                            Toast.makeText(getApplicationContext(),
                                "Account created successfully! Email verification failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(signup.this, MainActivity.class));
                        }
                    });
        } else {
            Log.e(TAG, "Firebase user is null");
            hideProgressAndEnableButton();
            Toast.makeText(getApplicationContext(), "Registration completed. Please login.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(signup.this, MainActivity.class));
        }
    }
}
