package id.ac.binus.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signup extends AppCompatActivity {

    private EditText msignuppassword, msignupemail;
    private RelativeLayout msignup;
    private TextView mgotologin;

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

        getActionBar().hide();

        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
            });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Mail And Password", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 7){
                    Toast.makeText(getApplicationContext(), "Password Must Be At Least 7 Characters", Toast.LENGTH_SHORT).show();
                }
                else{
                    //registered the user to firebase
                }
            }
        });

    }
}