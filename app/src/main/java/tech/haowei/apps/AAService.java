package tech.haowei.apps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.Buffer;

public class AAService extends Service {

    public static Socket socket;
    public boolean isConected = false;

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
                try {
                    socket = new Socket("118.193.158.249", 9500);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.write("ELHO".getBytes());
                    dos.flush();

                    Intent intent;
                    byte[] b = new byte[1024];
                    StringBuilder raw = new StringBuilder();

                    while (true) {
                        dis.read(b);
                        raw.append(new String(b).trim());
                        if (raw.length() > 0) {
                            Log.e("AASERVICE", "readLine");
                            intent = new Intent("com.android.action.broadcast");
                            intent.putExtra("text", raw.toString());
                            sendBroadcast(intent);
                        }
                    }

                } catch (IOException e) {
                    isConected = false;
                    Log.e("AASERVICE", e.getMessage());
                }
            }

        }).start();
    }

}
