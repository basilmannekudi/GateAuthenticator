package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.sparksplugin.gateauthenticator.fragments.AllTokenFragment;
import com.sparksplugin.gateauthenticator.fragments.ApprovedFragment;
import com.sparksplugin.gateauthenticator.fragments.SettingsFragment;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
BottomNavigationView bottomNavigationView;
String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu_new);

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
    AllTokenFragment allTokenFragment = new AllTokenFragment();
    ApprovedFragment approvedFragment = new ApprovedFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menu_new:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment,allTokenFragment).commit();
                //return true;
                return true;
            case R.id.menu_approved:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment,approvedFragment).commit();
                return true;
            case R.id.menu_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment,settingsFragment).commit();
                return true;

        }
        return false;

    }
}