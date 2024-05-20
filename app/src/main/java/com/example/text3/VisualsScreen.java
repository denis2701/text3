package com.example.text3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class VisualsScreen extends AppCompatActivity {

    RadioGroup colorChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visuals);

        colorChoices = findViewById(R.id.colorChoices);

        colorChoices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int color = getColorFromRadioButtonId(checkedId);
            //    updateUsageTime(findViewById(android.R.id.content), color);
            }
        });
    }

    private int getColorFromRadioButtonId(int radioButtonId) {
        if (radioButtonId == R.id.redRadioButton) {
            return Color.RED;
        } else if (radioButtonId == R.id.whiteRadioButton) {
            return Color.WHITE;
        } else if (radioButtonId == R.id.yellowRadioButton) {
            return Color.YELLOW;
        } else {
            return Color.YELLOW; // Default to yellow
        }
    }
}