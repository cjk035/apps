package tech.haowei.apps;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

public class MainActivity extends AppCompatActivity {

    public WebView webView;
    public TextView titleView;
    public TextView beforeButton;
    public TextView afterButton;
    public RelativeLayout rightView;
    public RelativeLayout navigatorView;
    public Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        titleView = (TextView) findViewById(R.id.navigatorTitle);
        navigatorView = (RelativeLayout) findViewById(R.id.navigatorBar);
        rightView = (RelativeLayout) findViewById(R.id.rightSide);

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View
                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.addFlags(WindowManager.LayoutParams
                .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));


        beforeButton = (TextView) findViewById(R.id.beforeButton);
        afterButton = (TextView) findViewById(R.id.afterButton);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "statics/font/apps.ttf");
        beforeButton.setTypeface(iconfont);
        afterButton.setTypeface(iconfont);


        beforeButton.setLongClickable(true);
        beforeButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                webView.reload();
                try {
                    vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{100, 200}, -1);
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
        webView.loadUrl("https://haowei.asia");

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
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams
                            .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                    window.setStatusBarColor(Color.parseColor(color));
                    navigatorView.setBackgroundColor(Color.parseColor(color));

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.parseColor("#aaaaaaaa"));
                    gd.setCornerRadius(rightView.getHeight());
                    gd.setStroke(1, Color.parseColor("white"));
                    rightView.setBackground(gd);

                    beforeButton.setTextColor(Color.parseColor("white"));
                    afterButton.setTextColor(Color.parseColor("white"));
                    titleView.setTextColor(Color.parseColor("white"));
                }
            });
        }

        @JavascriptInterface
        public void vibrate() {

            vibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{100, 200}, -1);

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

    }

}
