package com.kiaraacademy.ui.dashboard.fragment.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kiaraacademy.R;

public class PurchaseFragment extends Fragment {

    private RecyclerView myCourse;

    public PurchaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        myCourse = view.findViewById(R.id.rv_my_course);
    }
}