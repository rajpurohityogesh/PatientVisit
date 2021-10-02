package com.example.patientvisit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore db;

    private EditText email;
    private EditText pswd;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        pswd = findViewById(R.id.password);
        register = findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_ = email.getText().toString();
                String j = pswd.getText().toString();
                if (email_.isEmpty()) {
                    email.requestFocus();
                    email.setError("Field cannot be Empty");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_).matches()) {
                    email.requestFocus();
                    email.setError("Please enter valid Email-address!!");

                } else if (j.isEmpty()) {
                    pswd.requestFocus();
                    pswd.setError("Field cannot be Empty");

                } else if (pswd.getText().length() < 6 || pswd.getText().length() > 14) {
                    pswd.requestFocus();
                    pswd.setError("Please enter valid password");
                } else if (!(email_.isEmpty() && j.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email_, j)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Please try again!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String userId = mFirebaseAuth.getCurrentUser().getUid();
                                        db.collection(userId);
                                        Toast.makeText(RegisterActivity.this, "Your record has been saved successfully!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                    }
                                }
                            }
                    );
                } else {
                    Toast.makeText(RegisterActivity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}





