package cc.lzsou.media.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

public class FileHelper {

    private Context context;
    public FileHelper(Context context){
        this.context=context;
    }
    private static FileHelper instance;
    public  static FileHelper getInstance(Context context){
        if(instance==null)instance = new FileHelper(context);
        return instance;
    }

    private boolean hasSDCard() {
        boolean b = false;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            b = true;
        }
        return b;
    }

    private String getExtPath() {
        String path = "";
        if (hasSDCard()) {
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            File file = context.getDir("Cache", Context.MODE_PRIVATE);
            if (!file.exists()) file.mkdirs();
            path = file.getPath();
        }
        return path;
    }

    public void remove(String path){
        File file = new File(path);
        if(file.exists())file.delete();
    }


    //图片存放地址
    public String getImagePath() {
        String extPath = getExtPath();
        if (extPath == null || extPath.equals("")) return "";
        File file = new File(extPath + "/LsChat/Camera");
        if (!file.exists()) file.mkdirs();
        return file.getPath();
    }
    public String getId(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    //获取裁剪路径
    public String getCropPath() {
        String extPath = getExtPath();
        if (extPath == null || extPath.equals("")) return "";
        File file = new File(extPath + "/LsChat/Crop");
        if (!file.exists()) file.mkdirs();
        return file.getPath();
    }
    //获取图片地址
    public String getVideoPath(){
        String extPath = getExtPath();
        if (extPath == null || extPath.equals("")) return "";
        File file = new File(extPath + "/LsChat/Video");
        if (!file.exists()) file.mkdirs();
        return file.getPath();
    }
}
