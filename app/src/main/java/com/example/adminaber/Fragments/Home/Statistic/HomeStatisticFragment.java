package com.example.adminaber.Fragments.Home.Statistic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Image;
import com.anychart.graphics.vector.Stroke;
import com.example.adminaber.Adapters.Statistic.StatChartAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomeStatisticFragment extends Fragment {

    private ImageView buttonBack;
    private FirebaseManager firebaseManager;
    private int maleData,femaleData;
    WormDotsIndicator wormDotsIndicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_statistic, container, false);  // Replace with your Fragment layout

        buttonBack = view.findViewById(R.id.back);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        StatChartAdapter adapter = new StatChartAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);


        wormDotsIndicator = view.findViewById(R.id.worm_dots_indicator);
        wormDotsIndicator.attachTo(viewPager);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new MainHomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });




        return view;
    }


}

