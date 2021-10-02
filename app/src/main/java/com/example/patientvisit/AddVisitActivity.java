package com.example.patientvisit;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


public class AddVisitActivity extends AppCompatActivity {
    public static final Pattern PHONE=
            Pattern.compile(
                    "(\\+[0-9]+[\\- \\.]*)?"
                            + "(\\([0-9]+\\)+[\\- \\.]*)?"
                            + "([0-9][0-9\\- \\.]+[0-9])");

    private EditText name;
    private EditText medical;
    private EditText add;
    private EditText phone;
    private EditText age;
    private EditText cost;
    Button save;
    RadioGroup gender;
    EditText date;
    DatePickerDialog datePickerDialog;

    String userID;
    FirebaseAuth auth;
    private FirebaseFirestore db;
    RadioButton maleRadioButton, femaleRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvisit);

        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        name=findViewById(R.id.p_name);
        add=findViewById(R.id.p_add);
        age=findViewById(R.id.p_age);
        medical=findViewById(R.id.p_medical);
        cost=findViewById(R.id.cst);
        gender=findViewById(R.id.radioGroup);
        phone=findViewById(R.id.p_phn);

        save=findViewById(R.id.p_save);
        final Boolean[] isDateChanged = {false};

        maleRadioButton=(RadioButton)findViewById(R.id.radioButton3);
        femaleRadioButton=(RadioButton)findViewById(R.id.radioButton4);

        date = (EditText) findViewById(R.id.date);
        // perform click event on edit text
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

                datePickerDialog = new DatePickerDialog(AddVisitActivity.this,
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
                String date_str = date.getText().toString();
                String name_str = name.getText().toString();
                String add_str = add.getText().toString();
                String age_str = age.getText().toString();
                String cost_str = cost.getText().toString();
                String medical_des_str = medical.getText().toString();
                String phone_str = phone.getText().toString();
                String gender_str="";
                if (maleRadioButton.isChecked()){
                    gender_str="Male";
                }else if(femaleRadioButton.isChecked()){
                    gender_str="Female";
                }
                //String gen=gender.;
                if (name_str.isEmpty()) {
                    name.requestFocus();
                    name.setError("Field cannot be Empty");
                } else if (medical_des_str.isEmpty()) {
                    medical.requestFocus();
                    medical.setError("Field cannot be Empty");
                } else if (add_str.isEmpty()) {
                    add.requestFocus();
                    add.setError("Field cannot be Empty");
                } else if (phone_str.isEmpty()) {
                    phone.requestFocus();
                    phone.setError("Field cannot be Empty");
                } else if (phone.getText().length() != 10) {
                    phone.requestFocus();
                    phone.setError("Please enter valid phone number");
                } else if (age_str.isEmpty()) {
                    age.requestFocus();
                    age.setError("Field cannot be Empty");
                } else if (cost_str.isEmpty()) {
                    cost.requestFocus();
                    cost.setError("Field cannot be Empty");
                } else if (date_str.isEmpty()) {
                    date.requestFocus();
                    date.setError("Field cannot be Empty");

                }
                else
                {
                    String id = UUID.randomUUID().toString();
                    Patient patientObj = new Patient();
                    patientObj.setId(id);
                    patientObj.setAddress(add_str);
                    patientObj.setAge(age_str);
                    patientObj.setCost(cost_str);
                    patientObj.setGender(gender_str);
                    patientObj.setMedical_description(medical_des_str);
                    patientObj.setName(name_str);
                    patientObj.setPhone_no(phone_str);
                    patientObj.setStarting_date(date_str);

                    Intent intent = new Intent();
                    intent.putExtra("newVisit",patientObj);
                    setResult(RESULT_OK,intent);
                    finish();
//                    userID=auth.getCurrentUser().getUid();
//                    DocumentReference dbProducts=db.collection(userID).document();
//                    Map<String,Object> user=new HashMap<>();
//                    user.put("name",name_str);
//                    user.put("medical_description",medical_des_str);
//                    user.put("address",add_str);
//                    user.put("phone_no",phone_str);
//                    user.put("cost",cost_str);
//                    user.put("age",age_str);
//                    user.put("starting_date",date_str);
//                    user.put("gender", gender_str);
//
//                    dbProducts.set(user)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d("msg","Patient details has been added!!"+userID);
//                                Intent intent=new Intent(AddVisitActivity.this,HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("fbErr",e.toString());
//                                Toast.makeText(AddVisitActivity.this, "Something Went Wrong!!!"+e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
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