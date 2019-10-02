package org.bitart.testtaskmakeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.bitart.testtaskmakeapp.database.TaskDBSchema;

import java.util.ArrayList;
import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {

    private FloatingActionButton mFloatingActionButton;

    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;
    private ArrayList<Task> mTaskList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_name);

        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(view -> {
            Intent intent = TaskDetailActivity.newIntent(this, null);
            startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.recyclerViewTaskList);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mTaskList = Singleton.getInstance(this).getTasks();
        TaskAdapter taskAdapter = this.mTaskAdapter;
        if(taskAdapter == null) {
            mTaskAdapter = new TaskAdapter();
            mRecyclerView.setAdapter(mTaskAdapter);
            return;
        }
        taskAdapter.notifyDataSetChanged();
    }

    private class TaskHolder extends RecyclerView.ViewHolder {
        private Task mTask;

        private final TextView mHeader;
        private final TextView mBody;
        private final View mProirityIndicator;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.task_item, parent, false));

            this.itemView.setOnClickListener(view -> {
                Intent intent = TaskDetailActivity.newIntent(TaskListActivity.this, mTask.getId());
                startActivity(intent);
            });

            this.itemView.setOnLongClickListener(view -> {
                AlertDialog deleteDialog = new AlertDialog.Builder(TaskListActivity.this)
                        .setMessage(R.string.alert_dialog_message)
                        .setPositiveButton(R.string.alert_dialog_positive, (dialogInterface, i) -> {
                            int position = mTaskList.indexOf(mTask);
                            mTaskList.remove(position);
                            Singleton.getInstance(TaskListActivity.this).removeTask(mTask);
                            mTaskAdapter.notifyItemRemoved(position);
                            Toast.makeText(TaskListActivity.this, "Дело удалено", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(R.string.alert_dialog_negative, ((dialogInterface, i) -> {
                        }))
                        .create();
                deleteDialog.show();
                deleteDialog.getButton(deleteDialog.BUTTON_POSITIVE)
                        .setBackgroundColor(getResources().getColor(android.R.color.transparent));
                deleteDialog.getButton(deleteDialog.BUTTON_NEGATIVE)
                        .setBackgroundColor(getResources().getColor(android.R.color.transparent));
                deleteDialog.getButton(deleteDialog.BUTTON_POSITIVE)
                        .setTextColor(getResources().getColor(R.color.colorAccent));
                deleteDialog.getButton(deleteDialog.BUTTON_NEGATIVE)
                        .setTextColor(getResources().getColor(R.color.colorAccent));
                return true;
            });

            this.mHeader = this.itemView.findViewById(R.id.textViewTaskItemHeader);
            this.mBody = this.itemView.findViewById(R.id.textViewTaskItemBody);
            this.mProirityIndicator = this.itemView.findViewById(R.id.priorityIndicator);
        }

        public void bind(Task task) {
            mTask = task;

            mHeader.setText(task.getHeader());
            mBody.setText(task.getBody());

            switch (task.getPriority()) {
                case Task.LOW_PRIOROTY:
                    mProirityIndicator.setBackgroundResource(R.color.lowPriority);
                    break;
                case Task.MEDIUM_PRIOROTY:
                    mProirityIndicator.setBackgroundResource(R.color.mediumPriority);
                    break;
                case Task.HIGH_PRIOROTY:
                    mProirityIndicator.setBackgroundResource(R.color.highPriority);
                    break;
            }
        }

    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(TaskListActivity.this);
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(mTaskList.get(position));
        }

        @Override
        public int getItemCount() {
            return mTaskList.size();
        }
    }

    private int dpToPixels(int dp) {
        return (int) (dp * this.getResources().getDisplayMetrics().density);
    }
}
