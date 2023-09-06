package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    EditText forgetEmail;
    Button forgetBtn;
    String email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mAuth = FirebaseAuth.getInstance();
        forgetEmail = findViewById(R.id.forgetEmail);
        forgetBtn = findViewById(R.id.forgot_btn);

        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private void validateData() {
        email = forgetEmail.getText().toString();
        if (email.isEmpty()) {
            forgetEmail.setError("Required");
        } else {
            forgetPass();
        }
    }

    private void forgetPass() {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPass.this, "Check your Email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPass.this, login.class));
                    finish();
                } else {
                    Toast.makeText(ForgotPass.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}