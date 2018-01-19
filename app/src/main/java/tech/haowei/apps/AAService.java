package tech.haowei.apps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AAService extends Service {

    public static Socket socket;
    public int errorConnection = 0;
    public final int maxErrorConnection = 1;

    @Override
    public void onCreate() {
        tcpInit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        socket = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void tcpInit() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                Intent intent = new Intent("tech.haowei.apps.broadcast");

                try {

                    socket = new Socket("118.193.158.249", 9500);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.write("ELHO".getBytes());
                    dos.flush();

                    byte[] b = new byte[1024];
                    StringBuilder raw = new StringBuilder();

                    while (true) {
                        dis.read(b);
                        raw.append(new String(b).trim());
                        if (raw.length() > 0) {
                            Log.e("AASERVICE", "readLine");
                            intent.putExtra("text", raw.toString());
                            intent.putExtra("id", errorConnection);
                            sendBroadcast(intent);
                        }
                    }

                } catch (IOException e) {
                    Log.e("AASERVICE", e.getMessage());
                    intent.putExtra("id", errorConnection);
                    intent.putExtra("text", "服务连接失败");
                    sendBroadcast(intent);
                    errorConnection++;
                }

                Log.e("AASERVICE", "connection reset: " + errorConnection);

                if (errorConnection > 0 && errorConnection < maxErrorConnection) {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.e("AASERVICE.THREAD", e.getMessage());
                    }

                    tcpInit();

                } else {
                    Log.e("AASERVICE", "connection reset timeout");
                }

            }

        }).start();
    }

}
