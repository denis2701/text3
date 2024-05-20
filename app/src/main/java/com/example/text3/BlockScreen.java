package com.example.text3;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class BlockScreen extends Fragment {

    private RecyclerView recyclerView;
    private BlockedAppAdapter adapter;
    private List<String> blockedAppNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_blockscreen, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        blockedAppNames = databaseHelper.getBlockedAppNames();

        List<String> blockedAppNamesWithLabels = getAppNamesForBlockedPackages(blockedAppNames);

        adapter = new BlockedAppAdapter(blockedAppNamesWithLabels);
        recyclerView.setAdapter(adapter);

        Button appUsageButton = view.findViewById(R.id.appUsageButton);

        appUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppList.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private List<String> getAppNamesForBlockedPackages(List<String> blockedPackages) {
        List<String> appNames = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();
        for (String packageName : blockedPackages) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                appNames.add(appName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appNames;
    }
}