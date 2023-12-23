package com.example.adminaber.Fragments.User.Chat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminaber.Adapters.UserChatAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserChatListFragment extends Fragment implements UserChatAdapter.RecyclerViewClickListener {
    private Map<User, String> userMap;
    private UserChatAdapter userAdapter;
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private List<User> userList, filteredList;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showLoadingDialog();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_chat_list, container, false);
        firebaseManager = new FirebaseManager();

        firebaseManager.getAllUsers(new FirebaseManager.OnFetchUserListListener<User, String>() {
            @Override
            public void onFetchUserListSuccess(Map<User, String> usersData) {
                userMap = usersData;
                userList = new ArrayList<>(userMap.keySet());
                updateUI(userList);
                hideLoadingDialog();
            }

            @Override
            public void onFetchUserListFailure(String message) {
                showToast(message);
                hideLoadingDialog();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_chat_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userAdapter = new UserChatAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(userAdapter);

        searchView = root.findViewById(R.id.search_view);
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
                queryUsers(newText);
                return true;
            }
        });

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateUI(List<User> userList){
        userAdapter.setUserList(userList);
        userAdapter.notifyDataSetChanged();
        hideLoadingDialog();
    }

    private void handleSearch() {
        String query = searchView.getQuery().toString();
        queryUsers(query);
    }
    private void queryUsers(String query) {
        filteredList = new ArrayList<>();
        if (userList != null) {
            for (User user : userList) {
                if (user.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                } else if (user.getEmail().toLowerCase().contains(query.toLowerCase())){
                    filteredList.add(user);
                }
            }
        }

        updateUI(filteredList);
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

    @Override
    public void onCardClick(int position) {
        String id;
        if (filteredList == null || filteredList.isEmpty() || position >= filteredList.size()){
            id = userMap.get(userList.get(position));
        } else {
            id = userMap.get(filteredList.get(position));
        }
        if(id != null){
            UserChatDetailFragment fragment = new UserChatDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userID", id);
            fragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_chat_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}