package com.example.adminaber.Fragments.User.Chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Fragments.User.Manager.UserManagerListFragment;
import com.example.adminaber.R;

public class UserChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_chat, container, false);
        UserChatListFragment fragment = new UserChatListFragment();

        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_main_chat_container, fragment)
                .addToBackStack(null)
                .commit();

        return root;
    }
}