package com.example.adminaber.Fragments.User.Manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.R;

public class UserManagerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_user_manager, container, false);

        UserManagerListFragment fragment = new UserManagerListFragment();

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_user_manager_container, fragment)
                .addToBackStack(null)
                .commit();

        return root;
    }
}