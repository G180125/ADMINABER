package com.example.adminaber.Fragments.User.Chat;

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
import android.widget.Toast;

import com.example.adminaber.Adapters.MessageAdapter;
import com.example.adminaber.Adapters.UserChatAdapter;
import com.example.adminaber.FirebaseManager;
import com.example.adminaber.Models.Message.MyMessage;
import com.example.adminaber.Models.User.User;
import com.example.adminaber.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserChatDetailFragment extends Fragment {
    private String userID;
    private User currentUser;
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
        showLoadingDialog();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_chat_detail, container, false);
        firebaseManager = new FirebaseManager();

        Bundle bundle = getArguments();
        if (bundle != null) {
            userID = bundle.getString("userID");
        }
        recyclerView = root.findViewById(R.id.recycler_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize messageAdapter with an empty list and a placeholder avatar
        messageAdapter = new MessageAdapter(new ArrayList<>(), null);
        recyclerView.setAdapter(messageAdapter);

        firebaseManager.getUserByID(userID, new FirebaseManager.OnFetchUserListener() {
            @Override
            public void onFetchUserSuccess(User user) {
                currentUser = user;
                updateUI(currentUser);
            }

            @Override
            public void onFetchUserFailure(String message) {
                hideLoadingDialog();
                showToast(message);
            }
        });

        backImageView = root.findViewById(R.id.back);
        avatar = root.findViewById(R.id.avatar);
        nameTextView = root.findViewById(R.id.name);
        sendText = root.findViewById(R.id.send_text);
        sendButton = root.findViewById(R.id.send_button);

        firebaseManager.readMessage(
                Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid(), userID, new FirebaseManager.OnReadingMessageListener() {
                    @Override
                    public void OnMessageDataChanged(List<MyMessage> messageList) {
                        updateMessageList(messageList);
                    }
                }
        );

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_main_chat_container, new UserChatListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendText.getText().toString();
                if (!message.isEmpty()) {
                    String sender = Objects.requireNonNull(firebaseManager.mAuth.getCurrentUser()).getUid();
                    firebaseManager.sendMessage(sender, userID, message);
                } else {
                    showToast("You haven't typed anything");
                }
                sendText.setText("");
            }

        });

        return root;
    }

    private void updateUI(User user){
        if(!user.getAvatar().isEmpty()){
            firebaseManager.retrieveImage(user.getAvatar(), new FirebaseManager.OnRetrieveImageListener() {
                @Override
                public void onRetrieveImageSuccess(Bitmap bitmap) {
                    avatar.setImageBitmap(bitmap);
                    messageAdapter.setAvatar(bitmap);
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
    }

    private void updateMessageList(List<MyMessage> newMessageList) {
        messageAdapter.updateMessages(newMessageList);

        recyclerView.scrollToPosition(newMessageList.size() - 1);
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