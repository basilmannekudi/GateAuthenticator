package com.sparksplugin.gateauthenticator.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.ScanActivity2;
import com.sparksplugin.gateauthenticator.adapter.TokenAdapter;
import com.sparksplugin.gateauthenticator.model.Tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApprovedFragment extends Fragment {
    private RecyclerView recyclerView;

    private TokenAdapter tokenAdapter;
    private List<Tokens> mTokens;
    DatabaseReference reference;
    String UID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_approved, container, false);
        UID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        recyclerView = view.findViewById(R.id.recycler_view_1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        readTokens(UID);
        return view;
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
                        if(tokens.getSender().equals(id))
                        {
                            if(tokens.isVerified()) {
                                mTokens.add(tokens);
                            }

                        }
                    }catch (Exception e)
                    {
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    tokenAdapter = new TokenAdapter(getContext(),mTokens);
                    recyclerView.setAdapter(tokenAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}