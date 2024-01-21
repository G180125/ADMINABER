package com.example.adminaber.Fragments.Home;

import android.content.Intent;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Activities.LoginActivity;
import com.example.adminaber.BookingListFragment;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Fragments.Home.Pending.HomePendingFragment;
import com.example.adminaber.Fragments.Home.Settings.HomeSettingsFragment;
import com.example.adminaber.Fragments.Home.Statistic.HomeStatisticFragment;
import com.example.adminaber.PolicyListFragment;
import com.example.adminaber.R;
import com.example.adminaber.UserPolicyFragment;
import com.google.android.material.tabs.TabLayout;

public class MainHomeFragment extends Fragment {
    private FirebaseManager firebaseManager;
    private CardView staticCardView, bookingCardView, policyCardView, pendingCardView, settingCardView, logoutCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_home, container, false);
        firebaseManager = new FirebaseManager();

        staticCardView = root.findViewById(R.id.static_card);
        bookingCardView = root.findViewById(R.id.booking_card);
        policyCardView = root.findViewById(R.id.policy_card);
        pendingCardView = root.findViewById(R.id.pending_card);
        settingCardView = root.findViewById(R.id.setting_card);
        logoutCardView = root.findViewById(R.id.logout_card);

        bookingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new BookingListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        pendingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new HomePendingFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        policyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new PolicyListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseManager.mAuth.signOut();
                requireActivity().finish();
                startActivity(new Intent(requireContext(), LoginActivity.class));
            }
        });

        staticCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new HomeStatisticFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        settingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new HomeSettingsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}