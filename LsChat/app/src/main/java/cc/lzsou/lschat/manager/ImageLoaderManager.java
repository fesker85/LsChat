package cc.lzsou.lschat.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

import cc.lzsou.lschat.R;

public class ImageLoaderManager {

    public static ImageLoaderManager instance;

    public static ImageLoaderManager getInstance() {
        if (instance == null) instance = new ImageLoaderManager();
        return instance;
    }

    public void displayCircleAvatar(String imageUri,ImageView imageView){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_avatar)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_avatar)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_avatar)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .displayer(new RoundedBitmapDisplayer(200))//设置圆角
                .build();// 构建完成
        ImageLoader.getInstance().displayImage(imageUri, imageView,options);
    }
    public void displayAvatar(String imageUri,ImageView imageView){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_avatar)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_avatar)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_avatar)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                //.displayer(new RoundedBitmapDisplayer(20))//设置圆角
                .build();// 构建完成
        ImageLoader.getInstance().displayImage(imageUri, imageView,options);
    }

    public Bitmap loadAvatar(String imageUri){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_avatar)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_avatar)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_avatar)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                //.displayer(new RoundedBitmapDisplayer(20))//设置圆角
                .build();// 构建完成
       return ImageLoader.getInstance().loadImageSync(imageUri,new ImageSize(56,56),options);
    }

    public void displayFromBase64String(String base64String,String path,ImageView imageView){
        if((new File(path)).exists())displayFromSdcard(path,imageView);
        else displayFromBytes(FileManager.getInstance().base64StringToBytes(base64String),path,imageView);
    }
    public void displayFromBytes(byte[] data,String path,ImageView imageView){
        if(data==null)displayFromSdcard(path,imageView);
        if(FileManager.getInstance().saveFile(data,path))
            displayFromSdcard(path,imageView);
        else displayFromSdcard(path,imageView);
    }

    public void displayFromNetwork(String imageUri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage(imageUri, imageView,cricleDisplayOptions());
    }

    public void displayFromSdcard(String imageUri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        ImageLoader.getInstance().displayImage("file://" + imageUri, imageView,cricleDisplayOptions());
        //ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imageUri), imageView, cricleDisplayOptions());
    }

    public void dispalyFromAssets(String imageUri, ImageView imageView) {
        // String imageUri = "assets://image.png"; // from assets
        ImageLoader.getInstance().displayImage("assets://" + imageUri, imageView, cricleDisplayOptions());
    }

    public void displayFromDrawable(int imageUri, ImageView imageView) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        ImageLoader.getInstance().displayImage("drawable://" + imageUri, imageView, cricleDisplayOptions());
    }

    public void displayFromContent(String imageUri, ImageView imageView) {
        // String imageUri = "content://media/external/audio/albumart/13"; //
        // from content provider
        ImageLoader.getInstance().displayImage("content://" + imageUri, imageView, cricleDisplayOptions());
    }

    private DisplayImageOptions cricleDisplayOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.mipmap.dialog_loading_img)// 设置图片下载期间显示的图片
               // .showImageForEmptyUri(R.mipmap.icon_image_error)// 设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.mipmap.icon_image_error)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .build();// 构建完成
        return options;
    }



}
