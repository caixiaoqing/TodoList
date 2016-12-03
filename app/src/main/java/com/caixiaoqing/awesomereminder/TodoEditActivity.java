package com.caixiaoqing.awesomereminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.caixiaoqing.awesomereminder.models.Todo;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class TodoEditActivity extends AppCompatActivity {

    public static final String KEY_TODO = "todo";
    public static final String KEY_TODO_ID = "todo_id";

    private Todo todo;
    
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
        setupActionbar();//setupMenu();

        setupTodoTextView();
        setupDate();
        setupTime();

        setupEditDone();
        setupDeleteFab();
    }

    private void setupActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);//TODO 5 setElevation ?
        setTitle(null);
    }

    private void setupDeleteFab() {
        Intent result = new Intent();
        result.putExtra(KEY_TODO_ID, todo.id); //TODO todo.id is null if is --add()-->  in this route hide 'delete'
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void setupEditDone() {

    }

    private void setupTime() {

    }

    private void setupDate() {

    }

    private void setupTodoTextView() {

    }

    private void getData() {
        //TODO load from intent
        todo = getIntent().getParcelableExtra(KEY_TODO);

        //todo = new Todo("", DateUtils.getCurrentDateTime());
        //But if you want to set some thing different, like "Set Date", "Set Time", keep it as null
    }
}
