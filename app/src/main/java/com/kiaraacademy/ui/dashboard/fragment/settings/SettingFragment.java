package com.kiaraacademy.ui.dashboard.fragment.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.kiaraacademy.R;
import com.kiaraacademy.data.SharedPreferenceManager;
import com.kiaraacademy.ui.signin.SigninActivity;

public class SettingFragment extends Fragment {

    AppCompatButton btn_logout;
    private AppCompatTextView tv_name;
    private AppCompatTextView tv_email;
    private AppCompatImageButton ibBackButton;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        tv_email = view.findViewById(R.id.tv_email);
        tv_name = view.findViewById(R.id.tv_name);
        ibBackButton = view.findViewById(R.id.ib_back_button);
        btn_logout = view.findViewById(R.id.btn_logout);

        tv_email.setText(SharedPreferenceManager.getEmailId());
        tv_name.setText(SharedPreferenceManager.getUserName() + " " + SharedPreferenceManager.getUserLastName());
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceManager.setSigninComplete(false);
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
