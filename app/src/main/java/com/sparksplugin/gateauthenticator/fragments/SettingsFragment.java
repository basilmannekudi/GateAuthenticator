package com.sparksplugin.gateauthenticator.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.activity.DataCollectionActivity;
import com.sparksplugin.gateauthenticator.activity.HomeActivity;
import com.sparksplugin.gateauthenticator.activity.LoginActivity;

import java.util.Objects;


public class SettingsFragment extends Fragment {
    Button Logout;
    String UID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Logout = view.findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(UID);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),
                        LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}