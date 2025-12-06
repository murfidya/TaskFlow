package id.ac.binus.taskflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class forgotpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        mforgotpassword = findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        mgobacktologin = findViewById(R.id.gobacktologin);


        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Your Mail", Toast.LENGTH_SHORT).show();
                }
                else {
                    //we have to send password recover email
                    //Toast.makeText(getApplicationContext(), "Password Has Been Sent To Your Mail", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}