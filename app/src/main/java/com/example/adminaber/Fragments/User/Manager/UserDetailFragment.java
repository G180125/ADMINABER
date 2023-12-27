package com.example.adminaber.Fragments.User.Manager;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.User.Gender;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailFragment extends Fragment {
    private String userID;
    private User currentUser;
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private TextView nameTextView, emailTextView, phoneTextView, addressTextView, plateTextView, sosTextView;
    private ImageView backImageView;
    private RadioButton maleRadioButton, femaleRadiusButton;
    private CircleImageView avatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showLoadingDialog();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_detail, container, false);
        firebaseManager = new FirebaseManager();

        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }

        firebaseManager.getUserByID(userID, new FirebaseManager.OnFetchListener<User>() {
            @Override
            public void onFetchSuccess(User object) {
                currentUser = object;
                updateUI(currentUser);
            }

            @Override
            public void onFetchFailure(String message) {
                hideLoadingDialog();
                showToast(message);
            }
        });

        backImageView = root.findViewById(R.id.back);
        avatar = root.findViewById(R.id.avatar);
        nameTextView = root.findViewById(R.id.name);
        emailTextView = root.findViewById(R.id.email);
        maleRadioButton = root.findViewById(R.id.radioButtonMale);
        femaleRadiusButton = root.findViewById(R.id.radioButtonFemale);
        phoneTextView = root.findViewById(R.id.phone);
        addressTextView = root.findViewById(R.id.address);
        plateTextView = root.findViewById(R.id.plate);
        sosTextView = root.findViewById(R.id.sos_name);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_user_manager_container, new UserManagerListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }

    private void updateAvatar(Bitmap bitmap){
        avatar.setImageBitmap(bitmap);
    }

    private void updateUI(User user){
        if(!user.getAvatar().isEmpty()){
            firebaseManager.retrieveImage(user.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
                @Override
                public void onRetrieveImageSuccess(Bitmap bitmap) {
                    updateAvatar(bitmap);
                    hideLoadingDialog();
                }

                @Override
                public void onRetrieveImageFailure(String message) {
                    showToast(message);
                    hideLoadingDialog();
                }
            });
        } else {
            hideLoadingDialog();
        }
        nameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        setGenderFromRadiusButton(user);
        phoneTextView.setText(user.getPhoneNumber());
        addressTextView.setText(user.getHomes().get(0).getAddress());
        plateTextView.setText(user.getVehicles().get(0).getNumberPlate());
        if(!user.getEmergencyContacts().isEmpty()){
            sosTextView.setText(user.getEmergencyContacts().get(0).getName());
        }
    }

    private void setGenderFromRadiusButton(User user){
        if (user.getGender() == Gender.MALE) {
            maleRadioButton.setChecked(true);
        } else if (user.getGender() == Gender.FEMALE) {
            femaleRadiusButton.setChecked(true);
        }
    }

    private void showLoadingDialog() {
        requireActivity().runOnUiThread(() -> {
            progressDialog = new ProgressDialog(requireContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        });
    }

    private void hideLoadingDialog() {
        requireActivity().runOnUiThread(() -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}