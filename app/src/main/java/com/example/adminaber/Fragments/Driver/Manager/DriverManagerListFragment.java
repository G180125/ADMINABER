package com.example.adminaber.Fragments.Driver.Manager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminaber.Adapters.Home.DriverPendingAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Fragments.Home.Pending.DriverDetailFragment;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

public class DriverManagerListFragment extends Fragment implements DriverPendingAdapter.RecyclerViewClickListener{
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
        View root = inflater.inflate(R.layout.fragment_driver_manager_list, container, false);
        firebaseManager = new FirebaseManager();

        RecyclerView recyclerView = root.findViewById(R.id.recycler_manager_driver);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DriverPendingAdapter( new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        firebaseManager.getAllDrivers(new FirebaseManager.OnFetchDriverListListener() {
            @Override
            public void onFetchDriverListSuccess(List<Driver> list) {
                driverList = list;
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
            bundle.putString("previous", "manager_list");
            fragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .add(R.id.fragment_driver_manager_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}