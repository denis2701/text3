package com.example.text3;

import android.app.AppOpsManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 123;
    private static final int USAGE_ACCESS_REQUEST_CODE = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomescreen);

        Button firstButton = findViewById(R.id.firstbutton);
        Button secondButton = findViewById(R.id.secondbutton);
        Button thirdButton = findViewById(R.id.thirdbutton);
        Button fourthButton = findViewById(R.id.fourthbutton);
        Button fifthButton = findViewById(R.id.fifthbutton);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacyPolicyActivity();
            }
        });

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBatteryOptimizationSettings();
            }
        });

        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOverlayPermission();
            }
        });

        fourthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsageAccessPermission();
            }
        });

        fifthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    private void openPrivacyPolicyActivity() {
        Intent intent = new Intent(this, PrivacyPolicy.class);
        startActivity(intent);
    }

    private void openBatteryOptimizationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // If not, request overlay permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            showToast("Programėlė jau turi leidimą įsijungti virš kitų programų.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                showToast("Leidimas įjungti virš kitų programų sėkmingai suteiktas.");
            } else {
                showToast("Leidimas įjungti virš kitų programų nebuvo suteiktas.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USAGE_ACCESS_REQUEST_CODE) {
            if (hasUsageAccessPermission()) {
                showToast("Leidimas gauti naudojimo duomenis sėkmingai suteiktas.");
            } else {
                showToast("Leidimas gauti naudojimo duomenis nebuvo suteiktas.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkUsageAccessPermission() {
        if (!hasUsageAccessPermission()) {
            // If not, request usage access permission
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, USAGE_ACCESS_REQUEST_CODE);
        } else {
            showToast("Programėlė jau turi leidimą gauti naudojimo duomenis.");
        }
    }

    private boolean hasUsageAccessPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}