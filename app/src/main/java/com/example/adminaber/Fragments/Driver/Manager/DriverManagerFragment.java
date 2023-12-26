package com.example.adminaber.Fragments.Driver.Manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Fragments.User.Manager.UserManagerListFragment;
import com.example.adminaber.R;

public class DriverManagerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_manager, container, false);
        DriverManagerListFragment fragment = new DriverManagerListFragment();

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_driver_manager_container, fragment)
                .addToBackStack(null)
                .commit();

        return root;
    }
}