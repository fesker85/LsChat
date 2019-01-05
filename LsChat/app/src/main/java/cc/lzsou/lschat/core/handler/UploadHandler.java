package cc.lzsou.lschat.core.handler;

import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cz.msebera.android.httpclient.Header;

public abstract class UploadHandler extends Thread {

    private String url;
    private Map<String,String> map;
    private String filename;
    public UploadHandler(String url, Map<String,String> map, String filename){
        this.url=url;
        this.map=map;
        this.filename=filename;
    }

    @Override
    public void run() {
        super.run();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return HttpHelper.doPostFile(url,filename,map);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                AjaxResult ajaxResult = new AjaxResult();
                if(s==null||s.equals("")){
                    ajaxResult.setCode("400");
                    ajaxResult.setMsg("上传失败");
                    onResult(ajaxResult);
                }else {
                    ajaxResult = JsonHelper.jsonToObject(s,AjaxResult.class);
                    onResult(ajaxResult);
                }

            }
        }.execute();

//        try {
//            System.out.println("上传结果："+url+"\r\n"+filename);
//            AsyncHttpClient client = new AsyncHttpClient();
//            RequestParams params = new RequestParams();
//            params.put("file",new File(filename));
//            for(Map.Entry<String,String> entry:map.entrySet()){
//                params.put(entry.getKey(),entry.getValue());
//            }
//            client.post(this.url,params,new AsyncHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    System.out.println("上传结果："+new String(responseBody));
//                    onResult(JsonHelper.jsonToObject(new String(responseBody),AjaxResult.class));
//                }
//                @Override
//                public void onProgress(long bytesWritten, long totalSize) {
//                    System.out.println("需要上传："+totalSize+"\r\n已上传："+bytesWritten+"\r\n完成度："+((bytesWritten/totalSize)*100));
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    System.out.println("上传结果："+new String(responseBody));
//                    ajaxResult.setCode("400");
//                    ajaxResult.setMsg("上传失败");
//                    onResult(ajaxResult);
//                }
//            });
//        } catch (FileNotFoundException e) {
//            System.out.println("上传结果："+e.getMessage());
//            ajaxResult.setCode("400");
//            ajaxResult.setMsg("上传失败");
//            onResult(ajaxResult);
//        }
    }

    protected abstract  void onResult(AjaxResult ajaxResult);


}
