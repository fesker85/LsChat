package cc.lzsou.lschat.core.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.views.dialog.LoadingDialog;

public abstract class HttpGetHandler {
    private Context context;
    private boolean show=false;
    private String msg="加载中";
    private String url;
    private Map<String,String> parameters;

    private LoadingDialog dialog = null;

    public HttpGetHandler(Context context, String url, Map<String,String> parameters){
        this.context=context;
        this.url=url;
        this.parameters=parameters;
        onProcess();
    }
    public HttpGetHandler(Context context, String url, Map<String,String> parameters, boolean show, String msg){
        this.context=context;
        this.show=show;
        this.msg=msg;
        this.url=url;
        this.parameters=parameters;
        onProcess();
    }

    private void onProcess(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null) {
            Toast.makeText(context, context.getString(R.string.net_error), Toast.LENGTH_SHORT).show();
            return;
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                if (show) {
                    dialog = new LoadingDialog.Builder(context).setCancelable(true).setMessage(msg).create();
                    dialog.show();
                }
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return HttpHelper.doGet(url, parameters);
            }

            @Override
            protected void onPostExecute(String s) {
                if (dialog != null) dialog.dismiss();
                onResult(s);
                super.onPostExecute(s);
            }
        }.execute();
    }

    protected  abstract void  onResult(String r);
}
