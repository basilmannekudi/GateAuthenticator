package com.sparksplugin.gateauthenticator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sparksplugin.gateauthenticator.activity.TokenActivity;
import com.sparksplugin.gateauthenticator.model.External;

import java.util.Objects;

public class ScanActivity2 extends AppCompatActivity {

    String type;
    String key;
    String UID;
    private static final int PERMISSION_REQUEST_CAMERA =50 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan2);
        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        key = bundle.getString("key");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        //initiali intent integrator
        IntentIntegrator intentIntegrator=new IntentIntegrator(ScanActivity2.this);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt("Hold on Tight :)");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                finish();
            } else {
                if(type.equals("security"))
                {
                    securityService(intentResult.getContents());
                }
                else if(type.equals("user"))
                {
                    userService(intentResult.getContents());
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void userService(String contents) {
        if(contents.equals("CHECK_QR"))
        {
            checkQr();
        }
        else
        {
            buildNon();
        }

    }

    private void buildNon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Scan !");
        final View customLayout = getLayoutInflater().inflate(R.layout.invalid_layout, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(
                    DialogInterface dialog,
                    int which)
            {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void checkQr() {

        FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
        rootRef.getReference("external").child("id").setValue(key);
        rootRef.getReference("external").child("activate").setValue(true);
        rootRef.getReference("external").child("ui").setValue(UID);
        rootRef.getReference("external").child("response").setValue("null").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            buildResponse();

            }
        });
    }

    private void buildResponse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Keep Smiling !");
        final View customLayout = getLayoutInflater().inflate(R.layout.response_layout, null);
        builder.setView(customLayout);
        TextView Text = customLayout.findViewById(R.id.approv_id);
        ProgressBar pb = customLayout.findViewById(R.id.pbar);
        Button btn = customLayout.findViewById(R.id.ok_id);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("external").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                External external = snapshot.getValue(External.class);
                assert external != null;
                if(!external.isActivate()){
                    pb.setVisibility(View.GONE);
                    btn.setVisibility(View.VISIBLE);
                    Text.setText(external.getResponse());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),TokenActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void securityService(String contents) {
        try {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Requests").child(contents);
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Intent intent = new Intent(getApplicationContext(),
                                TokenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", contents);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    } else {
                        buildForgery();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed, how to handle?

                }

            });
        }catch (Exception e)
        {
         //   Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            buildForgery();
        }

    }
    public void buildForgery()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Possible Forgery !");
        final View customLayout = getLayoutInflater().inflate(R.layout.forgery_layout, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(
                    DialogInterface dialog,
                    int which)
            {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera access is required.",
                    Toast.LENGTH_LONG).show();


            // Request the permission
            ActivityCompat.requestPermissions(ScanActivity2.this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            Toast.makeText(this,
                    "Accessing Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }
}