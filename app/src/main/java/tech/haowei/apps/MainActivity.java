package tech.haowei.apps;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    public String appTitle = "小程序";
    public WebView webView;
    public TextView titleView;
    public TextView firstButton;
    public TextView beforeButton;
    public TextView afterButton;
    public TextView loadAppsIcon;
    public RelativeLayout loadView;
    public View splView;
    public RelativeLayout rightView;
    public RelativeLayout navigatorView;
    public Vibrator vibrator;
    public Window window;
    public WifiManager wifi;
    public ClipboardManager cm;
    public AnimatorSet aset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "statics/font/apps.ttf");
        vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams
                .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        aset = new AnimatorSet();


        splView = (View) findViewById(R.id.spl);
        titleView = (TextView) findViewById(R.id.navigatorTitle);
        navigatorView = (RelativeLayout) findViewById(R.id.navigatorBar);
        rightView = (RelativeLayout) findViewById(R.id.rightSide);
        loadAppsIcon = (TextView) findViewById(R.id.loadAppsIcon);
        titleView = (TextView) findViewById(R.id.navigatorTitle);
        loadView = (RelativeLayout) findViewById(R.id.loadView);
        rightView.bringToFront();

        firstButton = (TextView) findViewById(R.id.firstButton);
        beforeButton = (TextView) findViewById(R.id.beforeButton);
        afterButton = (TextView) findViewById(R.id.afterButton);

        loadAppsIcon.setTypeface(iconfont);
        firstButton.setTypeface(iconfont);
        beforeButton.setTypeface(iconfont);
        afterButton.setTypeface(iconfont);


        if (firstButton.getVisibility() == View.GONE) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMarginStart(dip2px(15));
            titleView.setLayoutParams(params);
        }


        afterButton.setLongClickable(true);
        afterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });

        beforeButton.setLongClickable(true);
        beforeButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                try {
                    webView.clearCache(true);
                    webView.reload();
                    if (vibrator != null) vibrator.vibrate(new long[]{100, 200}, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

        });

        beforeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        LinearLayout view = new LinearLayout(MainActivity.this);
                        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        view.setOrientation(LinearLayout.VERTICAL);
                        TextView text = new TextView(MainActivity.this);
                        text.setText("分享");
                        text.setTextColor(Color.parseColor("black"));
                        text.setTextSize(16);
                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(40)));
                        text.setGravity(Gravity.CENTER);
                        view.addView(text);

                        text = new TextView(MainActivity.this);
                        text.setText("投诉");
                        text.setTextColor(Color.parseColor("black"));
                        text.setTextSize(16);
                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(40)));
                        text.setGravity(Gravity.CENTER);
                        view.addView(text);

                        text = new TextView(MainActivity.this);
                        text.setText("添加到桌面");
                        text.setTextColor(Color.parseColor("black"));
                        text.setTextSize(16);
                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(40)));
                        text.setGravity(Gravity.CENTER);
                        view.addView(text);

                        text = new TextView(MainActivity.this);
                        text.setText("关于".concat(appTitle));
                        text.setTextColor(Color.parseColor("black"));
                        text.setTextSize(16);
                        text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(40)));
                        text.setGravity(Gravity.CENTER);
                        view.addView(text);


                        new AlertDialog.Builder(MainActivity.this)
                                .setView(view)
                                .create().show();
                    }

                });
            }

        });


        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        View animate_1 = (View) findViewById(R.id.animate_1);
        View animate_2 = (View) findViewById(R.id.animate_2);
        View animate_3 = (View) findViewById(R.id.animate_3);


        ObjectAnimator scaleA1 = ObjectAnimator.ofFloat(animate_1, "alpha", 0.2f, 1, 0.2f);
        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(animate_1, "scaleX", 0.5f, 0.75f, 1, 0.5f);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(animate_1, "scaleY", 0.5f, 0.75f, 1, 0.5f);
        scaleA1.setRepeatCount(ValueAnimator.INFINITE);
        scaleX1.setRepeatCount(ValueAnimator.INFINITE);
        scaleY1.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator scaleA2 = ObjectAnimator.ofFloat(animate_2, "alpha", 0.2f, 1, 0.2f);
        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(animate_2, "scaleX", 1, 0.75f, 0.5f, 1);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(animate_2, "scaleY", 1, 0.75f, 0.5f, 1);
        scaleA2.setRepeatCount(ValueAnimator.INFINITE);
        scaleX2.setRepeatCount(ValueAnimator.INFINITE);
        scaleY2.setRepeatCount(ValueAnimator.INFINITE);

        ObjectAnimator scaleA3 = ObjectAnimator.ofFloat(animate_3, "alpha", 0.2f, 1, 0.2f);
        ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(animate_3, "scaleX", 0.5f, 0.75f, 1, 0.5f);
        ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(animate_3, "scaleY", 0.5f, 0.75f, 1, 0.5f);
        scaleA3.setRepeatCount(ValueAnimator.INFINITE);
        scaleX3.setRepeatCount(ValueAnimator.INFINITE);
        scaleY3.setRepeatCount(ValueAnimator.INFINITE);

        aset.play(scaleX1).with(scaleY1).with(scaleA1);
        aset.play(scaleX2).with(scaleY2).with(scaleA2);
        aset.play(scaleX3).with(scaleY3).with(scaleA3);


        aset.setDuration(800);
        aset.start();


        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setUserAgentString("Mozilla 5.0 Android Apps (1.0)");
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setDefaultTextEncodingName("UTF -8");
        webView.getSettings().setBlockNetworkImage(true);
        webView.addJavascriptInterface(new JavaScript(), "apps");

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null) {
            Log.e("INTENT", url);
            webView.loadUrl(url);
        } else {

            try {

                Properties pro = new Properties();
                FileInputStream fis = getApplicationContext().openFileInput
                        ("config.properties");
                pro.load(fis);
                url = pro.getProperty("homeurl");
                Log.e("LOAD.ATTR.URL", url);
                if (url != null) {
                    webView.loadUrl(url);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("错误")
                            .setMessage("你给了一个错误的URL")
                            .setCancelable(false)
                            .setPositiveButton("确定", null)
                            .create().
                            show();
                }
                fis.close();
            } catch (Exception e) {
                webView.loadUrl("https://haowei.asia");
                Log.e("LOAD.ATTRIBUTES", e.getMessage());
            }

        }


        webView.setWebViewClient(new WebViewClient() {

            @Override
            @TargetApi(23)
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            @SuppressLint("NewApi")
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loadView.setVisibility(View.GONE);
                    }

                });

            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

            /*private boolean done = false;

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (done && newProgress < 75) {
                    done = false;
                } else if (!done && newProgress >= 75) {
                    done = true;
                    Log.e("LOCAL.SERVICE", "JavaScript Service Bridge Successful");
                    webView.loadUrl("javascript:window.dispatchEvent(new Event('ready'))");
                }
            }*/

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("WEBVIEW.CONSOLE", consoleMessage.message());
                Log.e("WEBVIEW.CONSOLE", "LINE: " + consoleMessage.lineNumber());
                return true;
            }

        });

    }

    public int dip2px(int dip) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (scale * dip + 0.5f);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        super.onBackPressed();
    }

    public void showBroadcast(final Object obj) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("广播消息")
                        .setMessage(obj.toString())
                        .create()
                        .show();
            }

        });
    }

    public class JavaScript {

        @JavascriptInterface
        public void setNavigatorTitle(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appTitle = text;
                    titleView.setText(text);
                }
            });
        }

        @JavascriptInterface
        public void setNavigatorBarColor(final String color) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    window.addFlags(WindowManager.LayoutParams
                            .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    if (color.equals("black")) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }

                    window.setStatusBarColor(Color.parseColor(color));
                    navigatorView.setBackgroundColor(Color.parseColor(color));

                    if (!color.equals("white")) {
                        GradientDrawable gd = new GradientDrawable();
                        gd.setColor(Color.parseColor("#aaa5a5a5"));
                        gd.setCornerRadius(rightView.getHeight());
                        gd.setStroke(1, Color.parseColor("white"));
                        rightView.setBackground(gd);

                        splView.setBackgroundColor(Color.parseColor("white"));
                        beforeButton.setTextColor(Color.parseColor("white"));
                        afterButton.setTextColor(Color.parseColor("white"));
                        titleView.setTextColor(Color.parseColor("white"));
                    } else {
                        titleView.setTextColor(Color.parseColor("black"));
                    }
                }
            });
        }

        @JavascriptInterface
        public void vibrate() {

            if (vibrator != null) vibrator.vibrate(new long[]{100, 200}, -1);

        }

        @JavascriptInterface
        public void open(final String url) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }

        @JavascriptInterface
        public void fullScreen(final boolean enable) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!enable) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                                (rightView.getWidth(), rightView.getHeight());
                        params.setMargins(0, dip2px(10), dip2px(10), 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_END);
                        rightView.setLayoutParams(params);
                        navigatorView.setVisibility(View.VISIBLE);

                        int flag = window.getAttributes().flags;
                        if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                            if (window.getStatusBarColor() != Color.parseColor("black")) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            } else {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                            }
                            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }


                    } else {
                        GradientDrawable gd = new GradientDrawable();
                        gd.setColor(Color.parseColor("#aaa5a5a5"));
                        gd.setCornerRadius(rightView.getHeight());
                        gd.setStroke(1, Color.parseColor("white"));
                        rightView.setBackground(gd);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                                (rightView.getWidth(), rightView.getHeight());
                        params.setMargins(0, dip2px(16), dip2px(10), 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_END);
                        rightView.setLayoutParams(params);

                        beforeButton.setTextColor(Color.parseColor("white"));
                        afterButton.setTextColor(Color.parseColor("white"));
                        splView.setBackgroundColor(Color.parseColor("white"));
                        navigatorView.setVisibility(View.GONE);

                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }

            });

        }

        @JavascriptInterface
        public void setHome(String url) {

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:alert('设置成功');");
                }
            };

            try {
                Properties pro = new Properties();
                pro.setProperty("homeurl", url);
                FileOutputStream fos = getApplicationContext().openFileOutput(
                        "config.properties", Context.MODE_PRIVATE);
                pro.store(fos, "");
            } catch (Exception e) {
                Log.e("SETHOME", e.getMessage());
                run = new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:alert('设置失败');");
                    }
                };
            }

            runOnUiThread(run);

        }

        @JavascriptInterface
        public void startWifi() {
            try {
                wifi.setWifiEnabled(true);
            } catch (Exception e) {
                Log.e("WIFI.START", e.getMessage());
            }
        }

        @JavascriptInterface
        public void stopWifi() {
            try {
                if (wifi.isWifiEnabled()) wifi.setWifiEnabled(false);
            } catch (Exception e) {
                Log.e("WIFI.STOP", e.getMessage());
            }
        }

        @JavascriptInterface
        public void showToast(final String text) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }

        @JavascriptInterface
        public void navigatorBackEnable(final boolean enable) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    if (enable) {
                        params.setMarginStart(0);
                        params.addRule(RelativeLayout.END_OF, R.id.firstButton);
                        titleView.setLayoutParams(params);
                        firstButton.setVisibility(View.VISIBLE);
                    } else {
                        params.setMarginStart(dip2px(15));
                        titleView.setLayoutParams(params);
                        firstButton.setVisibility(View.GONE);
                    }

                }
            });
        }

        @JavascriptInterface
        public void phoneCall(String mobile, boolean action) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse("tel:" + mobile));
                startActivity(intent);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("错误")
                                .setMessage("您无权调用打电话接口")
                                .create()
                                .show();
                    }
                });
            }

        }

        @JavascriptInterface
        public boolean setCopyText(String text) {

            try {
                ClipData mClipData = ClipData.newPlainText("text", text);
                cm.setPrimaryClip(mClipData);
            } catch (Exception e) {
                Log.e("PASTE", e.getMessage());
                return false;
            }

            return true;

        }

        @JavascriptInterface
        public String getCopyText() {
            ClipData abc = cm.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            return item.getText().toString();
        }

        @JavascriptInterface
        public String geoLocation() {

            JSONObject data = new JSONObject();

            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));


                if (location != null) {

                    try {
                        data.put("latitude", location.getLatitude());
                        data.put("longitude", location.getLongitude());
                        data.put("speed", location.getSpeed());
                        data.put("accuracy", location.getAccuracy());
                        data.put("altitude", location.getAltitude());
                    } catch (Exception e) {
                        Log.e("GEOLOCATION", e.getMessage());
                    }

                    return data.toString();
                }


            }

            return data.toString();

        }

        @JavascriptInterface
        public String getWifiList() {

            JSONArray data = new JSONArray();

            if (wifi.isWifiEnabled()) {
                JSONObject item;
                try {
                    WifiInfo info = wifi.getConnectionInfo();
                    List<ScanResult> results = wifi.getScanResults();
                    for (ScanResult next : results) {
                        item = new JSONObject();
                        item.put("BSSID", next.BSSID);
                        item.put("SSID", next.SSID);
                        item.put("level", next.level);
                        data.put(item);
                    }
                } catch (Exception e) {
                    Log.e("WIFI.LIST", e.getMessage());
                }
            }

            return data.toString();
        }

        @JavascriptInterface
        public void sendNotificationText(String text) {

            try {
                Intent intent = new Intent();
                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
                NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("测试")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setFullScreenIntent(pi, true)
                        .setContentText(text);

                notifyManager.notify(1, builder.build());
            } catch (Exception e) {
                Log.e("NOTIFICATION", e.getMessage());
            }

        }

        @JavascriptInterface
        public String getDeviceID() {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String deviceId = tm.getDeviceId();
                    Log.e("DEVICE.ID", "normal mode: " + deviceId);
                    if (deviceId == null) {
                        deviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
                        Log.e("DEVICE.ID", "compatible mode: " + deviceId);
                    }
                    return deviceId;
                } catch (Exception e) {
                    Log.e("DEVICE.ID", e.getMessage());
                }
            }
            return null;
        }

        @JavascriptInterface
        public void startRecord() {

            try {
                String tempFile = getExternalCacheDir().getPath() + "/tmp.amr";
                final MediaRecorder record = new MediaRecorder();
                record.setAudioSource(MediaRecorder.AudioSource.MIC);
                record.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                record.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                record.setOutputFile(tempFile);
                record.prepare();
                record.start();

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("")
                        .setMessage("正在录音")
                        .setCancelable(false)
                        .create();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dialog.show();
                    }

                });

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.hide();
                            }
                        });

                        Log.e("RECORD", "the end");
                        record.stop();
                        record.release();

                    }

                }, 5000);
                Log.e("RECORD.FILEPATH", tempFile);
                Log.e("RECORD", "let's start");
            } catch (Exception e) {
                Log.e("RECORD", e.getMessage());
            }

        }

        @JavascriptInterface
        public void playRecord() {

            try {
                String tempFile = getExternalCacheDir().getPath() + "/tmp.amr";
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(tempFile);
                player.prepare();

                Log.e("PLAYER.DURATION", "length: " + String.format("%.2f", (double) player.getDuration() / 1000));
                player.start();
            } catch (Exception e) {
                Log.e("PLAYER", e.getMessage());
            }

        }

        @JavascriptInterface
        public String getNetworkType() {

            String type = "";
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) return null;

            NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isAvailable()) return null;

            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                type = "WIFI";
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String subTypeName = netInfo.getSubtypeName();
                int networkType = netInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        type = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        type = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        type = "4G";
                        break;
                    default:
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            type = "3G";
                        }
                }
            }

            if (type.isEmpty()) return null;
            return type;
        }

    }

}
