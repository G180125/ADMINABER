package com.example.adminaber.Adapters.Home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.R;

import java.util.List;

public class DriverPendingAdapter extends RecyclerView.Adapter<DriverPendingAdapter.DriverRegisterViewHolder>{
    private List<Driver> driverList;
    private DriverPendingAdapter.RecyclerViewClickListener mListener;
    private FirebaseManager firebaseManager;

    public DriverPendingAdapter(List<Driver> driverList, DriverPendingAdapter.RecyclerViewClickListener listener ){
        this.driverList = driverList;
        this.mListener = listener;
        firebaseManager = new FirebaseManager();
    }

    public void setUserList(List<Driver> driverList) {
        this.driverList = driverList;
    }


    @NonNull
    @Override
    public DriverPendingAdapter.DriverRegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_pending_container, parent, false);
        return new DriverPendingAdapter.DriverRegisterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverPendingAdapter.DriverRegisterViewHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.bind(driver, position);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class DriverRegisterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView, statusTextView, emailTextView, dateTextView;
        public DriverRegisterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.name);
            statusTextView = itemView.findViewById(R.id.status);
            emailTextView = itemView.findViewById(R.id.email);
            dateTextView = itemView.findViewById(R.id.date);

            // Set click listener on the itemView
            itemView.setOnClickListener(this);
        }

        public void bind(Driver driver, int position) {
            nameTextView.setText(driver.getName());
            statusTextView.setText(driver.getStatus());
            emailTextView.setText(driver.getEmail());
            dateTextView.setText(driver.getAvatarUploadDate());

            if (driver.getStatus().equalsIgnoreCase("Ok")) {
                statusTextView.setTextColor(Color.GREEN);
            } else if (driver.getStatus().equalsIgnoreCase("Register Pending")) {
                statusTextView.setTextColor(Color.parseColor("#F45E0B"));
            } else {
                statusTextView.setTextColor(Color.RED);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onCardClick(getAdapterPosition());
        }
    }
    public interface RecyclerViewClickListener {
        void onCardClick(int position);
    }
}
