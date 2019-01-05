package cc.lzsou.media.utils;

import android.app.Activity;
import android.net.Uri;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

import cc.lzsou.media.entity.Media;
import cc.lzsou.selectorlibrary.R;

//截屏
public class CropUtils {
    //开始截图
    public static void startCrop(Media media, Activity activity){
        File file = new File(FileHelper.getInstance(activity).getCropPath(), "crop.jpg");
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        options.setHideBottomControls(true);
        UCrop.of(Uri.fromFile(new File(media.path)), Uri.fromFile(file)).withAspectRatio(1, 1).withMaxResultSize(300, 300).withOptions(options).start(activity);
    }
}
