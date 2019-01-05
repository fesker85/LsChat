package cc.lzsou.lschat.core.handler;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cz.msebera.android.httpclient.Header;

public abstract class DownLoadHandler {

    private String url;
    private Map<String, String> map;
    private String filename;

    public DownLoadHandler(String url, Map<String, String> map, String filename) {

         this.url = url;
        this.map = map;
        this.filename = filename;
    }
    public void run() {
        AjaxResult ajaxResult = new AjaxResult();
        String filePath = this.filename.substring(0, this.filename.lastIndexOf("/"));
        File file = new File(filePath);
        if (!file.exists()) file.mkdirs();
        Connection connection = Jsoup.connect(this.url);
        connection.ignoreContentType(true);
        connection.ignoreHttpErrors(true);
        connection.timeout(20*1000);
        try {
            Connection.Response response= connection.execute();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(response.bodyAsBytes());
            fos.close();
            ajaxResult.setCode("200");
            ajaxResult.setMsg("成功");
        } catch (IOException e) {
            ajaxResult.setCode("400");
            ajaxResult.setMsg("失败");
        }
        onResult(ajaxResult);

    }

    protected abstract void onResult(AjaxResult ajaxResult);
}
