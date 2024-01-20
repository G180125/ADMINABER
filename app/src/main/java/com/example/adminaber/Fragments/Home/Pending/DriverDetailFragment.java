package com.example.adminaber.Fragments.Home.Pending;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private TextView statusTextView, nameTextView, emailTextView, phoneTextView, licenseNumberTextView, avatarDateTextView;
    private RadioButton maleRadioButton, femaleRadiusButton;
    private PopupWindow popupWindow;
    private Button confirmButton, banButton;
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_driver_detail, container, false);
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
        avatarDateTextView = root.findViewById(R.id.date);
        confirmButton = root.findViewById(R.id.confirm_button);
        banButton = root.findViewById(R.id.ban_button);

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

        banButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
                popupWindow.showAsDropDown(root, 0, 0);
            }
        });

        return root;
    }

    public void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_ban, null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        // Set the background color with alpha transparency
        popupView.setBackgroundColor(getResources().getColor(R.color.popup_background, null));

        EditText reasonEditText = popupView.findViewById(R.id.reason_edit_text);
        Button submitButton = popupView.findViewById(R.id.submitNewSOSBtn);
        ImageView cancelBtn = popupView.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.showLoadingDialog(progressDialog);
                String status = reasonEditText.getText().toString();

                driver.setPermission(false);
                driver.setStatus(status);

                firebaseManager.updateDriver(driver, new FirebaseManager.OnTaskCompleteListener() {
                    @Override
                    public void onTaskSuccess(String message) {
                        AndroidUtil.showToast(requireContext(),"Successfully ban the driver");
                        AndroidUtil.hideLoadingDialog(progressDialog);
                        updateUI(driver);
                        // Dismiss the PopupWindow after updating
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onTaskFailure(String message) {
                        AndroidUtil.showToast(requireContext(),message);
                        AndroidUtil.hideLoadingDialog(progressDialog);
                    }
                });
            }
        });

        popupWindow.showAsDropDown(root, 0, 0);

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
        avatarDateTextView.setText(driver.getAvatarUploadDate());
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