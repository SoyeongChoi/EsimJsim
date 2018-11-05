package com.example.chm31.esimjsim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SafetyFragment extends Fragment{
    public SafetyFragment() {
        // Required empty public constructor
    }
    private static View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_safety, container, false);

        return rootView;
    }
}