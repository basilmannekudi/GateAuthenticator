package com.sparksplugin.gateauthenticator.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sparksplugin.gateauthenticator.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    String pass;
    String email;
    private FirebaseAuth mAuth;
    EditText mEmail;
    EditText mPass;
    Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(getResources().getColor(R.color.white));
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors

        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mEmail= findViewById(R.id.editTextEmail);
        mPass= findViewById(R.id.editTextPassword);
        mLogin=findViewById(R.id.cirLoginButton);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=mEmail.getText().toString().trim();
                signIn(mEmail.getText().toString().trim(),mPass.getText().toString().trim());
            }
        });


    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View View){
        Intent intent = new Intent(LoginActivity.this,
                RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        //finish();

    }
    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(),
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

}