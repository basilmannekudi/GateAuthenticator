package com.sparksplugin.gateauthenticator.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sparksplugin.gateauthenticator.FcmNotificationsSender;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.ScanActivity2;
import com.sparksplugin.gateauthenticator.model.Tokens;
import com.sparksplugin.gateauthenticator.model.User;

import java.util.Objects;

public class TokenActivity extends AppCompatActivity {
    TextView mName;
    TextView mClass;
    TextView mEorE;
    TextView Ee;
    TextView mDate;
    TextView mTime;
    TextView mVerified;
    TextView mToken;
    TextView mExp;
    TextView mReas;
    Button btn_qr;
    Button btn_ok;
    Button btn_verify;
    Button btn_approve;
    ImageView qr_image;
    String key;
    Tokens token;
    User user;
    String UID;
    ImageView Ontop;
    TextView regId;
    LinearLayout view;
    String uType;
    String senId;
    String supName;
    String supId;
    ProgressBar pb;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap ;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        view = findViewById(R.id.view_lin);
        pb = findViewById(R.id.pbar);
        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mClass = findViewById(R.id.class_name);
        regId = findViewById(R.id.reg_id);
        mName = findViewById(R.id.user_name);
        mEorE = findViewById(R.id.type);
        mDate = findViewById(R.id.date);
        mTime = findViewById(R.id.time);
        Ee = findViewById(R.id.entry_or_exit);
        mReas = findViewById(R.id.reason);
        mVerified = findViewById(R.id.verif_id);
        mToken = findViewById(R.id.token_id);
        mExp = findViewById(R.id.exp_id);
        Ontop = findViewById(R.id.on_top);
        qr_image = findViewById(R.id.qr_gen);
        btn_qr = findViewById(R.id.Scan_qr);
        btn_ok = findViewById(R.id.ok_id);
        btn_approve = findViewById(R.id.approve);
        btn_verify = findViewById(R.id.Verify);
        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        mToken.setText(key);
        pb.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Requests").child(key).child("sender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 senId = snapshot.getValue(String.class);

