package tech.haowei.apps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AAReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "来自广播消息：" + intent.getStringExtra("text"),Toast.LENGTH_LONG).show();
        Log.e("AARECEIVER", "receiver successful");
    }

}
