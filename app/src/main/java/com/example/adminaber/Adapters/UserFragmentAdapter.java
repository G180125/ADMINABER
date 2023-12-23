package com.example.adminaber.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.adminaber.Fragments.User.Chat.UserChatFragment;
import com.example.adminaber.Fragments.User.Manager.UserManagerFragment;

public class UserFragmentAdapter extends FragmentStateAdapter {
    public UserFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){
            return new UserChatFragment();
        }
        return new UserManagerFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
