package com.example.adminaber.Adapters.Statistic;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.adminaber.Fragments.Home.Statistic.BookingChartFragment;
import com.example.adminaber.Fragments.Home.Statistic.GenderChartFragment;

public class StatChartAdapter extends FragmentPagerAdapter {

    public StatChartAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // Return a PieChartFragment instance for each page
        return new GenderChartFragment();

    }

    @Override
    public int getCount() {
        // Set the number of pages (e.g., 2 for two pie charts)
        return 2;
    }
}
