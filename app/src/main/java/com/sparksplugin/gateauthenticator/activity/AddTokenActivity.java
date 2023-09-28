package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.sparksplugin.gateauthenticator.FcmNotificationsSender;
import com.sparksplugin.gateauthenticator.R;

import java.util.Calendar;
import java.util.Objects;

public class AddTokenActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    Button SetTime;
    Button addToken;
    String zdate;
    String ztime;
    String username;
    String key;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear;
    int mhour,mminute;
    TextView type,timeId,dateId,supId;
    String supName;
    String suparId;
    EditText reason;
    Boolean timeset=false;
    TextView err;
    String title;
    TextInputLayout box;
    String  UID;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        Bundle bundle = getIntent().getExtras();
         title = bundle.getString("type");
        type = findViewById(R.id.type_id);
        String val = title+" "+"at";
        type.setText(val);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        timeId = findViewById(R.id.time_id);
        dateId = findViewById(R.id.date_id);
        supId = findViewById(R.id.supervisor_id);
        reason = findViewById(R.id.reg_edittext);
        err= findViewById(R.id.error_id);
        box = findViewById(R.id.regfilled);
        UID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mDatabase.child("User").child(UID).child("supid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                suparId  = snapshot.getValue(String.class);
                mDatabase.child("User").child(suparId).child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        supName  = snapshot.getValue(String.class);
                       // Toast.makeText(AddTokenActivity.this, supName,Toast.LENGTH_SHORT).show();
                        supId.setText(supName);
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

        mDatabase.child("User").child(UID).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username  = snapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        addToken = findViewById(R.id.subtok);
        addToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addtokenfun();
            }
        });


        SetTime = findViewById(R.id.datetime);
        SetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setdate();
            }
        });


    }

    private void addtokenfun() {
       if(reason.getText().toString().equals("")) {
           //
           box.setError("Required Field");
       }
        else if(!timeset)
       {
           //
           String error = "* please set date and time";
           err.setText(error);
           err.setVisibility(View.VISIBLE);
       }
        else
       {
           DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
           DatabaseReference newRef = reference.child("Requests").push();
           key = newRef.getKey();
           newRef.child("reason").setValue(reason.getText().toString());
           newRef.child("day").setValue(myday);
           newRef.child("month").setValue(myMonth);
           newRef.child("year").setValue(myYear);
           newRef.child("minute").setValue(mminute);
           newRef.child("hour").setValue(mhour);
           newRef.child("verified").setValue(false);
           newRef.child("expired").setValue(false);
           newRef.child("approved").setValue(false);
           newRef.child("mtype").setValue(title);
           newRef.child("supername").setValue("Not Verified");
           newRef.child("sender").setValue(UID);
           newRef.child("ztime").setValue(ztime);
           newRef.child("zdate").setValue(zdate);
           newRef.child("username").setValue(username);
           newRef.child("receiver").setValue(suparId);
           newRef.child("id").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   Toast.makeText(AddTokenActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                   String message = title+" Request by "+username+" on "+zdate+" at "+ztime;
                   FcmNotificationsSender notificationsSender = new FcmNotificationsSender(suparId,"Token Request",
                           message,getApplicationContext(),AddTokenActivity.this,key,UID);
                   notificationsSender.SendNotifications();
                   Intent intent = new Intent(getApplicationContext(),
                           TokenActivity.class);
                   Bundle bundle = new Bundle();
                   bundle.putString("key", key);
                   intent.putExtras(bundle);
                   startActivity(intent);
                   finish();
               }
           });

           final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("tokenslist")
                   .child(suparId)
                   .child(UID);
           chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (!dataSnapshot.exists()){
                       chatRef.child("id").setValue(UID);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }

    }

    public void Setdate()
    {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddTokenActivity.this, AddTokenActivity.this, year, month, day);
        datePickerDialog.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month+1;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTokenActivity.this, AddTokenActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(this, myday+" "+myMonth+ " "+myYear, Toast.LENGTH_SHORT).show();
        mhour = hourOfDay;
        mminute = minute;
        zdate = myday+" / "+myMonth+ " / "+myYear;
        ztime = mhour+" : "+mminute;
        timeId.setText(ztime);
        dateId.setText(zdate);
        timeset = true;
    }
}
