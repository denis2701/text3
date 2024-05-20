package com.example.text3;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private DatabaseHelper dbHelper;

    public TaskAdapter(List<Task> taskList, DatabaseHelper dbHelper) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskInfoTextView;
        private Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskInfoTextView = itemView.findViewById(R.id.taskInfoTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Task task) {
            String taskInfo = task.getType();
            if (!TextUtils.isEmpty(task.getTime())) {
                taskInfo += " yra ne daugiau nei " + task.getTime();
            }
            if (!TextUtils.isEmpty(task.getAppName())) {
                taskInfo += " programėlei " + task.getAppName();
            }
            taskInfoTextView.setText(taskInfo);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task deletedTask = taskList.get(position);

                        taskList.remove(position);
                        notifyItemRemoved(position);

                        boolean deleted = dbHelper.deleteTask(deletedTask.getId());
                        if (deleted) {
                            Toast.makeText(itemView.getContext(), "Tikslas ištrintas sėkmingai", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
