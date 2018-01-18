package tech.haowei.apps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AAReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            int id = intent.getIntExtra("id", 0);
            NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,
                    MainActivity.class), 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("广播消息")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pi, true)
                    .setContentText(intent.getStringExtra("text"));

            notifyManager.notify(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AARECEIVER", "notification error: " + e.getMessage());
        }
        Log.e("AARECEIVER", "receiver successful");
    }

}
