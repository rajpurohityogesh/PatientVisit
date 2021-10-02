package com.example.patientvisit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateVisitActivity extends AppCompatActivity {
    private EditText name;
    private EditText medical;
    private EditText add;
    private EditText phone;
    private EditText age;
    private EditText cost;
    private FirebaseFirestore db;
    private Patient p;
    Button save;
    RadioGroup gender;
    EditText date;
    DatePickerDialog datePickerDialog;
    RadioButton maleRadioButton, femaleRadioButton;
    FirebaseAuth auth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatevisit);
        name=findViewById(R.id.p_name2);
        add=findViewById(R.id.p_add2);
        age=findViewById(R.id.p_age2);
        medical=findViewById(R.id.p_medical2);
        cost=findViewById(R.id.cst2);
        gender=findViewById(R.id.radioGroup2);
        phone=findViewById(R.id.p_phn2);
        save=findViewById(R.id.p_save2);
        final Boolean[] isDateChanged = {false};
        p = (Patient) getIntent().getSerializableExtra("product");
        name.setText(p.getName());
        age.setText(p.getAge());
        cost.setText(p.getCost());
        phone.setText(p.getPhone_no());
        add.setText(p.getAddress());
        medical.setText(p.getMedical_description());
        maleRadioButton=(RadioButton)findViewById(R.id.radioButton32);
        femaleRadioButton=(RadioButton)findViewById(R.id.radioButton42);

        date = (EditText) findViewById(R.id.date2);
        date.setText(p.getStarting_date());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Date picked");
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                isDateChanged[0] =true;
                datePickerDialog = new DatePickerDialog(UpdateVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Pressed line");
                String dt = date.getText().toString();
                String nm = name.getText().toString();
                String ad = add.getText().toString();
                String ag = age.getText().toString();
                String cst = cost.getText().toString();
                String med = medical.getText().toString();
                String phn = phone.getText().toString();
                String g="";
                if (maleRadioButton.isChecked()){
                    g="Male";
                }else if(femaleRadioButton.isChecked()){
                    g="Female";
                }
                //String gen=gender.;
                if (nm.isEmpty()) {
                    name.requestFocus();
                    name.setError("Field cannot be Empty");
                } else if (med.isEmpty()) {
                    medical.requestFocus();
                    medical.setError("Field cannot be Empty");
                } else if (ad.isEmpty()) {
                    add.requestFocus();
                    add.setError("Field cannot be Empty");
                } else if (phn.isEmpty()) {
                    phone.requestFocus();
                    phone.setError("Field cannot be Empty");
                } else if (phone.getText().length() != 10) {
                    phone.requestFocus();
                    phone.setError("Please enter valid phone number");

                } else if (ag.isEmpty()) {
                    age.requestFocus();
                    age.setError("Field cannot be Empty");
                } else if (cst.isEmpty()) {
                    cost.requestFocus();
                    cost.setError("Field cannot be Empty");
                } else if (dt.isEmpty()) {
                    date.requestFocus();
                    date.setError("Field cannot be Empty");

                }
                else
                {
                    userID=auth.getCurrentUser().getUid();
                    DocumentReference dbProducts=db.collection(userID).document(p.getId());
                    Map<String,Object> user=new HashMap<>();
                    user.put("name",nm);
                    user.put("medical_description",med);
                    user.put("address",ad);
                    user.put("phone_no",phn);
                    user.put("cost",cst);
                    user.put("age",ag);
                    user.put("starting_date",dt);
                    user.put("gender", g );

                    dbProducts.set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("msg","Patient details has been added!!"+userID);
                                Intent intent=new Intent(UpdateVisitActivity.this,HomeActivity.class);

                                finish();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("fbErr",e.toString());
                                        Toast.makeText(UpdateVisitActivity.this, "Something Went Wrong!!!"+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                        });
//                    patient patient=new patient(
//                            nm,
//                            med,
//                            ad,
//                            ag,
//                            phn,
//                            cst,
//                            dt
//
//                    );
//                    dbProducts.add(patient).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Toast.makeText(addVisit.this,"Patient deatils added",Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(addVisit.this,HomeActivity.class);
//                            startActivity(intent);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(addVisit.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                        }
//                    });

                }
            }
        });
    }
}