package com.example.adminaber.Adapters.Home;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.Models.Staff.Policy;
import com.example.adminaber.R;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder>{
    private List<Policy> policyList;

    public PolicyAdapter(List<Policy> policyList) {
        this.policyList = policyList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPolicyList(List<Policy> policyList) {
        this.policyList = policyList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addPolicyList(List<Policy> policyList) {
        Policy policy = new Policy();
        policyList.add(policy);
        notifyDataSetChanged();
    }

    public List<Policy> getPolicyList() {
        return policyList;
    }

    @NonNull
    @Override
    public PolicyAdapter.PolicyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.policy, parent, false);
        return new PolicyAdapter.PolicyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PolicyAdapter.PolicyViewHolder holder, int position) {
        Policy policy = policyList.get(position);
        holder.bind(policy, position);
    }

    @Override
    public int getItemCount() {
        return policyList.size();
    }

    public class PolicyViewHolder extends RecyclerView.ViewHolder{
        EditText policyEditText;
        ImageView deleteImageView;
        public PolicyViewHolder(@NonNull View itemView) {
            super(itemView);
            policyEditText = itemView.findViewById(R.id.policy);
            deleteImageView = itemView.findViewById(R.id.delete_button);

            policyEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Policy policy = policyList.get(position);
                        policy.setPolicy(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Do nothing
                }
            });


            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        policyList.remove(position); // Remove the policy from the list
                        notifyItemRemoved(position);  // Notify the adapter about the change
                        notifyItemRangeChanged(position, policyList.size()); // Update remaining items
                    }
                }
            });
        }

        public void bind(Policy policy, int position) {
            if (policy.getPolicy() != null && !policy.getPolicy().isEmpty()) {
                policyEditText.setText(policy.getPolicy());
            } else {
                policyEditText.setText("Enter a Policy"); // or set a default value
            }
        }

    }
}
