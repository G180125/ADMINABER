package com.example.adminaber;

import static com.example.adminaber.Utils.AndroidUtil.hideLoadingDialog;
import static com.example.adminaber.Utils.AndroidUtil.showLoadingDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.adminaber.Adapters.Home.BookingAdapter;
import com.example.adminaber.Fragments.Home.MainHomeFragment;
import com.example.adminaber.Models.Booking.Booking;
import com.example.adminaber.Models.Booking.BookingResponse;
import com.example.adminaber.Models.User.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingListFragment extends Fragment implements BookingAdapter.RecyclerViewClickListener{
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private List<Booking> bookingList, filteredList;
    private BookingAdapter adapter;
    private User user;
    private String userID, formattedDate;
    private boolean isSpinnerTouched = false;
    private Spinner bookingStatusSpinner;
    private ImageView buttonBack, calendarImageView, searchImageView, cancelImageView;
    private DatePicker datePicker;
    private CardView selectedDateCardView, searchCardView;
    private SearchView searchView;
    private TextView selectedDateTextVew;
    private boolean calendarState = false;
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

        calendarImageView = root.findViewById(R.id.calendar_button);
        datePicker = root.findViewById(R.id.date_picker);
        selectedDateCardView = root.findViewById(R.id.selected_date_card_view);
        selectedDateTextVew = root.findViewById(R.id.selected_date);
        searchImageView = root.findViewById(R.id.search_button);
        cancelImageView = root.findViewById(R.id.cancel_button);
        searchCardView = root.findViewById(R.id.search_card_view);
        searchView = root.findViewById(R.id.search_view);

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarState = !calendarState;

                if(calendarState){
                    datePicker.setVisibility(View.VISIBLE);
                    selectedDateCardView.setVisibility(View.VISIBLE);
                    searchCardView.setVisibility(View.GONE);

                    setupDatePickerListener();
                } else {
                    datePicker.setVisibility(View.GONE);
                    selectedDateCardView.setVisibility(View.GONE);
                    searchCardView.setVisibility(View.VISIBLE);
                }
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                firebaseManager.getBookingByDate(formattedDate, new FirebaseManager.OnFetchListListener<BookingResponse>() {
                    @Override
                    public void onDataChanged(List<BookingResponse> object) {
                        bookingList.clear();

                        for(BookingResponse bookingResponse : object){
                            if(bookingResponse.getBooking() != null){
                                bookingList.add(bookingResponse.getBooking());
                            }
                        }

                        updateUI(bookingList);
                    }
                });
            }
        });

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                selectedDateCardView.setVisibility(View.GONE);

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
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.bookingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BookingAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(adapter);

        buttonBack = root.findViewById(R.id.back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragmentTransaction.replace(R.id.fragment_main_container, new MainHomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
                handleSearch();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryBookings(newText);
                return true;
            }
        });


        return root;
    }

    private void handleSearch() {
        String query = searchView.getQuery().toString();
        if(query != null || !query.isEmpty()) {
            queryBookings(query);
        }
    }
    private void queryBookings(String query) {
        filteredList = new ArrayList<>();
        if (bookingList != null) {
            for (Booking booking : bookingList) {
                if (booking.getId().contains(query)) {
                    filteredList.add(booking);
                }
            }
        }

        updateUI(filteredList);
    }


    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<Booking> bookingList){
        adapter.setBookingList(bookingList);
        adapter.notifyDataSetChanged();
        hideLoadingDialog(progressDialog);
    }

    @Override
    public void onViewButtonClick(int position) {
        String id;
        if (filteredList == null || filteredList.isEmpty() || position >= filteredList.size()) {
            id = bookingList.get(position).getId();
        } else {
            id = filteredList.get(position).getId();
        }

        BookingDetailFragment fragment = new BookingDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bookingID", id);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentTransaction.replace(R.id.fragment_main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setupDatePickerListener() {
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
                formattedDate = sdf.format(selectedDate.getTime());

                selectedDateTextVew.setText(formattedDate);
            }
        });
    }
}