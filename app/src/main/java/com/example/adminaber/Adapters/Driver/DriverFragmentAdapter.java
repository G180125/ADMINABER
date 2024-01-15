package com.example.adminaber.Adapters.Driver;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.adminaber.Fragments.Driver.Chat.DriverChatFragment;
import com.example.adminaber.Fragments.Driver.Manager.DriverManagerFragment;

public class DriverFragmentAdapter extends FragmentStateAdapter {
    public DriverFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){
            return new DriverChatFragment();
        }
        return new DriverManagerFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}