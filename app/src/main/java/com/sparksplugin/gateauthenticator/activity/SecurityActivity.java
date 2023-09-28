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
import com.sparksplugin.gateauthenticator.fragments.ScanFragment;
import com.sparksplugin.gateauthenticator.fragments.SecApprovFragment;
import com.sparksplugin.gateauthenticator.fragments.SecUserFragment;
import com.sparksplugin.gateauthenticator.fragments.SettingsFragment;

import java.util.Objects;

public class SecurityActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.activity_security);
        bottomNavigationView = findViewById(R.id.viewbottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.sec_scan);
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
    ScanFragment scanfragment = new ScanFragment();
    SecApprovFragment approvedFragment = new SecApprovFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    SecUserFragment userFragment = new SecUserFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.sec_scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.security_fragment,scanfragment).commit();
                return true;
            case R.id.sec_approved:
                getSupportFragmentManager().beginTransaction().replace(R.id.security_fragment,approvedFragment).commit();
                return true;
            case R.id.menu_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.security_fragment,settingsFragment).commit();
                return true;
            case R.id.sec_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.security_fragment,userFragment).commit();
                return true;

        }

        return false;

    }
}