package com.example.adminaber;

import static com.example.adminaber.Utils.AndroidUtil.replaceFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Fragments.Home.Settings.Language.LanguageSettingsFragment;

public class PolicyListFragment extends Fragment {
    private ImageView buttonBack;
    private TextView userPolicyTextView, driverPolicyTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_policy_list, container, false);
        buttonBack = root.findViewById(R.id.back);
        userPolicyTextView = root.findViewById(R.id.user_policy);
        driverPolicyTextView = root.findViewById(R.id.driver_policy);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new MainHomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        userPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                replaceFragment(new UserPolicyFragment(),fragmentManager,fragmentTransaction,R.id.fragment_main_container);
            }
        });

        driverPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                replaceFragment(new DriverPolicyFragment(),fragmentManager,fragmentTransaction,R.id.fragment_main_container);
            }
        });

        return root;
    }
}