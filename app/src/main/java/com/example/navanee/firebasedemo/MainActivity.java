package com.example.navanee.firebasedemo;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView emailField, passwordField;
    Button loginBtn, signBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni.isConnected()) {
            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
            loadAllViews();
            FirebaseApp.initializeApp(this);
        } else {
            Toast.makeText(MainActivity.this, R.string.toast_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    public void loadAllViews() {
        emailField = (TextView) findViewById(R.id.emailField);
        passwordField = (TextView) findViewById(R.id.passwordField);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
        signBtn = (Button) findViewById(R.id.signUpBtn);
        signBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loginBtn) {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.toast_empty_values, Toast.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("test", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("test", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.toast_loginFailed, Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                            startActivity(intent);
                        }
                        }
                    });
            }
        } else if (v.getId() == R.id.signUpBtn) {
            Intent intent = new Intent(this,SignUpActivity.class);
            startActivity(intent);
        }
    }
}
