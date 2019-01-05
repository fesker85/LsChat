package cc.lzsou.webbrowser.scripts;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebSctiptBase {

    private Context context;
    public WebSctiptBase(Context context){
        this.context=context;
    }
    private static WebSctiptBase instance;
    public static WebSctiptBase getInstance(Context context){
        if(instance==null)
            instance=new WebSctiptBase(context);
        return instance;
    }

    @JavascriptInterface
    public void showToast(String msg){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }


}
