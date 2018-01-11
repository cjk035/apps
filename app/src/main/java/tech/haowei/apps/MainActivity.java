package tech.haowei.apps;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public TextView titleView;
    public TextView beforeButton;
    public TextView afterButton;
    public View splView;
    public RelativeLayout rightView;
    public RelativeLayout navigatorView;
    public Vibrator vibrator;
    public Window window;
    public WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        window = getWindow();
        window.getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams
                .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));


        splView = (View) findViewById(R.id.spl);
        titleView = (TextView) findViewById(R.id.navigatorTitle);
        navigatorView = (RelativeLayout) findViewById(R.id.navigatorBar);
        rightView = (RelativeLayout) findViewById(R.id.rightSide);
        rightView.bringToFront();

        beforeButton = (TextView) findViewById(R.id.beforeButton);
        afterButton = (TextView) findViewById(R.id.afterButton);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "statics/font/apps.ttf");
        beforeButton.setTypeface(iconfont);
        afterButton.setTypeface(iconfont);

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
                return false;
            }

        });


        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setDefaultTextEncodingName("UTF -8");
        webView.addJavascriptInterface(new JavaScript(), "apps");

        try {

            Properties pro = new Properties();
            FileInputStream fis = getApplicationContext().openFileInput
                    ("config.properties");
            pro.load(fis);
            String url = pro.getProperty("homeurl");
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
                    webView.loadUrl("javascript:window.dispatchEvent(new Event('ready'))");
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

    public int dip2px(int dip) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (scale * dip + 0.5f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class JavaScript {

        @JavascriptInterface
        public void setNavigatorTitle(final String text) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.parseColor("#aaa5a5a5"));
                    gd.setCornerRadius(rightView.getHeight());
                    gd.setStroke(1, Color.parseColor("white"));
                    rightView.setBackground(gd);

                    splView.setBackgroundColor(Color.parseColor("white"));
                    beforeButton.setTextColor(Color.parseColor("white"));
                    afterButton.setTextColor(Color.parseColor("white"));
                    if (!color.equals("white")) {
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
        public void load(final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(url);
                }
            });
        }

        @JavascriptInterface
        public void fullscreen(final boolean off) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (!off) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                                (rightView.getWidth(), rightView.getHeight());
                        params.setMargins(0, dip2px(10), dip2px(10), 0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_END);
                        rightView.setLayoutParams(params);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        navigatorView.setVisibility(View.VISIBLE);
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

                        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        navigatorView.setVisibility(View.GONE);
                    }
                }

            });

        }

        @JavascriptInterface
        public void sethome(String url) {

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
            }catch (Exception e) {
                Log.e("WIFI.START", e.getMessage());
            }
        }

        @JavascriptInterface
        public void stopWifi() {
            try {
                if (wifi.isWifiEnabled()) wifi.setWifiEnabled(false);
            }catch (Exception e) {
                Log.e("WIFI.STOP", e.getMessage());
            }
        }

        @JavascriptInterface
        public void getWifiList() {
            /*try {
                if (wifi.isWifiEnabled()) {
                    wifi.getScanResults();

                }
            }catch (Exception e) {
                Log.e("WIFI.LIST", e.getMessage());
            }*/
        }

    }

}
