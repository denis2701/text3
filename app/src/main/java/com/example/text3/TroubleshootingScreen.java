package com.example.text3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TroubleshootingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troubleshooting);

        Button button1 = findViewById(R.id.appUsageButton);
        Button button12 = findViewById(R.id.appUsageButton12);
        Button button2 = findViewById(R.id.appUsageButton2);
        Button button3 = findViewById(R.id.appUsageButton3);
        Button button5 = findViewById(R.id.appUsageButton5);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(TroubleshootingScreen.this, "Nerasta veiklos, kuri galėtų atlikti šį veiksmą", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHuaweiDevice()) {
                    Toast.makeText(TroubleshootingScreen.this, "Ši funkcija šiame telefone nepasiekiama", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(TroubleshootingScreen.this, "Nerasta veiklos, kuri galėtų atlikti šį veiksmą", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(TroubleshootingScreen.this, "Nerasta veiklos, kuri galėtų atlikti šį veiksmą", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(TroubleshootingScreen.this, "Nerasta veiklos, kuri galėtų atlikti šį veiksmą", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:denisgre2701@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Troubles with the program");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                    Toast.makeText(TroubleshootingScreen.this, "No email app found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isHuaweiDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("Huawei");
    }
}