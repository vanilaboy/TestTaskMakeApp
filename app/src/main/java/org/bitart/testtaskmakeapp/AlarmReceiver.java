package org.bitart.testtaskmakeapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String EXTRA_TASK_ID = "task_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        UUID id = (UUID) intent.getExtras().getSerializable(EXTRA_TASK_ID);
        Task task = Singleton.getInstance(context).getTask(id);
        if(task != null && checkDate(task.getDate())) {
            sendNotification(context, task);
        }
    }

    public static void sendNotification(Context context, Task task) {
        Intent intent = TaskDetailActivity.newIntent(context, task.getId());
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder notification = new Notification.Builder(context);
        notification.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_clear)
//                .setContentTitle(context.getResources().getString(R.string.notification_header))
                .setContentTitle(task.getHeader())
                .setPriority(Notification.PRIORITY_LOW)
                .setContentText(context.getResources().getString(R.string.notification_body));


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("133", "My channel_v1.3",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Zachem?");
            channel.enableVibration(false);

            notification.setChannelId("133");
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, notification.build());
    }

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        if(id != null) {
            intent.putExtra(EXTRA_TASK_ID, id);
        }
        return intent;
    }

    private boolean checkDate(String strDate) {
        String myFormat = "dd/MM/yyyy - HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        try {
            Date notificationDate = sdf.parse(strDate);
            Date current = new Date(System.currentTimeMillis());
            if(Math.abs(current.getTime() - notificationDate.getTime()) > 1000 * 60 * 60 + 1000 * 60) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
