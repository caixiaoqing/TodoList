package com.caixiaoqing.awesomereminder.adapters;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.caixiaoqing.awesomereminder.MainActivity;
import com.caixiaoqing.awesomereminder.R;
import com.caixiaoqing.awesomereminder.TodoEditActivity;
import com.caixiaoqing.awesomereminder.models.Todo;

import java.util.List;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class TodoListAdapter extends BaseAdapter {
    private MainActivity activity;
    private List<Todo> data;

    public TodoListAdapter(MainActivity activity, List<Todo> data){
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item, parent, false);

            vh = new ViewHolder();
            vh.todoText = (TextView) convertView.findViewById(R.id.list_item_text);
            vh.doneCheckbox = (CheckBox) convertView.findViewById(R.id.list_item_check);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        setupViewHolder(i, vh);

        setupIntent2TodoEditActivity(convertView, i);

        return convertView;
    }

    private void setupIntent2TodoEditActivity(View convertView, final int i) {
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TodoEditActivity.class);
                intent.putExtra(TodoEditActivity.KEY_TODO, (Todo) getItem(i));
                activity.startActivityForResult(intent, activity.REQ_CODE_TODO_EDIT);
            }
        });
    }

    private void setupViewHolder(final int i, ViewHolder vh) {
        final Todo todo = (Todo) getItem(i);
        vh.todoText.setText(todo.text);
        vh.doneCheckbox.setChecked(todo.isDone);

        vh.doneCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                activity.updateTodo(i, isChecked);
            }
        });
    }

    private static class ViewHolder {
        TextView todoText;
        CheckBox doneCheckbox;
    }
}
