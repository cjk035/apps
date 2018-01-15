package tech.haowei.apps;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class AAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, AAService.class);
        startService(intent);
        Log.e("AAPPLICATION", "welcome");
    }
}
