package cc.lzsou.lschat.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cc.lzsou.lschat.manager.FileManager;

public class BaseApplication extends Application {
    private static BaseApplication instance;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;
    /**
     * 聊天语音最小宽度
     */
    public static int minChatVoiceItemWidth;
    /**
     * 聊天语音最大宽度
     */
    public static int maxChatVoiceItemWidth;

    @Override
    public void onCreate() {
        super.onCreate();
        instance =this;
        initImageLoader(getApplicationContext());
        initScreenSize();
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
        minChatVoiceItemWidth=(int) (screenWidth*0.1f);
        maxChatVoiceItemWidth=(int)(screenWidth*0.6f);
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public static void  initImageLoader(Context context) {
        //缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, FileManager.getInstance().getImageCachePath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480,800)// max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2* 1024* 1024))// You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2* 1024* 1024)// 内存缓存的最大值
                .diskCacheSize(50* 1024* 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .imageDownloader(new BaseImageDownloader(context, 5* 1000,30* 1000))// connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()// Remove for release app
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
