package com.example.adminaber.Fragments.Home;

import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Fragments.Home.Pending.HomePendingFragment;
import com.example.adminaber.R;
import com.google.android.material.tabs.TabLayout;

public class MainHomeFragment extends Fragment {
    private CardView staticCardView, bookingCardView, pendingCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_home, container, false);

        staticCardView = root.findViewById(R.id.static_card);
        bookingCardView = root.findViewById(R.id.booking_card);
        pendingCardView = root.findViewById(R.id.pending_card);

        pendingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_container, new HomePendingFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }
}