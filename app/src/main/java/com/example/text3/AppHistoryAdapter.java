package com.example.text3;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppHistoryAdapter extends RecyclerView.Adapter<AppHistoryAdapter.ViewHolder> {

    private List<AppHistoryData> dataList;
    private PackageManager packageManager;

    public AppHistoryAdapter(List<AppHistoryData> dataList, PackageManager packageManager) {
        this.dataList = dataList;
        this.packageManager = packageManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_history_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppHistoryData data = dataList.get(position);
        holder.appNameTextView.setText(data.getAppName());
        holder.usageOpenedTextView.setText(data.getUsageOpened());
        holder.usageClosedTextView.setText(data.getUsageClosed());

        Drawable icon = getApplicationIcon(data.getPackageName());
        holder.appIconImageView.setImageDrawable(icon);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;
        TextView usageOpenedTextView;
        TextView usageClosedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            usageOpenedTextView = itemView.findViewById(R.id.usageOpenedTextView);
            usageClosedTextView = itemView.findViewById(R.id.usageClosedTextView);
        }
    }

    private Drawable getApplicationIcon(String packageName) {
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
