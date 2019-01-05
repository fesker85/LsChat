package cc.lzsou.lschat.manager;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * 视频管理器
 */
public class VideoManager {
    /**
     * 获取视频缩略图
     * @param path
     * @return
     */
    public static Bitmap createThumbnail(String path) {
        Bitmap b=null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            b=retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 获取视频时长
     * @param path
     * @return
     */
    public static long getDuration(String path){

        MediaPlayer meidaPlayer = new MediaPlayer();
        try {
            meidaPlayer.setDataSource(path);
            meidaPlayer.prepare();
            return meidaPlayer.getDuration();
        } catch (IOException e) {
           return  0;
        }
    }
}
