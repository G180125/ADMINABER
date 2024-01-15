package com.example.adminaber;

import static com.example.adminaber.Utils.AndroidUtil.hideLoadingDialog;
import static com.example.adminaber.Utils.AndroidUtil.showLoadingDialog;
import static com.example.adminaber.Utils.AndroidUtil.showToast;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.adminaber.Adapters.Home.BookingAdapter;
import com.example.adminaber.Models.Booking.Booking;
import com.example.adminaber.Models.Booking.BookingResponse;
import com.example.adminaber.Models.User.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingListFragment extends Fragment implements BookingAdapter.RecyclerViewClickListener{
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private List<Booking> bookingList;
    private BookingAdapter adapter;
    private User user;
    private String userID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_booking_list, container, false);
        firebaseManager = new FirebaseManager();
        bookingList = new ArrayList<>();
        firebaseManager.fetchBookings(new FirebaseManager.OnFetchListListener<BookingResponse>() {
            @Override
            public void onDataChanged(List<BookingResponse> object) {

                for(BookingResponse bookingResponse : object){
                    if(bookingResponse.getBooking() != null){
                        bookingList.add(bookingResponse.getBooking());
                    }
                }

                updateUI(bookingList);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.bookingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BookingAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<Booking> bookingList){
        adapter.setBookingList(bookingList);
        adapter.notifyDataSetChanged();
        hideLoadingDialog(progressDialog);
    }

    @Override
    public void onViewButtonClick(int position) {
        BookingDetailFragment fragment = new BookingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bookingID", bookingList.get(position).getId());
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction.replace(R.id.fragment_main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}