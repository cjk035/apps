package tech.haowei.apps;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    public String colorPrimary = "#FFFFFF";
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
        window.setStatusBarColor(Color.parseColor(colorPrimary));
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
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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

                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                        .MATCH_PARENT));
                layout.setOrientation(LinearLayout.VERTICAL);


                String[] list = new String[]{"分享","投诉","添加到桌面","关于%s"};

                for (String item : list) {
                    TextView text = new TextView(MainActivity.this);

                    text.setText(String.format(item, appTitle));
                    text.setTextColor(Color.parseColor("black"));
                    text.setTextSize(17);
                    text.setLayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout
                                    .LayoutParams.MATCH_PARENT, dip2px(44)));
                    text.setGravity(Gravity.CENTER);
                    text.setPadding(-2, -2, -2, 0);
                    text.setBackgroundResource(R.drawable.selector_menu_item);
                    text.setClickable(true);
                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    layout.addView(text);
                }

                new AlertDialog.Builder(MainActivity.this)
                        .setView(layout)
                        .create().show();


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
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.addJavascriptInterface(new JavaScript(), "apps");

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url != null) {
            Log.e("INTENT", url);
            boolean cross = intent.getBooleanExtra("cross", false);
            if (!cross) loadView.setVisibility(View.GONE);
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

            try {
                ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context
                        .CONNECTIVITY_SERVICE);
                connectivity.requestNetwork(new NetworkRequest.Builder().build(), new
                        ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        //onNetChange(true);
                        super.onAvailable(network);
                    }

                    @Override
                    public void onLost(Network network) {
                        onNetChange(false);
                        super.onLost(network);
                    }

                });
            } catch (Exception e) {
                Log.e("NETCHANGE", e.getMessage());
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



                Log.e("WEBVIEW.FINISHED", "URL: " + url);


            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

            private boolean done = false;

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (done && newProgress < 75) {
                    done = false;
                } else if (!done && newProgress >= 75) {
                    done = true;
                    Log.e("LOCAL.SERVICE", "JavaScript Service Bridge Successful");
                    ///webView.loadUrl("javascript:window.dispatchEvent(new Event('ready'))");

                    webView.getSettings().setBlockNetworkImage(false);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            loadView.setVisibility(View.GONE);
                        }

                    });

                }
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("WEBVIEW.CONSOLE", consoleMessage.message());
                Log.e("WEBVIEW.CONSOLE", "LINE: " + consoleMessage.lineNumber());
                return true;
            }

        });

    }

    @Override
    protected void onPause() {
        Log.e("ACTIVITY.PAUSE", "webView JavaScript disabled");
        webView.getSettings().setJavaScriptEnabled(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.e("ACTIVITY.PAUSE", "webView JavaScript enabled");
        webView.getSettings().setJavaScriptEnabled(true);
        super.onResume();
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

    public void onNetChange(final boolean available) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String message = "";
                if (available) {
                    message = "网络已恢复";
                    webView.reload();
                } else {
                    message = "网络已断开";
                }
                webView.loadUrl("javascript:alert('" + message + "');");
            }
        });

        Log.e("NET.CHANGE.EVENT", "initialize");
    }

    public boolean isDarkColor(String color) {
        int colors[] = new int[3];
        colors[0] = (int) Integer.parseInt(color.substring(1, 3), 16);
        colors[1] = (int) Integer.parseInt(color.substring(3, 5), 16);
        colors[2] = (int) Integer.parseInt(color.substring(5), 16);
        int grayLevel = (int) (colors[0] * 0.299 + colors[1] * 0.587 + colors[2] * 0.114);
        Log.e("GRAY", String.format("grayLevel: %d", grayLevel));
        return grayLevel <= 192;
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
        public void setNavigatorBarColor(String color_src) {
            if (!color_src.startsWith("#")) color_src = "#ffffff";
            final String color = color_src;

            Log.e("COLOR.PARSE", "INT: " + Color.parseColor(color));
            colorPrimary = color;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    // View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                    window.addFlags(WindowManager.LayoutParams
                            .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor(color));
                    navigatorView.setBackgroundColor(Color.parseColor(color));

                    if (isDarkColor(color)) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

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
                        window.getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        titleView.setTextColor(Color.parseColor("black"));
                    }

                    Log.e("COLOR.SUBSTRING", color.substring(1, 3));
                    Log.e("COLOR.SUBSTRING", color.substring(3, 5));
                    Log.e("COLOR.SUBSTRING", color.substring(5));

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
            intent.putExtra("cross", false);
            startActivity(intent);
        }

        @JavascriptInterface
        public void fullScreen(final boolean enable) {

            Log.e("FULLSCREEN", "value: " + (enable ? "true" : "false"));
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (enable) {
                        window.getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
                        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                        navigatorView.setVisibility(View.GONE);

                        GradientDrawable gd = new GradientDrawable();
                        gd.setColor(Color.parseColor("#aaa5a5a5"));
                        gd.setCornerRadius(rightView.getHeight());
                        gd.setStroke(1, Color.parseColor("white"));
                        rightView.setBackground(gd);

                        beforeButton.setTextColor(Color.parseColor("white"));
                        afterButton.setTextColor(Color.parseColor("white"));
                        splView.setBackgroundColor(Color.parseColor("white"));


                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                        navigatorView.setVisibility(View.VISIBLE);

                        if (isDarkColor(colorPrimary)) {
                            window.getDecorView().setSystemUiVisibility(View
                                    .SYSTEM_UI_FLAG_VISIBLE);
                        } else {
                            window.getDecorView().setSystemUiVisibility(View
                                    .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        }

                    }

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                            (rightView.getWidth(), rightView.getHeight());
                    params.setMargins(0, dip2px(10), dip2px(10), 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    rightView.setLayoutParams(params);

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
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout
                                    .LayoutParams.MATCH_PARENT);
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

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission
                    .CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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

            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission
                    (MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission
                            (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                LocationManager locationManager = (LocationManager) getSystemService(Context
                        .LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, true));


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

                NotificationManager notifyManager = (NotificationManager) getSystemService
                        (Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity
                        .this, "")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("测试")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentText(text);

                notifyManager.notify(1, builder.build());
            } catch (Exception e) {
                Log.e("NOTIFICATION", e.getMessage());
            }

        }

        @JavascriptInterface
        public String getDeviceID() {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission
                    (MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                try {
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context
                            .TELEPHONY_SERVICE);
                    String deviceId = tm.getDeviceId();
                    Log.e("DEVICE.ID", "normal mode: " + deviceId);
                    if (deviceId == null) {
                        deviceId = Settings.System.getString(getContentResolver(), Settings
                                .System.ANDROID_ID);
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

                Log.e("PLAYER.DURATION", "length: " + String.format("%.2f", (double) player
                        .getDuration() / 1000));
                player.start();
            } catch (Exception e) {
                Log.e("PLAYER", e.getMessage());
            }

        }

        @JavascriptInterface
        public String getNetworkType() {

            String type = "";
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context
                    .CONNECTIVITY_SERVICE);
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
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName
                                .equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase
                                ("CDMA2000")) {
                            type = "3G";
                        }
                }
            }

            if (type.isEmpty()) return null;
            return type;
        }

        @JavascriptInterface
        public void layoutFullscreen() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    window.getDecorView().setSystemUiVisibility(View
                            .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                            .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View
                            .SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    window.addFlags(WindowManager.LayoutParams
                            .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);


                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                            (rightView.getWidth(), rightView.getHeight());
                    params.setMargins(0, dip2px(28), dip2px(10), 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    rightView.setLayoutParams(params);

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.parseColor("#aaa5a5a5"));
                    gd.setCornerRadius(rightView.getHeight());
                    gd.setStroke(1, Color.parseColor("white"));
                    rightView.setBackground(gd);


                    beforeButton.setTextColor(Color.parseColor("white"));
                    afterButton.setTextColor(Color.parseColor("white"));
                    splView.setBackgroundColor(Color.parseColor("white"));

                    navigatorView.setVisibility(View.GONE);

                    if (isDarkColor(colorPrimary)) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }

                }

            });

        }

        @JavascriptInterface
        public void setColorPrimary(String color) {
            if (!color.startsWith("#")) color = "#ffffff";
            colorPrimary = color;
        }

    }

}
