package com.example.adminaber.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminaber.Adapters.MessageAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Fragments.Driver.Chat.DriverChatListFragment;
import com.example.adminaber.Fragments.User.Chat.UserChatListFragment;
import com.example.adminaber.Models.Message.MyMessage;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailFragment extends Fragment {
    private String id, type;
    private User currentUser;
    private Driver currentDriver;
    private FirebaseManager firebaseManager;
    private ProgressDialog progressDialog;
    private TextView nameTextView;
    private ImageView backImageView;
    private CircleImageView avatar;
    private ImageButton sendButton;
    private EditText sendText;
    private MessageAdapter messageAdapter;
    private List<MyMessage> messageList;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(requireContext());
        AndroidUtil.showLoadingDialog(progressDialog);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat_detail, container, false);
        firebaseManager = new FirebaseManager();
        currentUser = new User();
        currentDriver = new Driver();

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            id = bundle.getString("userID");
        }
        recyclerView = root.findViewById(R.id.recycler_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize messageAdapter with an empty list and a placeholder avatar
        messageAdapter = new MessageAdapter(new ArrayList<>(), null);
        recyclerView.setAdapter(messageAdapter);

        if(type.equals("user")) {
            fetchUser(id);
        } else if (type.equals("driver")){
            fetchDriver(id);
        }

        backImageView = root.findViewById(R.id.back);
        avatar = root.findViewById(R.id.avatar);
        nameTextView = root.findViewById(R.id.name);
        sendText = root.findViewById(R.id.send_text);
        sendButton = root.findViewById(R.id.send_button);

        firebaseManager.readMessage(
                Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid(), id, new FirebaseManager.OnReadingMessageListener() {
                    @Override
                    public void OnMessageDataChanged(List<MyMessage> messageList) {
                        updateMessageList(messageList);
                    }
                }
        );

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("user")) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_user_chat_container, new UserChatListFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (type.equals("driver")){
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_driver_chat_container, new DriverChatListFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendText.getText().toString();
                if (!message.isEmpty()) {
                    String sender = Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid();
                    firebaseManager.sendMessage(sender, id, message);
                } else {
                    AndroidUtil.showToast(requireContext(),"You haven't typed anything");
                }
                sendText.setText("");
            }

        });

        return root;
    }

    private void fetchUser(String id){
        firebaseManager.getUserByID(id, new FirebaseManager.OnFetchListener<User>() {
            @Override
            public void onFetchSuccess(User user) {
                currentUser = user;
                updateUserUI(currentUser);
            }

            @Override
            public void onFetchFailure(String message) {
                AndroidUtil.hideLoadingDialog(progressDialog);
                AndroidUtil.showToast(requireContext(), message);
            }
        });
    }

    private void fetchDriver(String id){
        firebaseManager.getDriverByID(id, new FirebaseManager.OnFetchListener<Driver>() {
            @Override
            public void onFetchSuccess(Driver object) {
                currentDriver = object;
                updateDriverUI(currentDriver);
            }

            @Override
            public void onFetchFailure(String message) {
                AndroidUtil.hideLoadingDialog(progressDialog);
                AndroidUtil.showToast(requireContext(), message);
            }
        });
    }

    private void updateUserUI(User user){
        if(!user.getAvatar().isEmpty()){
            firebaseManager.retrieveImage(user.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
                @Override
                public void onRetrieveImageSuccess(Bitmap bitmap) {
                    avatar.setImageBitmap(bitmap);
                    messageAdapter.setAvatar(bitmap);
                    AndroidUtil.hideLoadingDialog(progressDialog);
                }

                @Override
                public void onRetrieveImageFailure(String message) {
                    AndroidUtil.showToast(requireContext(), message);
                    AndroidUtil.hideLoadingDialog(progressDialog);
                }
            });
        } else {
            AndroidUtil.hideLoadingDialog(progressDialog);
        }
        nameTextView.setText(user.getName());
    }

    private void updateDriverUI(Driver driver) {
        firebaseManager.retrieveImage(driver.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
            @Override
            public void onRetrieveImageSuccess(Bitmap bitmap) {
                avatar.setImageBitmap(bitmap);
                messageAdapter.setAvatar(bitmap);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }

            @Override
            public void onRetrieveImageFailure(String message) {
                AndroidUtil.showToast(requireContext(), message);
                AndroidUtil.hideLoadingDialog(progressDialog);
            }
        });

        nameTextView.setText(driver.getName());
    }

    private void updateMessageList(List<MyMessage> newMessageList) {
        messageAdapter.updateMessages(newMessageList);

        recyclerView.scrollToPosition(newMessageList.size() - 1);
    }
}