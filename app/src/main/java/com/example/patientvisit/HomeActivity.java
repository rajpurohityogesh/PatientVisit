package com.example.patientvisit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button btnlogout;
    FloatingActionButton visitadd;
    FirebaseAuth mFirebaseAuth;
    private Context mcontext;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter patientRecyclerAdapter;
    FirebaseFirestore db;
    String userID;
    FirebaseAuth auth;
    List<Patient> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        btnlogout=findViewById(R.id.logout);
//        btnlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent i=new Intent(HomeActivity.this,MainActivity.class);
//                startActivity(i);
//            }
//        });
        auth=FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        patientRecyclerAdapter = new MyAdapter(patientList, this);
//        recyclerView.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View view) {
//                                                AlertDialog.Builder builder = new AlertDialog.Builder();
//                                                builder.setTitle("Choose option");
//                                                builder.setMessage("Update or delete user?");
//                                                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//
//                                                        //go to update activity
//
//                                                    }
//                                                });
//                                            }
//                                        });
//        db.collection(userID).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if (!queryDocumentSnapshots.isEmpty()) {
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for (DocumentSnapshot d : list) {
//                                product pd = d.toObject(product.class);
//                                pd.setId(d.getId());
//                                patientList.add(pd);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("fbErr",e.toString());
//                        Toast.makeText(HomeActivity.this, "Something Went Wrong!!!"+e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });

        recyclerView.setAdapter(patientRecyclerAdapter);
        showData();

        visitadd = findViewById(R.id.add);
        visitadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddVisitActivity.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Patient newVisit = data.getParcelableExtra("newVisit");
                saveToFireStrore(newVisit);
                patientList.add(newVisit);
                patientRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showData(){
        final ProgressDialog dialog = new ProgressDialog(HomeActivity.this);
        dialog.setMessage("Patient Visits...");
        dialog.show();
        db.collection(userID).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    patientList.clear();
                    for(DocumentSnapshot snapshot : task.getResult()){
                        Patient patient = new Patient(snapshot.getString("id")
                                ,snapshot.getString("address"),snapshot.getString("age")
                                ,snapshot.getString("cost"),snapshot.getString("gender")
                                ,snapshot.getString("medical_description"),snapshot.getString("name")
                                ,snapshot.getString("phone_no"),snapshot.getString("starting_date"));
                        patientList.add(patient);
                    }
                    dialog.dismiss();
                    patientRecyclerAdapter.notifyDataSetChanged();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void saveToFireStrore(Patient patient){
        final boolean[] result = {true};
        Map<String,Object> document = new HashMap<>();
        document.put("id",patient.getId());
        document.put("name",patient.getName());
        document.put("medical_description",patient.getMedical_description());
        document.put("address",patient.getAddress());
        document.put("phone_no",patient.getPhone_no());
        document.put("cost",patient.getCost());
        document.put("age",patient.getAge());
        document.put("starting_date",patient.getStarting_date());
        document.put("gender", patient.getGender());
        db.collection(userID).document().set(document)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Patient Visit Added!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        Log.d("fbErr",e.toString());
                        Toast.makeText(HomeActivity.this, "Error Occured : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        result[0] = false;
                    }
                });
    }
}