package com.caixiaoqing.awesomereminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.caixiaoqing.awesomereminder.models.Todo;
import com.caixiaoqing.awesomereminder.utils.DateUtils;
import com.caixiaoqing.awesomereminder.utils.UIUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class TodoEditActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener {

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";

    private EditText todoEdit;
    private TextView dateTv;
    private TextView timeTv;
    private CheckBox completeCb;

    private Todo todo;
    private Date remindDateFromPicker;
    
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
        getSupportActionBar().setElevation(0);//TODO 5 setElevation ?
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
            //TODO 5-2 think can remindDate be null?
            if(todo.remindDate != null) {
                dateTv.setText(DateUtils.dateToStringDate(todo.remindDate));
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

    private void setupDatePicker() {
        dateTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                if (todo != null && todo.remindDate != null) {
                    c.setTime(todo.remindDate);
                }
                Dialog dialog = new DatePickerDialog(
                        TodoEditActivity.this,
                        TodoEditActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
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
            Toast.makeText(this, "Added " + todo.id, Toast.LENGTH_LONG).show();
        }
        else{
            todo.text = todoEdit.getText().toString();
            todo.remindDate = remindDateFromPicker;
            Toast.makeText(this, "Updated " + todo.id, Toast.LENGTH_LONG).show();
        }

        todo.isDone = completeCb.isChecked();

        if (todo.remindDate != null) {
            //TODO 5-3 AlarmUtils.setAlarm(this, todo);
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
}
