package cc.lzsou.webbrowser.client;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/6.
 */

public class SimpleChromeWebViewClient extends WebChromeClient {
    private ProgressBar progressBar;
    private TextView textView;
    private Context context;
    public SimpleChromeWebViewClient(ProgressBar progressBar, TextView textView, Context context){
        this.progressBar = progressBar;
        this.progressBar.setVisibility(View.VISIBLE);
        this.textView = textView;
        this.context=context;
    }
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if(progressBar!=null){
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if(textView!=null)textView.setText(title);
        super.onReceivedTitle(view, title);
    }
}
