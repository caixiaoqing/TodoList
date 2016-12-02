package com.caixiaoqing.awesomereminder.models;

import java.util.Date;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class Todo{
    public String text;
    public boolean isDone;
    public Date remindDate;

    public Todo(String text, Date remindDate){
        this.text = text;
        this.isDone = false;
        this.remindDate = remindDate;
    }
}
