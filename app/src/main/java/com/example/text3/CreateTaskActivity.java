package com.example.text3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity {

    private FrameLayout goalTimePicker;
    private FrameLayout goalUnlockField;
    private Button blackButton;
    private Button yellowButton;
    private FrameLayout goalAppFrame;
    private Spinner goalAppItem;
    private Spinner spinner;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    NumberPicker unlockPicker;
    DatabaseHelper dbHelper = new DatabaseHelper(this);
    private int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategoals);

        goalAppItem = findViewById(R.id.GoalAppItem);

        List<String> installedApps = getInstalledApps(this);

        goalTimePicker = findViewById(R.id.GoalTimePicker);
        goalUnlockField = findViewById(R.id.GoalUnlockField);
        goalAppFrame = findViewById(R.id.GoalAppFrame);

        blackButton = findViewById(R.id.blackButton);
        yellowButton = findViewById(R.id.yellowButton);

        blackButton.setVisibility(View.INVISIBLE);
        yellowButton.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.GoalTypeItem);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.create_spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        hourPicker = findViewById(R.id.GoalHourTime);
        minutePicker = findViewById(R.id.GoalMinuteTime);

        unlockPicker = findViewById(R.id.GoalCountPicker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(8);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        unlockPicker.setMinValue(0);
        unlockPicker.setMaxValue(30);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goalTimePicker.setVisibility(View.GONE);
                goalUnlockField.setVisibility(View.GONE);
                goalAppFrame.setVisibility(View.GONE);

                String selectedItem = (String) parent.getItemAtPosition(position);
                String time = null;
                String appName = null;

                if ("Telefono naudojimo laikas".equals(selectedItem)) {
                    goalTimePicker.setVisibility(View.VISIBLE);
                    blackButton.setVisibility(View.VISIBLE);
                    yellowButton.setVisibility(View.VISIBLE);
                    updateButtonConstraints(R.id.GoalTimePicker);
                    time = String.format("%02d:%02d", hourPicker.getValue(), minutePicker.getValue());
                } else if ("Programėlės naudojimo laikas".equals(selectedItem)) {
                    goalTimePicker.setVisibility(View.VISIBLE);
                    goalAppFrame.setVisibility(View.VISIBLE);
                    blackButton.setVisibility(View.VISIBLE);
                    yellowButton.setVisibility(View.VISIBLE);
                    updateGoalAppFrameConstraints(goalTimePicker.getId());
                    updateButtonConstraints(R.id.GoalAppFrame);
                    time = String.format("%02d:%02d", hourPicker.getValue(), minutePicker.getValue());
                } else if ("Telefono atrakinimo skaičius".equals(selectedItem)) {
                    goalUnlockField.setVisibility(View.VISIBLE);
                    blackButton.setVisibility(View.VISIBLE);
                    yellowButton.setVisibility(View.VISIBLE);
                    updateButtonConstraints(goalUnlockField.getId());
                    time = String.format("%02d:%02d", unlockPicker.getValue(), 0);
                } else if ("Programėlės įjungimo skaičius".equals(selectedItem)) {
                    goalUnlockField.setVisibility(View.VISIBLE);
                    goalAppFrame.setVisibility(View.VISIBLE);
                    blackButton.setVisibility(View.VISIBLE);
                    yellowButton.setVisibility(View.VISIBLE);
                    updateGoalAppFrameConstraints(goalUnlockField.getId());
                    updateButtonConstraints(goalAppFrame.getId());
                    time = String.format("%02d:%02d", unlockPicker.getValue(), 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                goalTimePicker.setVisibility(View.GONE);
                goalUnlockField.setVisibility(View.GONE);
                goalAppFrame.setVisibility(View.GONE);
                blackButton.setVisibility(View.GONE);
                yellowButton.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, installedApps);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalAppItem.setAdapter(adapter2);

        goalAppItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Naujo tikslo sukūrimas buvo atšauktas");
                onBackPressed();
            }
        });

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
                if (flag == 0) {
                    replaceFragment(new Goalscreen());
                }
                else {
                    flag = 0;
                    onBackPressed();
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showToast(String message) {
        Toast.makeText(CreateTaskActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveTask() {
        String selectedItem = (String) spinner.getSelectedItem();
        String time = null;
        String appName = null;

        if ("Telefono naudojimo laikas".equals(selectedItem) || "Programėlės naudojimo laikas".equals(selectedItem)) {
            time = String.format("%02d:%02d", hourPicker.getValue(), minutePicker.getValue());
        } else if ("Telefono atrakinimo skaičius".equals(selectedItem) || "Programėlės įjungimo skaičius".equals(selectedItem)) {
            time = String.valueOf(unlockPicker.getValue());
        }

        if ("Programėlės naudojimo laikas".equals(selectedItem) || "Programėlės įjungimo skaičius".equals(selectedItem)) {
            appName = (String) goalAppItem.getSelectedItem();
        }

        if (dbHelper.taskExists(selectedItem)) {
            showToast("Tikslas su tokiu užduoties tipu jau egzistuoja");
            flag++;
        } else if ("Programėlės naudojimo laikas".equals(selectedItem) || "Programėlės įjungimo skaičius".equals(selectedItem)) {
            if (dbHelper.taskExists2(selectedItem, appName)) {
                showToast("Tikslas su tokiu užduoties tipu ir tokiu programėlės pavadinimu jau egzistuoja");
                flag++;
            } else {
                dbHelper.insertTask(selectedItem, time, appName);
                showToast("Naujas tikslas buvo sukurta sėkmingai");
            }
        } else {
            dbHelper.insertTask(selectedItem, time, appName);
            showToast("Naujas tikslas buvo sukurtas sėkmingai");
        }
    }


    private List<String> getInstalledApps(Context context) {
        List<String> installedApps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String appName = resolveInfo.loadLabel(packageManager).toString();
            installedApps.add(appName);
        }
        Collections.sort(installedApps);
        return installedApps;
    }

    private void updateGoalAppFrameConstraints(int anchorId) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) goalAppFrame.getLayoutParams();
        params.topToBottom = anchorId;
        goalAppFrame.setLayoutParams(params);
    }

    private void updateButtonConstraints(int anchorId) {
        ConstraintLayout.LayoutParams blackButtonParams = (ConstraintLayout.LayoutParams) blackButton.getLayoutParams();
        blackButtonParams.topToBottom = anchorId;
        blackButton.setLayoutParams(blackButtonParams);

        ConstraintLayout.LayoutParams yellowButtonParams = (ConstraintLayout.LayoutParams) yellowButton.getLayoutParams();
        yellowButtonParams.topToBottom = anchorId;
        yellowButton.setLayoutParams(yellowButtonParams);
    }
}
