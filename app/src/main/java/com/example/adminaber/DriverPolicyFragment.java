package com.example.adminaber;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adminaber.Adapters.Home.PolicyAdapter;
import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Models.Staff.DriverPolicy;
import com.example.adminaber.Models.Staff.UserPolicy;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.Objects;

public class DriverPolicyFragment extends Fragment {
    private DriverPolicy driverPolicy;
    private PolicyAdapter documentPolicyAdapter, respectPolicyAdapter, lawPolicyAdapter, vehiclePolicyAdapter, practicePolicyAdapter;
    private ImageView addDocumentImageView, addRespectImageVIew, addLawImageView, addVehicleImageView, addPracticeImageView;
    private FirebaseManager firebaseManager;
    private Button saveButton;
    private ProgressDialog progressDialog;
    private ImageView buttonBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_policy, container, false);
        firebaseManager = new FirebaseManager();

        String adminID = Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid();

        firebaseManager.fetchDriverPolicy(adminID, new FirebaseManager.OnFetchListener<DriverPolicy>() {
            @Override
            public void onFetchSuccess(DriverPolicy object) {
                driverPolicy = object;
                if (driverPolicy == null) {
                    driverPolicy = new DriverPolicy();
                }
                if(driverPolicy.getRespect() == null){
                    driverPolicy.setRespect(new ArrayList<>());
                }
                if(driverPolicy.getDocuments() == null){
                    driverPolicy.setDocuments(new ArrayList<>());
                }
                if(driverPolicy.getLaw() == null){
                    driverPolicy.setLaw(new ArrayList<>());
                }
                if(driverPolicy.getVehicle() == null){
                    driverPolicy.setVehicle(new ArrayList<>());
                }
                if(driverPolicy.getPractice() == null){
                    driverPolicy.setPractice(new ArrayList<>());
                }

                initializeAdapters(root);
                updateUI(driverPolicy);
            }

            @Override
            public void onFetchFailure(String message) {
                AndroidUtil.showToast(requireContext(),message);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }
        });

        saveButton = root.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.showLoadingDialog(progressDialog);
                driverPolicy.setDocuments(documentPolicyAdapter.getPolicyList());
                driverPolicy.setRespect(respectPolicyAdapter.getPolicyList());
                driverPolicy.setRespect(lawPolicyAdapter.getPolicyList());
                driverPolicy.setRespect(vehiclePolicyAdapter.getPolicyList());
                driverPolicy.setRespect(practicePolicyAdapter.getPolicyList());

                String id = firebaseManager.mAuth.getCurrentUser().getUid();
                firebaseManager.updateDriverPolicy(id, driverPolicy, new FirebaseManager.OnTaskCompleteListener() {
                    @Override
                    public void onTaskSuccess(String message) {
                        AndroidUtil.showToast(requireContext(), message);
                        AndroidUtil.hideLoadingDialog(progressDialog);
                    }

                    @Override
                    public void onTaskFailure(String message) {
                        AndroidUtil.showToast(requireContext(), message);
                        AndroidUtil.hideLoadingDialog(progressDialog);
                    }
                });
            }
        });
        buttonBack = root.findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new PolicyListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    private void initializeAdapters(View root) {
        RecyclerView respectRecyclerView = root.findViewById(R.id.respectRecyclerView);
        respectRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        respectPolicyAdapter = new PolicyAdapter(driverPolicy.getRespect());
        respectRecyclerView.setAdapter(respectPolicyAdapter);

        RecyclerView documentRecyclerView = root.findViewById(R.id.documentRecyclerView);
        documentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        documentPolicyAdapter = new PolicyAdapter(driverPolicy.getDocuments());
        documentRecyclerView.setAdapter(documentPolicyAdapter);

        RecyclerView lawRecyclerView = root.findViewById(R.id.lawRecyclerView);
        lawRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        lawPolicyAdapter = new PolicyAdapter(driverPolicy.getLaw());
        lawRecyclerView.setAdapter(lawPolicyAdapter);

        RecyclerView vehicleRecyclerView = root.findViewById(R.id.vehicleRecyclerView);
        vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        vehiclePolicyAdapter = new PolicyAdapter(driverPolicy.getVehicle());
        vehicleRecyclerView.setAdapter(vehiclePolicyAdapter);

        RecyclerView practiceRecyclerView = root.findViewById(R.id.practiceRecyclerView);
        practiceRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        practicePolicyAdapter = new PolicyAdapter(driverPolicy.getPractice());
        practiceRecyclerView.setAdapter(practicePolicyAdapter);

        addDocumentImageView = root.findViewById(R.id.add_document);
        addRespectImageVIew = root.findViewById(R.id.add_respect);
        addLawImageView = root.findViewById(R.id.add_law);
        addVehicleImageView = root.findViewById(R.id.add_vehicle);
        addPracticeImageView = root.findViewById(R.id.add_practice);

        addDocumentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentPolicyAdapter.addPolicyList(driverPolicy.getDocuments());
            }
        });
        addRespectImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respectPolicyAdapter.addPolicyList(driverPolicy.getRespect());
            }
        });
        addLawImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lawPolicyAdapter.addPolicyList(driverPolicy.getLaw());
            }
        });
        addVehicleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehiclePolicyAdapter.addPolicyList(driverPolicy.getVehicle());
            }
        });
        addPracticeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                practicePolicyAdapter.addPolicyList(driverPolicy.getPractice());
            }
        });
    }

    private void updateUI(DriverPolicy driverPolicy){
        respectPolicyAdapter.setPolicyList(driverPolicy.getRespect());
        documentPolicyAdapter.setPolicyList(driverPolicy.getDocuments());
        lawPolicyAdapter.setPolicyList(driverPolicy.getLaw());
        vehiclePolicyAdapter.setPolicyList(driverPolicy.getVehicle());
        practicePolicyAdapter.setPolicyList(driverPolicy.getPractice());

        AndroidUtil.hideLoadingDialog(progressDialog);
    }
}