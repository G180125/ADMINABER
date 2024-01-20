package com.example.adminaber.Fragments.Home.Statistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.R;

import java.util.ArrayList;
import java.util.List;

public class GenderChartFragment extends Fragment {
    private FirebaseManager firebaseManager;

    private int maleData,femaleData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender_chart, container, false);

        firebaseManager = new FirebaseManager();
        firebaseManager.getAllUsersGender(new FirebaseManager.OnFetchGenderCountsListener() {
            @Override
            public void onFetchSuccess(int maleCount, int femaleCount) {
                maleData = maleCount;
                femaleData = femaleCount;
                Log.d("TestMale","Count : " + maleCount);

                AnyChartView pieChartView = view.findViewById(R.id.pie_chart_view);

                Pie pie = AnyChart.pie();

                pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                    @Override
                    public void onClick(Event event) {
                        Toast.makeText(requireContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                    }
                });





                pie.title(getString(R.string.pie_chart_title));

                pie.labels().position("outside");

                pie.legend().title().enabled(true);
                pie.legend().title()
                        .text(getString(R.string.genders))
                        .padding(0d, 0d, 10d, 0d);

                pie.legend()
                        .position("center-bottom")
                        .itemsLayout(LegendLayout.HORIZONTAL)
                        .align(Align.CENTER);

                pieChartView.setChart(pie);

                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry(getString(R.string.female), femaleData));
                Log.d("TestMaleAfterFunc","Count : " + maleData);
                data.add(new ValueDataEntry(getString(R.string.male), maleData));


                pie.data(data);
            }

            @Override
            public void onFetchFailure(String message) {

            }
        });

        return view;
    }
}