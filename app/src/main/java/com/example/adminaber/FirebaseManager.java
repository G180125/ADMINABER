package com.example.adminaber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.adminaber.Models.Message.MyMessage;
import com.example.adminaber.Models.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseManager {
    public final String COLLECTION_USERS = "users";
    public final String COLLECTION_ADMINS = "admins";
    public final String COLLECTION_CHATS = "Chats";
    public FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    private FirebaseDatabase database;

    public FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
    }

    public void login(String email, String password, OnTaskCompleteListener listener){
        new Thread(() -> {
            this.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, get the user from Firestore
                            FirebaseUser firebaseUser = this.mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            isAdmin(firebaseUser.getUid(), new OnTaskCompleteListener() {
                                @Override
                                public void onTaskSuccess(String message) {
                                    listener.onTaskSuccess(message);
                                }

                                @Override
                                public void onTaskFailure(String message) {
                                    listener.onTaskFailure(message);
                                }
                            });
                        } else {
                            listener.onTaskFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    private void isAdmin(String userID, OnTaskCompleteListener listener) {
        this.firestore.collection(COLLECTION_ADMINS)
                .document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            listener.onTaskSuccess("Login Successfully");
                        } else {
                            listener.onTaskFailure("This Account is not an Admin");
                        }
                    } else {
                        listener.onTaskFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }


    public void getUserByID(String userID, OnFetchUserListener listener) {
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_USERS)
                    .document(userID)  // Use document() instead of whereEqualTo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                listener.onFetchUserSuccess(user);
                            } else {
                                listener.onFetchUserFailure("User Data not found");
                            }
                        } else {
                            listener.onFetchUserFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void getAllUsers(OnFetchUserListListener<User, String> listener) {
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Map<User, String> usersData = new HashMap<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                usersData.put(user, document.getId());
                            }
                            listener.onFetchUserListSuccess(usersData);
                        } else {
                            listener.onFetchUserListFailure("Error fetching users: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void retrieveImage(String path, OnRetrieveImageListener listener){
        final long ONE_MEGABYTE = 1024 * 1024;

        new Thread(() -> {
            this.storageRef.child(path)
                    .getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(bytes -> {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        listener.onRetrieveImageSuccess(bitmap);
                    })
                    .addOnFailureListener(exception -> {
                        listener.onRetrieveImageFailure("Error: " + exception.getMessage());
                    });
        }).start();
    }

    public void sendMessage(String sender, String receiver, String message){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        this.database.getReference().child(COLLECTION_CHATS)
                .push()
                .setValue(hashMap);
    }

    public void readMessage(final String myID, final String userID, OnReadingMessageListener listener){
        List<MyMessage> messageList = new ArrayList<>();

        DatabaseReference reference =  this.database.getReference(COLLECTION_CHATS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    MyMessage message = s.getValue(MyMessage.class);
                    if(message.getReceiver().equals(myID) && message.getSender().equals(userID) ||
                            message.getReceiver().equals(userID) && message.getSender().equals(myID)){
                        messageList.add(message);
                    }
                    listener.OnMessageDataChanged(messageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface OnTaskCompleteListener {
        void onTaskSuccess(String message);
        void onTaskFailure(String message);
    }

    public interface OnFetchUserListener {
        void onFetchUserSuccess(User user);
        void onFetchUserFailure(String message);
    }

    public interface OnFetchUserListListener<K, V> {
        void onFetchUserListSuccess(Map<K, V> usersData);
        void onFetchUserListFailure(String errorMessage);
    }

    public interface OnRetrieveImageListener {
        void onRetrieveImageSuccess(Bitmap bitmap);
        void onRetrieveImageFailure(String message);
    }

    public interface OnReadingMessageListener{
        void OnMessageDataChanged(List<MyMessage> messageList);
    }
}


