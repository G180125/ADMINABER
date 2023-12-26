package com.example.adminaber.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminaber.FirebaseManager;
import com.example.adminaber.R;
import com.example.adminaber.Utils.AndroidUtil;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText emailEditText, passwordEditText;
    private ProgressDialog progressDialog;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseManager = new FirebaseManager();
        progressDialog = new ProgressDialog(LoginActivity.this);

        loginButton = findViewById(R.id.login_button);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        Intent intent = getIntent();
        if (intent.hasExtra("email") && intent.hasExtra("password")) {
            String email = intent.getStringExtra("email");
            String password = intent.getStringExtra("password");

            emailEditText.setText(email);
            passwordEditText.setText(password);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtil.showLoadingDialog(progressDialog);
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()){
                    firebaseManager.login(email, password, new FirebaseManager.OnTaskCompleteListener() {
                        @Override
                        public void onTaskSuccess(String message) {
                            AndroidUtil.hideLoadingDialog(progressDialog);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onTaskFailure(String message) {
                            AndroidUtil.hideLoadingDialog(progressDialog);
                            AndroidUtil.showToast(LoginActivity.this, message);
                        }
                    });
                } else {
                    AndroidUtil.hideLoadingDialog(progressDialog);
                    AndroidUtil.showToast(LoginActivity.this, "Email or Password is empty");
                }
            }
        });
    }
}