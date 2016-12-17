package com.caixiaoqing.awesomereminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import com.caixiaoqing.awesomereminder.models.Todo;
import com.caixiaoqing.awesomereminder.utils.DateUtils;
import com.caixiaoqing.awesomereminder.utils.ModelUtils;
import com.caixiaoqing.awesomereminder.utils.ParcelableUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by caixiaoqing on 16/12/16.
 */

        //http://stackoverflow.com/questions/38775285/android-7-broadcastreceiver-onreceive-intent-getextras-missing-data
        //http://stackoverflow.com/questions/39573849/android-7-intent-extras-missing
        //Step 6: add receiver to manifest.xml
        //Step 7: AlarmUtils (AlarmManager will set(PendingIntent from getBroadcast)
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Step 1 : prep data from intent-input
        final int notificationId = 100; // this will be used to cancel the notification
        //Specail case: Parcelable bug for AlarmManager
        //*** = intent.getParcelableExtra(TodoEditActivity.KEY_TODO);
        String todoId = intent.getStringExtra(TodoEditActivity.KEY_TODO_ID);
        //*** *** = ModelUtils.get(context, todoId, new TypeToken<***>(){});

        byte[] bytes = intent.getByteArrayExtra(TodoEditActivity.KEY_TODO);
        Todo todo = ParcelableUtil.unmarshall(bytes, Todo.CREATOR);

        //Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.vibrate(2000);

        //Step 2: prep NotificationCompat.Builder w icon + text
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(todo.text)
                .setContentText(todo.text + " @ " + DateUtils.dateToStringTime(todo.remindDate));

        //Step 3: resultIntent w data => PendingIntent from getActivity
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, TodoEditActivity.class);
        resultIntent.putExtra(TodoEditActivity.KEY_TODO, todo);
        resultIntent.putExtra(TodoEditActivity.KEY_NOTIFICATION_ID, notificationId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                                                                        0,
                                                                        resultIntent,
                                                                        PendingIntent.FLAG_UPDATE_CURRENT);
        //Step 4: set builder.setContentIntent
        builder.setContentIntent(resultPendingIntent);

        //Step 5: NotificationManager (from getSystemService) . notify()
        NotificationManager nm = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        // notificationId allows you to update the notification later on, like canceling it
        nm.notify(notificationId, builder.build());
    }
}
