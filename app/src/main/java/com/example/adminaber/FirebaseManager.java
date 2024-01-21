package com.example.adminaber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.adminaber.Models.Booking.BookingResponse;
import com.example.adminaber.Models.Message.MyMessage;
import com.example.adminaber.Models.Staff.Admin;
import com.example.adminaber.Models.Staff.Driver;
import com.example.adminaber.Models.Staff.DriverPolicy;
import com.example.adminaber.Models.Staff.UserPolicy;
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
    public final String COLLECTION_DRIVERS = "drivers";
    public final String COLLECTION_ADMINS = "admins";
    public final String COLLECTION_CHATS = "Chats";
    public final String COLLECTION_BOOKINGS = "Bookings";
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

    public void fetchUserPolicy(String adminID, OnFetchListener<UserPolicy> listener){
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_ADMINS)
                    .document(adminID)  // Use document() instead of whereEqualTo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Admin admin = document.toObject(Admin.class);
                                assert admin != null;
                                listener.onFetchSuccess(admin.getUserPolicy());
                            } else {
                                listener.onFetchFailure("Admin Data not found");
                            }
                        } else {
                            listener.onFetchFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void updateUserPolicy(String adminID, UserPolicy userPolicy, OnTaskCompleteListener listener) {
        new Thread(() -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("userPolicy", userPolicy); // Assuming "userPolicy" is the field in Admin's document

            this.firestore.collection(this.COLLECTION_ADMINS)
                    .document(adminID)
                    .update(updateData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onTaskSuccess("Update User Policy Successfully");
                        } else {
                            listener.onTaskFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void updateDriverPolicy(String adminID, DriverPolicy driverPolicy, OnTaskCompleteListener listener) {
        new Thread(() -> {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("driverPolicy", driverPolicy); // Assuming "userPolicy" is the field in Admin's document

            this.firestore.collection(this.COLLECTION_ADMINS)
                    .document(adminID)
                    .update(updateData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onTaskSuccess("Update Driver Policy Successfully");
                        } else {
                            listener.onTaskFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void fetchDriverPolicy(String adminID, OnFetchListener<DriverPolicy> listener){
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_ADMINS)
                    .document(adminID)  // Use document() instead of whereEqualTo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Admin admin = document.toObject(Admin.class);
                                assert admin != null;
                                listener.onFetchSuccess(admin.getDriverPolicy());
                            } else {
                                listener.onFetchFailure("Admin Data not found");
                            }
                        } else {
                            listener.onFetchFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }


    public void getUserByID(String userID, OnFetchListener<User> listener) {
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_USERS)
                    .document(userID)  // Use document() instead of whereEqualTo
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = document.toObject(User.class);
                                listener.onFetchSuccess(user);
                            } else {
                                listener.onFetchFailure("User Data not found");
                            }
                        } else {
                            listener.onFetchFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
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

    public void getAllPendingDriver(OnFetchDriverListListener listener){
        List<Driver> list = new ArrayList<>();
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_DRIVERS)
                    .whereEqualTo("permission", false)
                    .whereEqualTo("status","Register Pending")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Driver driver = document.toObject(Driver.class);
                                list.add(driver);
                            }
                            listener.onFetchDriverListSuccess(list);
                        } else {
                            listener.onFetchDriverListFailure("Error fetching users: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void getAllDrivers(OnFetchDriverListListener listener){
        List<Driver> list = new ArrayList<>();
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_DRIVERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Driver driver = document.toObject(Driver.class);
                                list.add(driver);
                            }
                            listener.onFetchDriverListSuccess(list);
                        } else {
                            listener.onFetchDriverListFailure("Error fetching users: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void getDriverByID(String driverID, OnFetchListener<Driver> listener) {
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_DRIVERS)
                    .document(driverID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Driver driver = document.toObject(Driver.class);
                                listener.onFetchSuccess(driver);
                            } else {
                                listener.onFetchFailure("User Data not found");
                            }
                        } else {
                            listener.onFetchFailure("Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        }).start();
    }

    public void updateDriver(Driver driver, OnTaskCompleteListener listener){
        new Thread(() -> {
            this.firestore.collection(this.COLLECTION_DRIVERS)
                    .document(driver.getDocumentID())
                    .set(driver)
                    .addOnSuccessListener(aVoid -> {
                        listener.onTaskSuccess("Driver updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        listener.onTaskFailure("Error updating user: " + e.getMessage());
                    });
        }).start();
    }

    public void fetchBookings(OnFetchListListener<BookingResponse> listener){
        List<BookingResponse> bookingResponseList = new ArrayList<>();

        DatabaseReference reference =  this.database.getReference(COLLECTION_BOOKINGS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingResponseList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    BookingResponse bookingResponse = s.getValue(BookingResponse.class);
                    assert bookingResponse != null;
                    bookingResponse.setId(s.getKey());
                    bookingResponseList.add(bookingResponse);
                }
                listener.onDataChanged(bookingResponseList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fetchBookingById(String bookingId, OnFetchListener<BookingResponse> listener) {
        DatabaseReference reference = this.database.getReference(COLLECTION_BOOKINGS);

        reference.orderByChild("booking/id").equalTo(bookingId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DataSnapshot bookingSnapshot = snapshot.getChildren().iterator().next();
                            BookingResponse bookingResponse = bookingSnapshot.getValue(BookingResponse.class);

                            if (bookingResponse != null) {
                                bookingResponse.setId(bookingSnapshot.getKey());
                                listener.onFetchSuccess(bookingResponse);
                            } else {
                                listener.onFetchFailure("BookingResponse is null");
                            }
                        } else {
                            listener.onFetchFailure("Booking not found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onFetchFailure(error.getMessage());
                    }
                });
    }

    public void getBookingsByStatus(String status, OnFetchListListener<BookingResponse> listener){
        List<BookingResponse> bookingResponseList = new ArrayList<>();

        DatabaseReference reference =  this.database.getReference(COLLECTION_BOOKINGS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingResponseList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    BookingResponse bookingResponse = s.getValue(BookingResponse.class);
                    assert bookingResponse != null;
                    bookingResponse.setId(s.getKey());
                    bookingResponseList.add(bookingResponse);
                }
                listener.onDataChanged(bookingResponseList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getBookingByDate(String date, OnFetchListListener<BookingResponse> listener){
        List<BookingResponse> bookingResponseList = new ArrayList<>();

        DatabaseReference reference =  this.database.getReference(COLLECTION_BOOKINGS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingResponseList.clear();
                for (DataSnapshot s: snapshot.getChildren()){
                    BookingResponse bookingResponse = s.getValue(BookingResponse.class);
                    assert bookingResponse != null;
                    bookingResponse.setId(s.getKey());
                    if(bookingResponse.getBooking().getBookingDate().equals(date)){
                        bookingResponseList.add(bookingResponse);
                    }
                }
                listener.onDataChanged(bookingResponseList);
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

    public interface OnFetchListener<T> {
        void onFetchSuccess(T object);
        void onFetchFailure(String message);
    }

    public interface OnFetchUserListListener<K, V> {
        void onFetchUserListSuccess(Map<K, V> usersData);
        void onFetchUserListFailure(String errorMessage);
    }

    public interface OnFetchDriverListListener{
        void onFetchDriverListSuccess(List<Driver> list);
        void onFetchDriverListFailure(String message);
    }

    public interface OnRetrieveImageListener {
        void onRetrieveImageSuccess(Bitmap bitmap);
        void onRetrieveImageFailure(String message);
    }

    public interface OnReadingMessageListener{
        void OnMessageDataChanged(List<MyMessage> messageList);
    }

    public interface OnFetchListListener<T>{
        void onDataChanged(List<T> object);
    }
}


