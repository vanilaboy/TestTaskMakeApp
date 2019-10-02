package org.bitart.testtaskmakeapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "org.bitart.testtaskmakeapp.task_id";

    private Task mTask;
    private EditText mEditTextHeader;
    private EditText mEditTextBody;
    private ImageButton mExitButton;

    private int mProirity;
    private FloatingActionButton mFabLow;
    private FloatingActionButton mFabMedium;
    private FloatingActionButton mFabHigh;
    private ArrayList<FloatingActionButton> mFabList = new ArrayList<>();

    private Button mButtonCreateTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_details);

        try {
            UUID id = (UUID) getIntent().getExtras().getSerializable(EXTRA_TASK_ID);
            mTask = Singleton.getInstance(this).getTask(id);
        } catch (NullPointerException e) {
            mTask = null;
        }

        mEditTextHeader = findViewById(R.id.editTextTaskDetailHeader);
        mEditTextBody = findViewById(R.id.editTextTaskDetailBody);
        mButtonCreateTask = findViewById(R.id.buttonCreateTask);
        mButtonCreateTask.setOnClickListener(view -> {
            Task task = new Task();
            task.setHeader(mEditTextHeader.getText().toString());
            if (task.getHeader().equals("")) {
                Toast.makeText(TaskDetailActivity.this, "Введите заголовок", Toast.LENGTH_SHORT).show();
                mEditTextHeader.requestFocus();
            } else {
                task.setBody(mEditTextBody.getText().toString());
                task.setPriority(mProirity);

                if(mTask == null) {
                    Singleton.getInstance(TaskDetailActivity.this).addTask(task);
                } else {
                    task.setId(mTask.getId());
                    Singleton.getInstance(TaskDetailActivity.this).updateTask(task);
                }

                finish();
                Toast.makeText(getApplicationContext(), "Задача добавлена", Toast.LENGTH_SHORT).show();
            }
        });
        mExitButton = findViewById(R.id.imageButtonClear);
        mExitButton.setOnClickListener(view -> {
            finish();
        });

        mFabLow = findViewById(R.id.floatingActionButtonLow);
        mFabMedium = findViewById(R.id.floatingActionButtonMedium);
        mFabHigh = findViewById(R.id.floatingActionButtonHigh);

        mFabList.add(mFabLow);
        mFabList.add(mFabMedium);
        mFabList.add(mFabHigh);

        for(int i = 0; i < mFabList.size(); i++) {
            FloatingActionButton fab = mFabList.get(i);
            fab.setOnClickListener(view -> {
                mProirity = mFabList.indexOf(fab);
                for(FloatingActionButton localFab : mFabList) {
                    localFab.setImageResource(android.R.color.transparent);
                }
                ((FloatingActionButton)view).setImageResource(R.drawable.ic_pick);
            });
        }

        if(mTask != null) {
            mEditTextHeader.setText(mTask.getHeader());
            mEditTextBody.setText(mTask.getBody());
            mFabList.get(mTask.getPriority()).callOnClick();
        }
    }





    public static Intent newIntent(Context paramContext, UUID id) {
        Intent intent = new Intent(paramContext, TaskDetailActivity.class);
        if(id != null) {
            intent.putExtra(EXTRA_TASK_ID, id);
        }
        return intent;
    }
}
