package com.example.text3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<String> appNames;
    private Context context;

    public AppListAdapter(Context context, List<String> appNames) {
        this.context = context;
        this.appNames = appNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.appNameTextView.setText(appNames.get(position));
        holder.appCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });
    }

    @Override
    public int getItemCount() {
        return appNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox appCheckbox;
        TextView appNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appCheckbox = itemView.findViewById(R.id.appCheckbox);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
        }
    }
}