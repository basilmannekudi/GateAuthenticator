package com.sparksplugin.gateauthenticator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.adapter.TokenAdapter;
import com.sparksplugin.gateauthenticator.model.Tokens;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TokenAdapter tokenAdapter;
    private List<Tokens> mTokens;
    String val;
    String uId;
    String name;
    TextView uname;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Bundle bundle = getIntent().getExtras();
        val = bundle.getString("val");
        uId = bundle.getString("userid");
        name = bundle.getString("name");
        uname = findViewById(R.id.user);
        uname.setText(name);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        readTokens(uId);
    }
    private void readTokens(String id){
        mTokens = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Requests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTokens.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Tokens tokens = snapshot.getValue(Tokens.class);
                    assert tokens != null;
                    try {
                        if(val.equals("security")) {
                            if(tokens.getSender().equals(id)){
                                if (tokens.isApproved()) {
                                    mTokens.add(tokens);
                                }

                            }

                        }
                        else if(val.equals("super")) {
                            if (tokens.getSender().equals(id)) {
                                mTokens.add(tokens);
                            }
                        }
                    }catch (Exception e)
                    {
                       // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    tokenAdapter = new TokenAdapter(getApplicationContext(),mTokens);
                    recyclerView.setAdapter(tokenAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}