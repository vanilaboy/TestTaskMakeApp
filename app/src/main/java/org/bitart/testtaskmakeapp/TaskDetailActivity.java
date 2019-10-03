package org.bitart.testtaskmakeapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String EXTRA_TASK_ID = "org.bitart.testtaskmakeapp.task_id";

    private Task mTask;
    private final Calendar mCalendar = Calendar.getInstance();

    private EditText mEditTextHeader;
    private EditText mEditTextBody;
    private EditText mEditTextDate;
    private ImageButton mExitButton;

    private int mPriority;
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

        mEditTextDate = findViewById(R.id.editTextDate);
        setDateTimePicker();

        mEditTextHeader = findViewById(R.id.editTextTaskDetailHeader);
        mEditTextBody = findViewById(R.id.editTextTaskDetailBody);
        mButtonCreateTask = findViewById(R.id.buttonCreateTask);
        mButtonCreateTask.setOnClickListener(view -> {
            Task task = new Task();
            task.setHeader(mEditTextHeader.getText().toString());
            if (task.getHeader().equals("")) {
                mEditTextHeader.requestFocus();
                mEditTextHeader.callOnClick();
                return;
            }

            task.setBody(mEditTextBody.getText().toString());
            task.setPriority(mPriority);
            task.setDate(mEditTextDate.getText().toString());
            if(task.getDate().equals("")) {
                mEditTextDate.callOnClick();
                return;
            }

            if (mTask == null) {
                Singleton.getInstance(TaskDetailActivity.this).addTask(task);
            } else {
                task.setId(mTask.getId());
                Singleton.getInstance(TaskDetailActivity.this).updateTask(task);
            }

            setAlarm(task);
            finish();
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
                mPriority = mFabList.indexOf(fab);
                for(FloatingActionButton localFab : mFabList) {
                    localFab.setImageResource(android.R.color.transparent);
                }
                ((FloatingActionButton)view).setImageResource(R.drawable.ic_pick);
            });
        }

        if(mTask != null) {
            mEditTextHeader.setText(mTask.getHeader());
            mEditTextBody.setText(mTask.getBody());
            mEditTextDate.setText(mTask.getDate());
            mFabList.get(mTask.getPriority()).callOnClick();
        }

    }

    private void setDateTimePicker() {

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mCalendar.set(Calendar.HOUR_OF_DAY, i);
                mCalendar.set(Calendar.MINUTE, i1);

                String myFormat = "dd/MM/yyyy - HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);

                mEditTextDate.setText(sdf.format(mCalendar.getTime()));
            }
        };

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                mCalendar.set(Calendar.YEAR, i);
                mCalendar.set(Calendar.MONTH, i1);
                mCalendar.set(Calendar.DAY_OF_MONTH, i2);

                new TimePickerDialog(
                        TaskDetailActivity.this,
                        time,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE),
                        true
                ).show();
            }
        };

        mEditTextDate.setOnClickListener(view -> new DatePickerDialog(
                TaskDetailActivity.this,
                date,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH))
                .show());
    }

    private void setAlarm(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = AlarmReceiver.newIntent(this, task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                mCalendar.getTimeInMillis() - 1000 * 60 * 60,
                pendingIntent);
    }


    public static Intent newIntent(Context paramContext, UUID id) {
        Intent intent = new Intent(paramContext, TaskDetailActivity.class);
        if(id != null) {
            intent.putExtra(EXTRA_TASK_ID, id);
        }
        return intent;
    }
}