                mDatabase.child("User").child(senId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        assert user != null;
                        regId.setText(user.getId());
                        String tex = user.getSem()+" "+user.getBranch()+" "+user.getBatch();
                        mClass.setText(tex);
                        supId = user.getSupid();
                        mDatabase.child("User").child(supId).child("username").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                supName = snapshot.getValue(String.class);
                                mDatabase.child("User").child(UID).child("type").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        uType = snapshot.getValue(String.class);
                                        if(uType.equals("user")) {
                                            btn_approve.setVisibility(View.GONE);
                                            btn_verify.setVisibility(View.GONE);
                                            btn_qr.setVisibility(View.GONE);
                                            if(!token.isVerified())
                                            {
                                                btn_approve.setVisibility(View.GONE);
                                                btn_verify.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_not_interested_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                setQr("NOT");
                                            }
                                            if(token.isExpired())
                                            {
                                                btn_approve.setVisibility(View.GONE);
                                                btn_verify.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                setQr("EXPIRED");
                                            }
                                            if (token.isVerified()&&!token.isExpired()) {
                                                btn_approve.setVisibility(View.GONE);
                                                btn_verify.setVisibility(View.GONE);
                                                    btn_qr.setVisibility(View.VISIBLE);
                                                    Ontop.setVisibility(View.GONE);
                                                    setQr(key);
                                                }


                                        }else if (uType.equals("super")) {
                                            btn_verify.setVisibility(View.GONE);
                                            btn_approve.setVisibility(View.GONE);
                                            btn_qr.setVisibility(View.GONE);
                                            if(!token.isVerified())
                                            {   btn_verify.setVisibility(View.VISIBLE);
                                                btn_approve.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_not_interested_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                setQr("NOT");

                                            }
                                            if(token.isExpired())
                                            {   btn_verify.setVisibility(View.GONE);
                                                btn_approve.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                setQr("EXPIRED");


                                            }
                                            if (token.isVerified()&&!token.isExpired()) {
                                                btn_qr.setVisibility(View.VISIBLE);
                                                Ontop.setVisibility(View.GONE);
                                                setQr(key);
                                                btn_approve.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                btn_verify.setVisibility(View.GONE);
                                            }

                                        }else if(uType.equals("security")){
                                            btn_verify.setVisibility(View.GONE);
                                            btn_qr.setVisibility(View.GONE);
                                            btn_approve.setVisibility(View.GONE);

                                            if(!token.isVerified())
                                            {   btn_verify.setVisibility(View.VISIBLE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_not_interested_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                btn_approve.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                btn_verify.setVisibility(View.GONE);
                                                setQr("NOT");

                                            }
                                            if(token.isExpired())
                                            {   btn_verify.setVisibility(View.GONE);
                                                Ontop.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                                                Ontop.setVisibility(View.VISIBLE);
                                                setQr("EXPIRED");
                                                btn_verify.setVisibility(View.GONE);
                                                btn_approve.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                            }
                                            if (token.isVerified()&&!token.isExpired()) {
                                                btn_qr.setVisibility(View.VISIBLE);
                                                Ontop.setVisibility(View.GONE);
                                                setQr(key);
                                                btn_verify.setVisibility(View.GONE);
                                                btn_qr.setVisibility(View.GONE);
                                                btn_approve.setVisibility(View.VISIBLE);

                                            }

                                        }
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
                        mDatabase.child("Requests").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                token = snapshot.getValue(Tokens.class);
                                assert token != null;
                                mName.setText(token.getUsername());
                                Ee.setText(token.getmType());
                                mEorE.setText(token.getmType());
                                mDate.setText(token.getZdate());
                                mTime.setText(token.getZtime());
                                mReas.setText(token.getReason());
                                mVerified.setText(token.getSupername());
                                pb.setVisibility(View.GONE);


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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        setQr(key);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ScanActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "user");
                bundle.putString("key", key);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlert("verify");
            }
        });
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlert("approve");
            }
        });

    }

    private void buildAlert(String val) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Enter Passkey");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.edit_alert_dialog,
                        null);
        builder.setView(customLayout);
        builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {

                                // send data from the
                                // AlertDialog to the Activity
                                EditText editText = customLayout.findViewById(R.id.editText);
                                Alert2(editText.getText().toString(),val);
                            }
                        });
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void Alert2(String val,String type) {
        if (type.equals("approve"))
        {
            if (val.equals("#approve"))
            {
                approve();
            }
            else {
                Snackbar.make(view, "Wrong Passkey", Snackbar.LENGTH_LONG).show();
            }
        }else if (type.equals("verify"))
        {
            if (val.equals("#verify"))
            {
                verify();
            }
            else {
                Snackbar.make(view, "Wrong Passkey", Snackbar.LENGTH_LONG).show();
            }
        }
    }



    private void checkExpiry()
    {

    }
    private void approve() {
        mDatabase.child("Requests").child(key).child("approved").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDatabase.child("Requests").child(key).child("expired").setValue(true);
                mDatabase.child("approved").child(key).child("id").setValue(key).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(view, "Token Approved", Snackbar.LENGTH_LONG).show();
                        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("approvedlist")
                                .child(token.getSender());
                        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()){
                                    chatRef.child("id").setValue(token.getSender());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token.getSender(),"Request to "+token.getmType(),
                                "Your Request is Approved by the Security",getApplicationContext(),TokenActivity.this,key,UID);
                        notificationsSender.SendNotifications();
                        Intent intent = new Intent(getApplicationContext(),TokenActivity.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }



    private void verify() {
        mDatabase.child("Requests").child(key).child("verified").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDatabase.child("Requests").child(key).child("supername").setValue(supName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token.getSender(),"Request to "+token.getmType(),
                                "Your Request is Approved by "+supName,getApplicationContext(),TokenActivity.this,key,UID);
                        notificationsSender.SendNotifications();
                        Snackbar.make(view, "Token Verified", Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),
                                TokenActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("key", key);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public void setQr(String key)
    {

        try {
            bitmap = TextToImageEncode(key);

            qr_image.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.light_blue_900):getResources().getColor(R.color.QRCodeWhiteColor);
            }


        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}