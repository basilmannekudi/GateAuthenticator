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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sparksplugin.gateauthenticator.activity.AddTokenActivity;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.adapter.TokenAdapter;

import com.sparksplugin.gateauthenticator.model.Chatlist;
import com.sparksplugin.gateauthenticator.model.Tokens;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllTokenFragment extends Fragment {
    private RecyclerView recyclerView;
    private TokenAdapter tokenAdapter;
    private List<Tokens> mTokens;
    DatabaseReference reference;
    FloatingActionButton mAddAlarmFab, mAddPersonFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;
    String UID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_token, container, false);
        UID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAddFab = view.findViewById(R.id.add_fab);
        mAddAlarmFab = view.findViewById(R.id.add_alarm_fab);
        mAddPersonFab = view.findViewById(R.id.add_person_fab);
        addAlarmActionText = view.findViewById(R.id.add_alarm_action_text);
        addPersonActionText = view.findViewById(R.id.add_person_action_text);
        mAddAlarmFab.setVisibility(View.GONE);
        mAddPersonFab.setVisibility(View.GONE);
        addAlarmActionText.setVisibility(View.GONE);
        addPersonActionText.setVisibility(View.GONE);
        isAllFabsVisible = false;
        mAddFab.shrink();
        mAddFab.setOnClickListener(
                view1 -> {
                    if (!isAllFabsVisible) {
                        mAddAlarmFab.show();
                        mAddPersonFab.show();
                        addAlarmActionText.setVisibility(View.VISIBLE);
                        addPersonActionText.setVisibility(View.VISIBLE);
                        mAddFab.extend();
                        isAllFabsVisible = true;
                    } else {
                        mAddAlarmFab.hide();
                        mAddPersonFab.hide();
                        addAlarmActionText.setVisibility(View.GONE);
                        addPersonActionText.setVisibility(View.GONE);
                        mAddFab.shrink();
                        isAllFabsVisible = false;
                    }
                });
        mAddPersonFab.setOnClickListener(
                view12 -> {
                    Intent intent = new Intent(getContext(),
                            AddTokenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "Entry");
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
        mAddAlarmFab.setOnClickListener(
                view13 -> {
                    Intent intent = new Intent(getContext(),
                            AddTokenActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "Exit");
                    intent.putExtras(bundle);
                    startActivity(intent);
                });




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
                            mTokens.add(tokens);

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

