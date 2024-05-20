package com.example.text3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CategoryApps extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private Button buttonDateCategories;
    private Calendar selectedDate;
    private TextView textDateCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryapps);

        databaseHelper = new DatabaseHelper(this);

        textDateCategories = findViewById(R.id.textDateCategories2);

        buttonDateCategories = findViewById(R.id.categoryButton);

        buttonDateCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        selectedDate = Calendar.getInstance();
        updatePieChart(selectedDate);
        updateDateText(selectedDate);
    }

    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CategoryApps.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        updatePieChart(selectedDate);
                        updateDateText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void updatePieChart(Calendar selectedDate) {
        List<PieEntry> pieEntries = new ArrayList<>();
        List<CategoryAppsData> categoryAppsList = databaseHelper.getCategoryAppsForDate(selectedDate);
        for (CategoryAppsData categoryAppsData : categoryAppsList) {
            Log.d("DatabaseDetails", "Category: " + categoryAppsData.getCategory() + ", Value: " + categoryAppsData.getValue());
        }

        for (CategoryAppsData categoryAppsData : categoryAppsList) {
            pieEntries.add(new PieEntry(categoryAppsData.getValue(), categoryAppsData.getCategory()));
        }

        PieChart pieChart = findViewById(R.id.piegraphlayout);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.getDescription().setEnabled(false);
        pieChart.getDescription().setPosition(1f, 1f);
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new CategoryApps.PieValueFormatter());
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();

        Legend legend7 = pieChart.getLegend();
        legend7.setTextColor(Color.WHITE);
        legend7.setTextSize(12f);
        legend7.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend7.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend7.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend7.setDrawInside(false);
    }

    private void updateDateText(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        textDateCategories.setText(formattedDate);
    }

    private class PieValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes % 60);
        }
    }

    public static class CategoryAppsData {
        private String date;
        private String category;
        private long value;

        public CategoryAppsData(String date, String category, long value) {
            this.date = date;
            this.category = category;
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public String getCategory() {
            return category;
        }

        public long getValue() {
            return value;
        }
    }
}