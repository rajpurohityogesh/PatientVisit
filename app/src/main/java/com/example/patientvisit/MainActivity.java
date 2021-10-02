package com.example.patientvisit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
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
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EditText email;
    private EditText pswd;
    private Button login;
    private Button register;

    //private Text text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();
        email = (EditText)findViewById(R.id.email);
        pswd = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.button);
        register=(Button)findViewById(R.id.button2);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "You are logged in!!", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Please Log in!!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
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
                    mFirebaseAuth.signInWithEmailAndPassword(email_,j).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Log.d("fbErr",task.getException().toString());
                                Toast.makeText(MainActivity.this, "Please try again!!", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this, "You logged in successfully!!", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(i);
                            }
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "Error Occured!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
