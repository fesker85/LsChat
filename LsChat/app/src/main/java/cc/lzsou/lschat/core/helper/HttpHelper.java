package cc.lzsou.lschat.core.helper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import cc.lzsou.lschat.base.AppEntity;


public class HttpHelper {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3534.4 Safari/537.36";
    private static final int timeout=10*1000;

    public static String doGet(String url, Map<String, String> parameters) {
        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent(userAgent).data(parameters).timeout(timeout).get();
            if (doc != null) return doc.body().ownText().trim();
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public static String doPost(String url, Map<String, String> parameters) {

        System.out.println("打印POST地址："+url);

        try {
            Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent(userAgent).data(parameters).timeout(timeout).post();
            if (doc != null) return doc.body().ownText().trim();
            return "";
        } catch (IOException e) {
            System.out.println("打印错误结果："+e.getMessage());
            return "";
        }
    }

    public static String doPostFile(String url,String filename,Map<String,String> map){
        Connection connection = Jsoup.connect(url);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);
        connection.timeout(10*1000);
        File file = new File(filename);
        if (!file.exists()) return "";
        try {
            connection.data("file", "file", new FileInputStream(file));
            connection.data(map);
            Document doc= connection.post();
            return doc.body().ownText();
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }


}
