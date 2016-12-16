package com.caixiaoqing.awesomereminder.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.caixiaoqing.awesomereminder.AlarmReceiver;
import com.caixiaoqing.awesomereminder.TodoEditActivity;
import com.caixiaoqing.awesomereminder.models.Todo;

import java.util.Calendar;

/**
 * Created by caixiaoqing on 16/12/16.
 */

public class AlarmUtils {

    public static void setAlarm(@NonNull Context context, @NonNull Todo todo) {
        //Step 1: validate alarm time
        Calendar c = Calendar.getInstance(); // c will contain the current time
        if (todo.remindDate.compareTo(c.getTime()) < 0) { // this statement checks if date is smaller than current time
            // we only fire alarm when date is in the future
            Toast.makeText(context, "setAlarm " + DateUtils.dateToStringTime(todo.remindDate) + " < "
                    +DateUtils.dateToStringTime(c.getTime()), Toast.LENGTH_LONG).show();

            return;
        }

        Toast.makeText(context, "setAlarm " + DateUtils.dateToStringTime(todo.remindDate), Toast.LENGTH_LONG).show();

        //Step 2: Intent w data => PendingIntent from getBroadcast
        Intent intent = new Intent(context, AlarmReceiver.class);
        //Specail case: Parcelable bug for AlarmManager
        //Just for pre-caution, do not pass anything beside primitive type into AlarmManager PendingIntent Extra
        intent.putExtra(TodoEditActivity.KEY_TODO_ID, todo.id);
        ModelUtils.save(context, todo.id, todo);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
                                    0,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

        //Step 3: AlarmManager (getSystemService) . set()
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, // will wake up the device
                todo.remindDate.getTime(),    // For test System.currentTimeMillis() + (15 * 1000),
                alarmIntent);
    }
}
