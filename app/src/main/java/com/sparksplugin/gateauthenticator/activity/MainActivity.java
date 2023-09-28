package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.model.User;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            try {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                String UID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                mDatabase.child("User").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //String Val = snapshot.getValue(String.class);
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        String Val = user.getType();

                        if(Val .equals("user"))
                        {
                                    if(user.isIsdataset()) {
                                        Intent intent = new Intent(MainActivity.this,
                                                HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(MainActivity.this,
                                                DataCollectionActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                        }
                        else if(Val .equals("super"))
                        {

                            Intent intent = new Intent(MainActivity.this,
                                    SupervisorActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else if(Val .equals("security"))
                        {

                            Intent intent = new Intent(MainActivity.this,
                                    SecurityActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }catch (Exception e)
            {

               // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
                }
            },2000);

        } else {
            Intent intent = new Intent(MainActivity.this,
                    RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }



}