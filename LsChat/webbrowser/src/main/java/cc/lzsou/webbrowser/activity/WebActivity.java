package cc.lzsou.webbrowser.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import cc.lzsou.pushtorefresh.PullToRefreshBase;
import cc.lzsou.pushtorefresh.PullToRefreshWebView;
import cc.lzsou.webbrowser.R;
import cc.lzsou.webbrowser.client.SimpleChromeWebViewClient;
import cc.lzsou.webbrowser.client.SimpleWebViewClient;
import cc.lzsou.webbrowser.scripts.WebSctiptBase;

public class WebActivity extends AppCompatActivity implements PullToRefreshBase.OnRefreshListener<WebView>, View.OnClickListener {

    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_URL = "url";
    public static final String INTENT_KEY_CACHE="cache";
    public static final String INTENT_KEY_UA="ua";
    public static final String INTENT_KEY_VERSION="version";

    private ImageButton backButton;
    private TextView titleView;
    private ProgressBar progressBar;
    private PullToRefreshWebView pullToRefreshWebView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        init();
    }

    private void init() {
        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        titleView = (TextView) findViewById(R.id.titleView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webView);
        pullToRefreshWebView.setOnRefreshListener(this);
        //pullToRefreshWebView.setMode(PullToRefreshBase.Mode.DISABLED);//禁止拖拽
        webView = pullToRefreshWebView.getRefreshableView();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new SimpleWebViewClient());
        webView.setWebChromeClient(new SimpleChromeWebViewClient(progressBar, null, WebActivity.this));
        webView.addJavascriptInterface(new WebSctiptBase(WebActivity.this), "LSWeb");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.densityDpi > 240) {
            webView.getSettings().setDefaultFontSize(40); //可以取1-72之间的任意值，默认16
        }
        initUI();
    }

    private void initUI() {
        String title = getIntent().getStringExtra(INTENT_KEY_TITLE);
        String url = getIntent().getStringExtra(INTENT_KEY_URL);
        boolean cache = getIntent().getBooleanExtra(INTENT_KEY_CACHE,true);
        if (title == null || title.equals("")) title = getString(R.string.app_name);
        titleView.setText(title);
        if (url == null || url.equals("")) url = "http://school.sntianyuan.com";
        webView.getSettings().setAppCacheEnabled(cache);
        String ua = webView.getSettings().getUserAgentString();
        String id = getIntent().getStringExtra(INTENT_KEY_UA);
        String version = getIntent().getStringExtra(INTENT_KEY_VERSION);
        webView.getSettings().setUserAgentString(ua+" ;[os:android][version:"+version+"][uuid:"+id+"]}");
        webView.loadUrl(url);
    }

    @Override
    public void onRefresh(final PullToRefreshBase<WebView> refreshView) {
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                refreshView.onRefreshComplete();
            }
        }, 10);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack())webView.goBack();
            else finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
