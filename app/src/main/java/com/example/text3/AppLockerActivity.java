package com.example.text3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AppLockerActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppLockerPrefs";
    private static final String PASSWORD_KEY = "password";
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_locker);

        passwordEditText = findViewById(R.id.passwordEditText);
        Button unlockButton = findViewById(R.id.unlockButton);

        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPassword(passwordEditText.getText().toString())) {
                    allowAccessToLockedApp();
                } else {
                    Toast.makeText(AppLockerActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPassword(String enteredPassword) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedPassword = preferences.getString(PASSWORD_KEY, "");

        return savedPassword.equals(enteredPassword);
    }

    private void allowAccessToLockedApp() {
        String packageName = getIntent().getStringExtra("packageName");

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        }

        finish();
    }
}