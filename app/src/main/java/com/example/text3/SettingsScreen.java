package com.example.text3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

public class SettingsScreen extends Fragment {

    public SettingsScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_settingsscreen, container, false);

        rootView.findViewById(R.id.yellowButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UsageTips.class));
            }
        });

        rootView.findViewById(R.id.yellowButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TroubleshootingScreen.class));
            }
        });

        rootView.findViewById(R.id.yellowButton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:")); // This ensures only SMS apps respond
                smsIntent.putExtra("sms_body", "Sveikas, naudoju programėlę, kuri seka mano telefono naudojimą, nes stengiuosi telefoną naudoti rečiau, jei tave domina, prisijunk prie manęs! \"https://we.tl/t-Ta7RE3iL0Z\"");

                if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Toast.makeText(getActivity(), "Nerasta praneimo siuntimo programėlės", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}