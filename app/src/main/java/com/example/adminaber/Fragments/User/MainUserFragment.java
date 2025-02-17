package com.example.adminaber.Fragments.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Adapters.User.UserFragmentAdapter;
import com.example.adminaber.R;
import com.google.android.material.tabs.TabLayout;

public class MainUserFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private UserFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_main_user, container, false);

        tabLayout = root.findViewById(R.id.user_tab_layout);
        viewPager2 = root.findViewById(R.id.user_view_pager_2);

        tabLayout.addTab(tabLayout.newTab().setText("Manager"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat"));

        FragmentManager fragmentManager = getChildFragmentManager();
        adapter = new UserFragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                ;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return root;
    }
}