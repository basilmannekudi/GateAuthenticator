package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.sparksplugin.gateauthenticator.R;

import java.util.Objects;

public class DataCollectionActivity extends AppCompatActivity {
    AutoCompleteTextView semester;
    AutoCompleteTextView branch;
    AutoCompleteTextView batch;
    EditText mRegistration;
    Button mSubmit;
    TextInputLayout semLay;
    TextInputLayout braLay;
    TextInputLayout batLay;
    TextInputLayout regLay;
    String UID;
    String reg;
    String sem;
    String admin;
    String bat;
    String branc;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.activity_data_collection);
        semester = findViewById(R.id.sem);
        regLay = findViewById(R.id.regfilled);
        braLay = findViewById(R.id.branch_id);
        batLay= findViewById(R.id.batch_id);
        semLay = findViewById(R.id.sem_id);
        branch = findViewById(R.id.branch);
        batch = findViewById(R.id.division);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRegistration = findViewById(R.id.reg_edittext);
        mSubmit = findViewById(R.id.submit_button);
        UID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String[] Sem = new String[]{"S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8"};
        String[] bra = new String[]{"ECE", "EEE", "MECH","CS","IT"};
        String[] bat = new String[]{"A", "B"};
        ArrayAdapter<String> Semadapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Sem);
        semester.setAdapter(Semadapter);
        ArrayAdapter<String> Braadapter = new ArrayAdapter<>(this, R.layout.dropdown_item, bra);
        branch.setAdapter(Braadapter);
        ArrayAdapter<String> Batadapter = new ArrayAdapter<>(this, R.layout.dropdown_item, bat);
        batch.setAdapter(Batadapter);

        semester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        branch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        batch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();

            }
        });

    }

    private void uploadData() {
        if(mRegistration.getText().toString().equals(""))
        {
            regLay.setError("Required Field");

        }
        else if(semester.getText().toString().equals(""))
        {
            semLay.setError("Field Empty");

        }
        else if(branch.getText().toString().equals(""))
        {
            braLay.setError("Field Empty");

        }
        else if(batch.getText().toString().equals(""))
        {
            batLay.setError("Field Empty");

        }

        else
        {
            reg = mRegistration.getText().toString();
            bat = batch.getText().toString();
            branc = branch.getText().toString();
            sem = semester.getText().toString();
            mDatabase.child("Common").child("Dept").child(branc).child(sem).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    admin = snapshot.getValue(String.class);
                    mDatabase.child("User").child(admin).child("username").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String adName = snapshot.getValue(String.class);
                            buildAlert(adName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }

    public void upload2()
    {

        mDatabase.child("User").child(UID).child("id").setValue(reg);
        mDatabase.child("User").child(UID).child("sem").setValue(sem);
        mDatabase.child("User").child(UID).child("branch").setValue(branc);
        mDatabase.child("User").child(UID).child("type").setValue("user");
        mDatabase.child("User").child(UID).child("supid").setValue(admin);
        mDatabase.child("User").child(UID).child("isdataset").setValue(true);
        mDatabase.child("User").child(UID).child("batch").setValue(bat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(DataCollectionActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DataCollectionActivity.this,
                        HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
    public void buildAlert(String Name)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Supervisor");
        final View customLayout = getLayoutInflater().inflate(R.layout.show_admin_dialog, null);
        builder.setView(customLayout);
        TextView supName = customLayout.findViewById(R.id.super_id);
        supName.setText(Name);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {
                                upload2();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

