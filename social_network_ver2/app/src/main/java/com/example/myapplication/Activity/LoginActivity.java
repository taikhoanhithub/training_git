package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.example.socialnetwork.R;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.myapplication.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvDirectToRegister;
    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        tvDirectToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString(), password = etPassword.getText().toString();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        });
    }

    void initView() {
        tvDirectToRegister = findViewById(R.id.directToRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!= null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}