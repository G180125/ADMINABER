package com.example.adminaber.Fragments.Driver.Chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.R;

public class DriverChatFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_chat, container, false);
        DriverChatListFragment fragment = new DriverChatListFragment();

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_driver_chat_container, fragment)
                .addToBackStack(null)
                .commit();

        return root;
    }
}