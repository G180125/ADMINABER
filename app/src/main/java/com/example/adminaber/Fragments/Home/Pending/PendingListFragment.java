package com.example.adminaber.Fragments.Home.Pending;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.adminaber.Adapters.Home.DriverPendingAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

public class PendingListFragment extends Fragment implements DriverPendingAdapter.RecyclerViewClickListener{
    private List<Driver> driverList;
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private DriverPendingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pending_list, container, false);
        firebaseManager = new FirebaseManager();

        RecyclerView recyclerView = root.findViewById(R.id.recycler_driver_register);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DriverPendingAdapter( new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        firebaseManager.getAllPendingDriver(new FirebaseManager.OnFetchDriverListListener() {
            @Override
            public void onFetchDriverListSuccess(List<Driver> list) {
                driverList = list;
                Log.d("driver", "driverList:" + String.valueOf(driverList.size()));
                for (Driver driver: driverList){
                    Log.d("driver", "Checking driver: " + driver.getStatus());
                }
                updateUI(driverList);
            }

            @Override
            public void onFetchDriverListFailure(String message) {
                AndroidUtil.showToast(requireContext(), message);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }
        });

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<Driver> driverList){
        adapter.setUserList(driverList);
        adapter.notifyDataSetChanged();
        AndroidUtil.hideLoadingDialog(progressDialog);
    }

    @Override
    public void onCardClick(int position) {
        String id = driverList.get(position).getDocumentID();

        if(id != null){
            DriverDetailFragment fragment = new DriverDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("driverID", id);
            bundle.putString("previous", "pending_list");
            fragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_pending_container, fragment)
                    .commit();

        }
    }
}