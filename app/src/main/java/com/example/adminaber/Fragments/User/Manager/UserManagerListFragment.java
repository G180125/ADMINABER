package com.example.adminaber.Fragments.User.Manager;

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

import com.example.adminaber.Adapters.User.UserManagerAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManagerListFragment extends Fragment implements UserManagerAdapter.RecyclerViewClickListener{
    private Map<User, String> userMap;
    private UserManagerAdapter userAdapter;
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private List<User> userList, filteredList;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_manager_list, container, false);
        firebaseManager = new FirebaseManager();

        firebaseManager.getAllUsers(new FirebaseManager.OnFetchUserListListener<User, String>() {
            @Override
            public void onFetchUserListSuccess(Map<User, String> usersData) {
                userMap = usersData;
                userList = new ArrayList<>(userMap.keySet());
                updateUI(userList);
            }

            @Override
            public void onFetchUserListFailure(String message) {
                AndroidUtil.showToast(requireContext(), message);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_manager_user);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        userAdapter = new UserManagerAdapter(new ArrayList<>(), this);
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
        AndroidUtil.hideLoadingDialog(progressDialog);
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

    @Override
    public void onCardClick(int position) {
        String id;
        if (filteredList == null || filteredList.isEmpty() || position >= filteredList.size()){
            id = userMap.get(userList.get(position));
        } else {
            id = userMap.get(filteredList.get(position));
        }
        if(id != null){
            UserDetailFragment fragment = new UserDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userID", id);
            fragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_user_manager_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}