package com.example.adminaber;

import static com.example.adminaber.Utils.AndroidUtil.hideLoadingDialog;
import static com.example.adminaber.Utils.AndroidUtil.showLoadingDialog;
import static com.example.adminaber.Utils.AndroidUtil.showToast;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.adminaber.Models.Booking.Booking;
import com.example.adminaber.Models.Booking.BookingResponse;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.Models.User.Gender;
import com.example.adminaber.Models.User.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class BookingDetailFragment extends Fragment {
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private Driver driver;
    private Booking booking;
    private User user;
    private String bookingID, userId, imagePath;
    private TextView pickUpTextView, destinationTextView, bookingDateTextView, bookingTimeTextView, statusTextView, userNameTextView, userGenderTextView, userPhoneNumberTextView, brandTextView, vehicleNameTextView, colorTextView, seatTextView, plateTextView, amountTextView, methodTextView, driverNameTextView, driverGenderTextView, driverphoneNumberTextView, licenseNumberTextView, realPickUpTimeTextView;
    private CircleImageView userAvatar, driverAvatar;
    private ImageView backButton, pickupImageView, userExpand,vehicleExpand, paymentExpand, driverExpand, resourceExpand;
    private CardView userCardView, vehicleCardView, paymentCardView, driverCardView, resourceCardView;
    private boolean[] imageViewClickStates = {false, false, false, false, false};
    private Button pickUpButton;
    private Bitmap cropped;
    private PopupWindow popupWindow;
    private View root;
    private TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_booking_detail, container, false);
        firebaseManager = new FirebaseManager();

        Bundle args = getArguments();
        if (args != null) {
            bookingID = args.getString("bookingID", "");
            firebaseManager.fetchBookingById(bookingID, new FirebaseManager.OnFetchListener<BookingResponse>() {
                @Override
                public void onFetchSuccess(BookingResponse object) {
                    booking = object.getBooking();
                    updateUI(booking);
                }

                @Override
                public void onFetchFailure(String message) {

                }
            });
        }

        backButton = root.findViewById(R.id.back);
        pickUpTextView = root.findViewById(R.id.pick_up);
        destinationTextView = root.findViewById(R.id.destination);
        bookingDateTextView = root.findViewById(R.id.booking_date);
        bookingTimeTextView = root.findViewById(R.id.booking_time);
        statusTextView = root.findViewById(R.id.status);
        userAvatar = root.findViewById(R.id.user_avatar);
        userNameTextView = root.findViewById(R.id.user_name);
        userGenderTextView = root.findViewById(R.id.user_gender);
        userPhoneNumberTextView = root.findViewById(R.id.user_phone_number);
        brandTextView = root.findViewById(R.id.brand);
        vehicleNameTextView = root.findViewById(R.id.name);
        colorTextView = root.findViewById(R.id.color);
        seatTextView = root.findViewById(R.id.seat);
        plateTextView = root.findViewById(R.id.plate);
        amountTextView = root.findViewById(R.id.amount);
        methodTextView = root.findViewById(R.id.method);
        driverAvatar = root.findViewById(R.id.driver_avatar);
        driverNameTextView = root.findViewById(R.id.driver_name);
        driverGenderTextView = root.findViewById(R.id.gender);
        driverphoneNumberTextView = root.findViewById(R.id.phone_number);
        licenseNumberTextView = root.findViewById(R.id.license_number);
        realPickUpTimeTextView = root.findViewById(R.id.real_pick_up_time);
        pickupImageView = root.findViewById(R.id.image);
        userExpand = root.findViewById(R.id.user_expand);
        vehicleExpand = root.findViewById(R.id.vehicle_expand);
        paymentExpand = root.findViewById(R.id.payment_expand);
        driverExpand = root.findViewById(R.id.driver_expand);
        resourceExpand = root.findViewById(R.id.resource_expand);
        userCardView = root.findViewById(R.id.user_card_view);
        vehicleCardView = root.findViewById(R.id.vehicle_card_view);
        paymentCardView = root.findViewById(R.id.payment_card_view);
        driverCardView = root.findViewById(R.id.driver_card_view);
        resourceCardView = root.findViewById(R.id.resource_card_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new BookingListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        userExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClickStates[0] = !imageViewClickStates[0];

                if (imageViewClickStates[0]) {
                    userExpand.setImageResource(R.drawable.ic_arrow_up);
                    userCardView.setVisibility(View.VISIBLE);
                } else {
                    userExpand.setImageResource(R.drawable.ic_arrow_down);
                    userCardView.setVisibility(View.GONE);
                }
            }
        });

        vehicleExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClickStates[1] = !imageViewClickStates[1];

                if (imageViewClickStates[1]) {
                    vehicleExpand.setImageResource(R.drawable.ic_arrow_up);
                    vehicleCardView.setVisibility(View.VISIBLE);
                } else {
                    vehicleExpand.setImageResource(R.drawable.ic_arrow_down);
                    vehicleCardView.setVisibility(View.GONE);
                }
            }
        });

        paymentExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClickStates[2] = !imageViewClickStates[2];

                if (imageViewClickStates[2]) {
                    paymentExpand.setImageResource(R.drawable.ic_arrow_up);
                    paymentCardView.setVisibility(View.VISIBLE);
                } else {
                    paymentExpand.setImageResource(R.drawable.ic_arrow_down);
                    paymentCardView.setVisibility(View.GONE);
                }
            }
        });

        driverExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClickStates[3] = !imageViewClickStates[3];

                if (imageViewClickStates[3]) {
                    driverExpand.setImageResource(R.drawable.ic_arrow_up);
                    driverCardView.setVisibility(View.VISIBLE);
                } else {
                    driverExpand.setImageResource(R.drawable.ic_arrow_down);
                    driverCardView.setVisibility(View.GONE);
                }
            }
        });

        resourceExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewClickStates[4] = !imageViewClickStates[4];

                if (imageViewClickStates[4]) {
                    resourceExpand.setImageResource(R.drawable.ic_arrow_up);
                    resourceCardView.setVisibility(View.VISIBLE);
                } else {
                    resourceExpand.setImageResource(R.drawable.ic_arrow_down);
                    resourceCardView.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    private void updateUI(Booking booking){
        pickUpTextView.setText(booking.getPickUp().getAddress());
        destinationTextView.setText(booking.getDestination().getAddress());
        bookingDateTextView.setText(booking.getBookingDate());
        bookingTimeTextView.setText(booking.getBookingTime());
        statusTextView.setText(booking.getStatus());
        brandTextView.setText(booking.getVehicle().getBrand());
        vehicleNameTextView.setText(booking.getVehicle().getName());
        colorTextView.setText(booking.getVehicle().getColor());
        seatTextView.setText(booking.getVehicle().getSeatCapacity());
        plateTextView.setText(booking.getVehicle().getNumberPlate());
        double paymentAmount = booking.getPayment().getAmount();
        String roundedPayment = String.format("%.0f", paymentAmount); // Rounds to 0 decimal places
        String amount = roundedPayment + " " + booking.getPayment().getCurrency();
        amountTextView.setText(amount);
        methodTextView.setText("Card **** **** ****" + booking.getPayment().getCard().getLast4());
        if(booking.getPickUp().getRealPickUpTime() != null && !booking.getPickUp().getRealPickUpTime().isEmpty()){
            realPickUpTimeTextView.setText(booking.getPickUp().getRealPickUpTime());
        }

        if(booking.getUser() != null){
            firebaseManager.getUserByID(booking.getUser(), new FirebaseManager.OnFetchListener<User>() {
                @Override
                public void onFetchSuccess(User object) {
                    userNameTextView.setText(object.getName());
                    userGenderTextView.setText(getGenderText(object.getGender()));
                    userPhoneNumberTextView.setText(object.getPhoneNumber());
                    if(object.getAvatar() != null && !object.getAvatar().isEmpty()){
                        firebaseManager.retrieveImage(object.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
                            @Override
                            public void onRetrieveImageSuccess(Bitmap bitmap) {
                                userAvatar.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onRetrieveImageFailure(String message) {

                            }
                        });
                    }
                }

                @Override
                public void onFetchFailure(String message) {
                    showToast(requireContext(), message);
                    hideLoadingDialog(progressDialog);
                }
            });
        }

        if(booking.getDriver() != null &&  !booking.getDriver().isEmpty()){
            firebaseManager.getDriverByID(booking.getDriver(), new FirebaseManager.OnFetchListener<Driver>() {
                @Override
                public void onFetchSuccess(Driver object) {
                    driverNameTextView.setText(object.getName());
                    driverGenderTextView.setText(getGenderText(object.getGender()));
                    licenseNumberTextView.setText(object.getLicenseNumber());
                    driverphoneNumberTextView.setText(object.getPhone());

                    firebaseManager.retrieveImage(object.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
                        @Override
                        public void onRetrieveImageSuccess(Bitmap bitmap) {
                            driverAvatar.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onRetrieveImageFailure(String message) {

                        }
                    });
                }

                @Override
                public void onFetchFailure(String message) {

                }
            });
        }

        if(booking.getPickUp().getPickUpImage() != null && !booking.getPickUp().getPickUpImage().isEmpty()){
            firebaseManager.retrieveImage(booking.getPickUp().getPickUpImage(), new FirebaseManager.OnRetrieveImageListener() {
                @Override
                public void onRetrieveImageSuccess(Bitmap bitmap) {
                    pickupImageView.setImageBitmap(bitmap);
                    hideLoadingDialog(progressDialog);
                }

                @Override
                public void onRetrieveImageFailure(String message) {

                }
            });
        } else {
            hideLoadingDialog(progressDialog);
        }
    }

    public static String getGenderText(Gender gender) {
        switch (gender) {
            case MALE:
                return "Male";
            case FEMALE:
                return "Female";
            default:
                return "Unknown";
        }
    }
}