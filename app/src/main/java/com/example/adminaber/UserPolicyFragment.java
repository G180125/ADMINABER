package com.example.adminaber;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.adminaber.Adapters.Home.BookingAdapter;
import com.example.adminaber.Adapters.Home.PolicyAdapter;
import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Models.Staff.UserPolicy;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.Objects;

public class UserPolicyFragment extends Fragment {
    private UserPolicy userPolicy;
    private PolicyAdapter respectPolicyAdapter, experiencePolicyAdapter, accountPolicyAdapter, emergencyPolicyAdapter;
    private ImageView addRespectImageView, addExperienceImageVIew, addAccountImageView, addEmergencyImageView;
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
        View root =  inflater.inflate(R.layout.fragment_user_policy, container, false);
        firebaseManager = new FirebaseManager();

        String adminID = Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid();

        firebaseManager.fetchUserPolicy(adminID, new FirebaseManager.OnFetchListener<UserPolicy>() {
            @Override
            public void onFetchSuccess(UserPolicy object) {
                userPolicy = object;
                if (userPolicy == null) {
                    userPolicy = new UserPolicy();
                }
                if(userPolicy.getRespect() == null){
                    userPolicy.setRespect(new ArrayList<>());
                }
                if(userPolicy.getExperience() == null){
                    userPolicy.setExperience(new ArrayList<>());
                }
                if(userPolicy.getAccount() == null){
                    userPolicy.setAccount(new ArrayList<>());
                }
                if(userPolicy.getEmergency() == null){
                    userPolicy.setEmergency(new ArrayList<>());
                }

                initializeAdapters(root);
                updateUI(userPolicy);
            }

            @Override
            public void onFetchFailure(String message) {
                AndroidUtil.showToast(requireContext(),message);
                AndroidUtil.hideLoadingDialog(progressDialog);
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

        saveButton = root.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.showLoadingDialog(progressDialog);
                userPolicy.setRespect(respectPolicyAdapter.getPolicyList());
                userPolicy.setExperience(experiencePolicyAdapter.getPolicyList());
                userPolicy.setAccount(accountPolicyAdapter.getPolicyList());
                userPolicy.setEmergency(emergencyPolicyAdapter.getPolicyList());

                String id = firebaseManager.mAuth.getCurrentUser().getUid();
                firebaseManager.updateUserPolicy(id, userPolicy, new FirebaseManager.OnTaskCompleteListener() {
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

        return root;
    }

    private void initializeAdapters(View root) {
        RecyclerView respectRecyclerView = root.findViewById(R.id.respectRecyclerView);
        respectRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        respectPolicyAdapter = new PolicyAdapter(userPolicy.getRespect());
        respectRecyclerView.setAdapter(respectPolicyAdapter);

        RecyclerView experienceRecyclerView = root.findViewById(R.id.rideExperienceRecyclerView);
        experienceRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        experiencePolicyAdapter = new PolicyAdapter(userPolicy.getExperience());
        experienceRecyclerView.setAdapter(experiencePolicyAdapter);

        RecyclerView accountRecyclerView = root.findViewById(R.id.accountRecyclerView);
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        accountPolicyAdapter = new PolicyAdapter(userPolicy.getAccount());
        accountRecyclerView.setAdapter(accountPolicyAdapter);

        RecyclerView emergencyRecyclerView = root.findViewById(R.id.emergencyRecyclerView);
        emergencyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        emergencyPolicyAdapter = new PolicyAdapter(userPolicy.getEmergency());
        emergencyRecyclerView.setAdapter(emergencyPolicyAdapter);

        addRespectImageView = root.findViewById(R.id.add_respect);
        addExperienceImageVIew = root.findViewById(R.id.add_experience);
        addAccountImageView = root.findViewById(R.id.add_account);
        addEmergencyImageView = root.findViewById(R.id.add_emergency);

        addRespectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respectPolicyAdapter.addPolicyList(userPolicy.getRespect());
            }
        });

        addExperienceImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experiencePolicyAdapter.addPolicyList(userPolicy.getExperience());
            }
        });
        addAccountImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountPolicyAdapter.addPolicyList(userPolicy.getAccount());
            }
        });
        addEmergencyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyPolicyAdapter.addPolicyList(userPolicy.getEmergency());
            }
        });
    }

    private void updateUI(UserPolicy userPolicy){
        respectPolicyAdapter.setPolicyList(userPolicy.getRespect());
        experiencePolicyAdapter.setPolicyList(userPolicy.getExperience());
        accountPolicyAdapter.setPolicyList(userPolicy.getAccount());
        emergencyPolicyAdapter.setPolicyList(userPolicy.getEmergency());

        AndroidUtil.hideLoadingDialog(progressDialog);
    }
}