package com.sparksplugin.gateauthenticator.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sparksplugin.gateauthenticator.R;
import com.sparksplugin.gateauthenticator.ScanActivity2;
import com.sparksplugin.gateauthenticator.activity.AddTokenActivity;

public class ScanFragment extends Fragment {
    Button Scan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_scan, container, false);
        Scan = view.findViewById(R.id.scan_button);
        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),
                        ScanActivity2.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "security");
                bundle.putString("key", "null");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        return view;
    }
}