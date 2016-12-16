package com.caixiaoqing.awesomereminder.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by caixiaoqing on 2/12/16.
 */

public class Todo implements Parcelable {
    public String id;
    public String text;
    public boolean isDone;
    public Date remindDate;

    public Todo(String text, Date remindDate){
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.isDone = false;
        this.remindDate = remindDate;
    }

    protected Todo(Parcel in) {
        id = in.readString();
        text = in.readString();
        isDone = in.readByte() != 0;

        //Add parcel for Date
        long date = in.readLong();
        remindDate = date == 0 ? null : new Date(date);
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

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
