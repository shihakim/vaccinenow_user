package com.example.data33;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private EditText editTextEmail_manager;
    private EditText editTextPassword_manager;
    private Button buttonLogIn_manager;
    private Button buttonSignUp_manager;
    private Button buttonchang_manager;
    public String Uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manager);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail_manager = (EditText) findViewById(R.id.edittext_email);
        editTextPassword_manager = (EditText) findViewById(R.id.edittext_password);


        buttonSignUp_manager = (Button) findViewById(R.id.btn_usersignup);
        buttonSignUp_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpActivity 연결
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        buttonLogIn_manager = (Button) findViewById(R.id.btn_login);
        buttonLogIn_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail_manager.getText().toString().equals("") && !editTextPassword_manager.getText().toString().equals("")) {
                    loginUser(editTextEmail_manager.getText().toString(), editTextPassword_manager.getText().toString());
                } else {
                    Toast.makeText(LogInActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    firebaseAuth.addAuthStateListener(firebaseAuthListener);

                } else {
                }
            }
        };
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(LogInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser users = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(LogInActivity.this, navigation.class);
                            intent.putExtra("uid",users.getUid());
                            startActivity(intent);
                        } else {
                            // 로그인 실패
                            Toast.makeText(LogInActivity.this, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
