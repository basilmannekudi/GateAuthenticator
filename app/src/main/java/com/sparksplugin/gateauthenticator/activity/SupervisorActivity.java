package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.fragments.SettingsFragment;
import com.sparksplugin.gateauthenticator.fragments.SupAllFragment;
import com.sparksplugin.gateauthenticator.fragments.SupApprovedFragment;
import com.sparksplugin.gateauthenticator.fragments.SupUserFragment;

import java.util.Objects;

public class SupervisorActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.sup_all);
        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        subscribePush();
    }
    private void subscribePush() {
        FirebaseMessaging.getInstance().subscribeToTopic(UID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    SupApprovedFragment approvedFragment = new SupApprovedFragment();
    SupAllFragment allTokenFragment = new SupAllFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    SupUserFragment userFragment = new SupUserFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.sup_all:
                getSupportFragmentManager().beginTransaction().replace(R.id.supervisor_fragment,allTokenFragment).commit();
                return true;
            case R.id.sup_approv:
                getSupportFragmentManager().beginTransaction().replace(R.id.supervisor_fragment,approvedFragment).commit();
                return true;
            case R.id.sup_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.supervisor_fragment,settingsFragment).commit();
                return true;
            case R.id.sup_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.supervisor_fragment, userFragment).commit();
                return true;

        }

        return false;

    }
}