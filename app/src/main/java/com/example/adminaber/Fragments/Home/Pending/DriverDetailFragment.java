package com.example.adminaber.Fragments.Home.Pending;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Fragments.Driver.Manager.DriverManagerListFragment;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.Models.User.Gender;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverDetailFragment extends Fragment {
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private String driverID, previous;
    private Driver driver;
    private CircleImageView avatar;
    private ImageView backImageView, activeImageView;
    private TextView statusTextView, nameTextView, emailTextView, phoneTextView, licenseNumberTextView, totalDriveTextView;
    private RadioButton maleRadioButton, femaleRadiusButton;
    private Button confirmButton, denyButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_driver_detail, container, false);
        firebaseManager = new FirebaseManager();

        Bundle bundle = getArguments();
        if (bundle != null) {
            previous = bundle.getString("previous","");
            driverID = bundle.getString("driverID","");
            firebaseManager.getDriverByID(driverID, new FirebaseManager.OnFetchListener<Driver>() {
                @Override
                public void onFetchSuccess(Driver object) {
                    driver = object;
                    updateUI(object);
                }

                @Override
                public void onFetchFailure(String message) {
                    AndroidUtil.hideLoadingDialog(progressDialog);
                    AndroidUtil.showToast(requireContext(), message);
                }
            });
        }

        backImageView = root.findViewById(R.id.back);
        avatar = root.findViewById(R.id.avatar);
        statusTextView = root.findViewById(R.id.status);
        nameTextView = root.findViewById(R.id.name);
        emailTextView = root.findViewById(R.id.email);
        maleRadioButton = root.findViewById(R.id.radioButtonMale);
        femaleRadiusButton = root.findViewById(R.id.radioButtonFemale);
        phoneTextView = root.findViewById(R.id.phone_number);
        licenseNumberTextView = root.findViewById(R.id.license_number);
        totalDriveTextView = root.findViewById(R.id.total_drive);
        confirmButton = root.findViewById(R.id.confirm_button);
        denyButton = root.findViewById(R.id.deny_button);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previous.equals("pending_list")) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_pending_container, new PendingListFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (previous.equals("manager_list")){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_driver_manager_container, new DriverManagerListFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.showLoadingDialog(progressDialog);
                driver.setPermission(true);
                driver.setStatus("OK");
                firebaseManager.updateDriver(driver, new FirebaseManager.OnTaskCompleteListener() {
                    @Override
                    public void onTaskSuccess(String message) {
                        AndroidUtil.hideLoadingDialog(progressDialog);
                        AndroidUtil.showToast(requireContext(), "Provide Permission Successfully");
                    }

                    @Override
                    public void onTaskFailure(String message) {
                        AndroidUtil.hideLoadingDialog(progressDialog);
                        AndroidUtil.showToast(requireContext(), message);
                    }
                });
            }
        });

        return root;
    }

    private void updateUI(Driver driver){
        firebaseManager.retrieveImage(driver.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
            @Override
            public void onRetrieveImageSuccess(Bitmap bitmap) {
                avatar.setImageBitmap(bitmap);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }

            @Override
            public void onRetrieveImageFailure(String message) {
                AndroidUtil.hideLoadingDialog(progressDialog);
                AndroidUtil.showToast(requireContext(), message);
            }
        });

        setStatus(driver);
        nameTextView.setText(driver.getName());
        emailTextView.setText(driver.getEmail());
        setGenderFromRadiusButton(driver);
        phoneTextView.setText(generatePhoneNumberForView(driver.getPhone()));
        licenseNumberTextView.setText(driver.getLicenseNumber());
        totalDriveTextView.setText(String.valueOf(driver.getTotalDrive()));
    }

    private void setStatus(Driver driver){
        statusTextView.setText(driver.getStatus());

        if (driver.getStatus().equalsIgnoreCase("Ok")) {
            statusTextView.setTextColor(Color.GREEN);
        } else if (driver.getStatus().equalsIgnoreCase("Register Pending")) {
            statusTextView.setTextColor(Color.parseColor("#F45E0B"));
        } else {
            statusTextView.setTextColor(Color.RED);
        }
    }

    private String generatePhoneNumberForView(String phone) {
        String cleanPhoneNumber = phone.replaceAll("\\D", "");

        // Extract the last 9 digits
        int startIndex = Math.max(0, cleanPhoneNumber.length() - 9);
        String last9Digits = cleanPhoneNumber.substring(startIndex);

        return last9Digits;
    }

    private void setGenderFromRadiusButton(Driver driver){
        if (driver.getGender() == Gender.MALE) {
            maleRadioButton.setChecked(true);
        } else if (driver.getGender() == Gender.FEMALE) {
            femaleRadiusButton.setChecked(true);
        }
    }
}