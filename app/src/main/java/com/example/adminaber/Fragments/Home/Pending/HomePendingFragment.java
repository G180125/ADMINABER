package com.example.adminaber.Fragments.Home.Pending;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.R;

public class HomePendingFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home_pending, container, false);

        PendingListFragment fragment = new PendingListFragment();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_pending_container, fragment)
                .addToBackStack(null)
                .commit();

        return root;
    }
}