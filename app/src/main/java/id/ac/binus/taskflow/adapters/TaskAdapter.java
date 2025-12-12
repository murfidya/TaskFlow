package id.ac.binus.taskflow.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import id.ac.binus.taskflow.EditTaskActivity;
import id.ac.binus.taskflow.R;
import id.ac.binus.taskflow.models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.categoryTextView.setText(task.getCategory());
        holder.statusTextView.setText(task.getStatus());

        if (task.getDueDate() != null) {
            holder.dueDateTextView.setText(dateFormat.format(task.getDueDate().toDate()));
        } else {
            holder.dueDateTextView.setText("No due date");
        }

        // Set priority color
        switch (task.getPriority() != null ? task.getPriority() : "Low") {
            case "High":
                holder.priorityIndicator.setBackgroundColor(Color.parseColor("#F44336"));
                break;
            case "Medium":
                holder.priorityIndicator.setBackgroundColor(Color.parseColor("#FF9800"));
                break;
            default:
                holder.priorityIndicator.setBackgroundColor(Color.parseColor("#4CAF50"));
                break;
        }

        // Set status color
        switch (task.getStatus() != null ? task.getStatus() : "Pending") {
            case "Completed":
                holder.statusTextView.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case "In Progress":
                holder.statusTextView.setTextColor(Color.parseColor("#2196F3"));
                break;
            default:
                holder.statusTextView.setTextColor(Color.parseColor("#FF9800"));
                break;
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("taskId", task.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View priorityIndicator;
        TextView titleTextView, descriptionTextView, categoryTextView, statusTextView, dueDateTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_task);
            priorityIndicator = itemView.findViewById(R.id.priority_indicator);
            titleTextView = itemView.findViewById(R.id.text_title);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            categoryTextView = itemView.findViewById(R.id.text_category);
            statusTextView = itemView.findViewById(R.id.text_status);
            dueDateTextView = itemView.findViewById(R.id.text_due_date);
        }
    }
}
