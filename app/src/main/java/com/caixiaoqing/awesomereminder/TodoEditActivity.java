package com.caixiaoqing.awesomereminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.caixiaoqing.awesomereminder.models.Todo;
import com.caixiaoqing.awesomereminder.utils.AlarmUtils;
import com.caixiaoqing.awesomereminder.utils.DateUtils;
import com.caixiaoqing.awesomereminder.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";
    public static final String KEY_NOTIFICATION_ID = "notification_id";

    private EditText todoEdit;
    private TextView dateTv;
    private TextView timeTv;
    private CheckBox completeCb;

    private Todo todo;
    private Date remindDateFromPicker = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        
        getData();
        setupUI();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUI() {
        setupActionbar();//setupMenu();//onCreateOptionsMenu//getMenuInflater().inflate(R.menu.menu_main, menu);

        setupTodoTextView();
        setupCheckbox();
        setupDatePicker();

        setupEditDoneFab();
        setupDeleteFab();
    }

    private void setupActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //https://developer.android.com/training/material/shadows-clipping.html
        //http://hukai.me/android-training-course-in-chinese/material/shadows-clipping.html
        getSupportActionBar().setElevation(0);
        setTitle(null);
    }

    private void setupTodoTextView() {
        todoEdit = (EditText) findViewById(R.id.toto_detail_todo_edit);
        dateTv = (TextView) findViewById(R.id.todo_detail_date);
        timeTv = (TextView) findViewById(R.id.todo_detail_time);
        completeCb = (CheckBox) findViewById(R.id.todo_detail_complete);

        if(todo != null){
            todoEdit.setText(todo.text);
            UIUtils.setTextViewStrikeThrough(todoEdit, todo.isDone);
            completeCb.setChecked(todo.isDone);

            if(todo.remindDate != null) {
                dateTv.setText(DateUtils.dateToStringDate(todo.remindDate));
                //Special Case : what if user set date without time?
                timeTv.setText(DateUtils.dateToStringTime(todo.remindDate));
            }
        }

        if(todo == null || todo.remindDate == null){
            dateTv.setText(R.string.set_date);
            timeTv.setText(R.string.set_time);
        }
    }

    private void setupCheckbox() {
        completeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                UIUtils.setTextViewStrikeThrough(todoEdit, isChecked);
                todoEdit.setTextColor(isChecked ? Color.GRAY : Color.WHITE);
            }
        });

        // use this wrapper to make it possible for users to click on the entire row
        // to change the check box
        View completeWrapper = findViewById(R.id.todo_detail_complete_wrapper);
        completeWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeCb.setChecked(!completeCb.isChecked());
            }
        });
    }

    //Step 1: DatePickerDialog and show onClick()  + impl TimePickerDialog.OnTimeSetListener
    private void setupDatePicker() {
        dateTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = getCalendarFromRemindDate();
                Dialog dialog = new TimePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        true);
                dialog.show();
            }
        });
    }

    private Calendar getCalendarFromRemindDate() {
        Calendar c = Calendar.getInstance();
        if (todo != null && todo.remindDate != null) {
            c.setTime(todo.remindDate);
        }
        return c;
    }

    private void setupEditDoneFab() {
        findViewById(R.id.todo_detail_done).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        if(todo == null){
            todo = new Todo(todoEdit.getText().toString(), remindDateFromPicker);
        }
        else{
            todo.text = todoEdit.getText().toString();
            //Special Case : user just open and save (without pick date-time)
            if(remindDateFromPicker != null) {
                todo.remindDate = remindDateFromPicker;
            }
        }

        todo.isDone = completeCb.isChecked();

        if (todo.remindDate != null) {
            AlarmUtils.setAlarm(this, todo);
        }

        Intent result = new Intent();
        result.putExtra(KEY_TODO, todo);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setupDeleteFab() {
        if(todo == null){
            //id is null if is --add()-->  in this route hide 'delete'
            findViewById(R.id.todo_delete).setVisibility(View.GONE);
        }
        else{
            findViewById(R.id.todo_delete).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        }
    }

    private void delete() {
        Intent result = new Intent();
        result.putExtra(KEY_TODO_ID, todo.id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void getData() {
        todo = getIntent().getParcelableExtra(KEY_TODO);

        //But if you want to set some thing different, like "Set Date", "Set Time", keep it as null
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        //todo.remindDate = c.getTime(); //BUG it can be still null yet. --add()-->
        remindDateFromPicker = c.getTime();
        dateTv.setText(DateUtils.dateToStringDate(remindDateFromPicker));
    }

    //Step 2: remember to init calendar with remindDateFromPicker + save c.getTime() back;
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = getCalendarFromRemindDate();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        remindDateFromPicker = c.getTime();
        timeTv.setText(DateUtils.dateToStringTime(remindDateFromPicker));
    }
}
