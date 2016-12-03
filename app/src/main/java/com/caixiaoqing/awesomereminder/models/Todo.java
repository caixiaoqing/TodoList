package com.caixiaoqing.awesomereminder.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class Todo implements Parcelable {
    public String id;
    public String text;
    public boolean isDone;
    public Date remindDate;

    public Todo(String text, Date remindDate){
        this.text = text;
        this.isDone = false;
        this.remindDate = remindDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeByte((byte) (isDone ? 1 : 0));
        dest.writeLong(remindDate != null ? remindDate.getTime() : 0);
    }
}
