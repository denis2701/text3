package com.example.text3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlockedAppAdapter extends RecyclerView.Adapter<BlockedAppAdapter.ViewHolder> {

    private List<String> blockedAppNames;

    public BlockedAppAdapter(List<String> blockedAppNames) {
        this.blockedAppNames = blockedAppNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String appName = blockedAppNames.get(position);
        holder.appNameTextView.setText(appName);
    }

    @Override
    public int getItemCount() {
        return blockedAppNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
        }
    }
}
