package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.socialnetwork.R;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText etName, etProfession, etEmail, etPassword;
    Button SignUpButton;
    TextView tvDirectToLogin;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        tvDirectToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString(), profession = etProfession.getText().toString();
                String email = etEmail.getText().toString(), password = etPassword.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User u = new User(name, profession, email, password);
                            String id = task.getResult().getUser().getUid();
                            database.child("Users").child(id).setValue(u);
                            Toast.makeText(getApplicationContext(), "dang ki thanh cong", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        }
                    }
                });
            }
        });
    }

    void initView() {
        etName = findViewById(R.id.edtName);
        etProfession = findViewById(R.id.edtProfession);
        etEmail = findViewById(R.id.edtEmailSignUp);
        etPassword = findViewById(R.id.edtPasswordSignUp);
        SignUpButton = findViewById(R.id.btnSignUp);
        tvDirectToLogin = findViewById(R.id.directToLogin);
    }
}