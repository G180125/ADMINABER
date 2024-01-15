package com.example.adminaber.Adapters.User;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;

import java.util.List;

public class UserManagerAdapter extends RecyclerView.Adapter<UserManagerAdapter.UserViewHolder> {
    private List<User> userList;
    private RecyclerViewClickListener mListener;
    private FirebaseManager firebaseManager;

    public UserManagerAdapter(List<User> userList, RecyclerViewClickListener listener) {
        this.userList = userList;
        this.mListener = listener;
        firebaseManager = new FirebaseManager();
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getUser(int position){
        return this.userList.get(position);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_manager_container, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = userList.get(position);
        holder.bind(user, position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView, emailTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            emailTextView = itemView.findViewById(R.id.email);

            // Set click listener on the itemView
            itemView.setOnClickListener(this);
        }

        public void bind(User user, int position) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
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

