package com.caixiaoqing.awesomereminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.caixiaoqing.awesomereminder.adapters.TodoListAdapter;
import com.caixiaoqing.awesomereminder.models.Todo;
import com.caixiaoqing.awesomereminder.utils.DateUtils;
import com.caixiaoqing.awesomereminder.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_TODO_EDIT = 100;

    private static final String TODOS = "todos";

    private List<Todo> todos;
    private TodoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        loadData();
        setupUI();
    }

    private void setupUI() {
        setupAddFab();
        setupListView();
    }

    private void setupListView() {
        adapter = new TodoListAdapter(this, todos);
        ((ListView) findViewById(R.id.main_list_view)).setAdapter(adapter);
    }

    private void setupAddFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodoEditActivity.class);
                startActivityForResult(intent, REQ_CODE_TODO_EDIT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_TODO_EDIT && resultCode == Activity.RESULT_OK) {

            String todoId = data.getStringExtra(TodoEditActivity.KEY_TODO_ID);
            if (todoId != null) {
                deleteTodoItem(todoId);
            } else {
                Todo todoFromEdit = data.getParcelableExtra(TodoEditActivity.KEY_TODO);
                updateTodoItem(todoFromEdit);
            }
        }
    }

    public void updateTodo(int i, boolean isChecked) {
        Todo t = todos.get(i);
        t.isDone = isChecked;
        todos.set(i, t);

        adapter.notifyDataSetChanged();
        ModelUtils.save(this, TODOS, todos);
    }

    private void updateTodoItem(Todo todoFromEdit) {
        boolean found = false;
        for (int i = 0; i < todos.size(); ++i) {
            Todo t = todos.get(i);
            if(t.id.equals(todoFromEdit.id)){
                //t = todoFromEdit; //No effec
                todos.set(i, todoFromEdit);
                found = true;
                break;
            }
        }

        if(!found){
            todos.add(todoFromEdit); //Add
        }
        adapter.notifyDataSetChanged();
        ModelUtils.save(this, TODOS, todos);
    }

    private void deleteTodoItem(@NonNull String id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
        for (int i = 0; i < todos.size(); ++i) {
            Todo item = todos.get(i);
            if (TextUtils.equals(item.id, id)) {
                todos.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
        ModelUtils.save(this, TODOS, todos);
    }

    private void loadData() {
        todos = ModelUtils.get(this, TODOS, new TypeToken<List<Todo>>(){});
        if(todos == null){
            todos = new ArrayList<>();
        }
    }

}
