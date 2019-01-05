package cc.lzsou.media.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cc.lzsou.media.utils.FileHelper;

//摄像头管理
public class CameraView extends SurfaceView implements MediaRecorder.OnErrorListener, SurfaceHolder.Callback {

    private SurfaceHolder mSurfaceHolder;
    private int mWidthPixel, mHeightPixel;//SurfaceView的宽高
    private int maxDuration=10;//最大录制时长,单位秒
    private Camera mCamera;//相机
    private File mRecordFile;//录制的视频文件
    private File mCaptureFile;//拍照保存的文件
    private Camera.Parameters mCaptureParameters;//相机配置，在录像前记录，用以录像结束后恢复原配置
    private MediaRecorder mMediaRecorder;
    private FlashMode mFlashMode = FlashMode.AUTO;//闪光灯模式
    private int mOrientation;//当前屏幕旋转角度
    private int mCurrCameraFacing;//当前设置头类型,0:后置/1:前置
    private boolean enableCountDown;//是否启用倒计时录制视频
    private CountDownTimer mCountDownTimer;
    private boolean isRecording;//是否正在录制
    private OnCameraListener listener;//视频录制 或 拍照回调
    private FileHelper fileHelper;//文件帮助类

    public void addOnCameraListener(OnCameraListener listener) {
        this.listener = listener;
    }

    public interface OnCameraListener {
        void onRecordFinish(String path);

        void onRecordProgress(int total, int current);

        //void onCaptureFinish(String path);
        void onCaptureFinish(byte[] data);
    }

    public void setEnableCountDown(boolean b) {
        enableCountDown = b;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                openCamera();
            }
            if (null != mCamera) {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "打开相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidthPixel = width;
        mHeightPixel = height;
        setCameraParameters();
        updateCameraOrientation();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseRecord();
        releaseCamera();
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        if (null != mCamera) {
            releaseCamera();
        }
        try {
            if (!checkCameraFacing(0) && !checkCameraFacing(1)) {
                Toast.makeText(getContext(), "未发现有可用摄像头", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!checkCameraFacing(mCurrCameraFacing)) {
                Toast.makeText(getContext(), mCurrCameraFacing == 0 ? "后置摄像头不可用" : "前置摄像头不可用", Toast.LENGTH_SHORT).show();
                return;
            }
            mCamera = Camera.open(mCurrCameraFacing);
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
    }

    /**
     * 设置相机参数
     */
    private void setCameraParameters() {
        if (null != mCamera) {
            Camera.Parameters params = mCamera.getParameters();
            Camera.Size preViewSize = getOptimalSize(params.getSupportedPreviewSizes(), mWidthPixel, mHeightPixel);
            if (null != preViewSize) {
                if (preViewSize.width > 1920) params.setPictureSize(1920, 1080);
                else params.setPreviewSize(preViewSize.width, preViewSize.height);
            }

            Camera.Size pictureSize = getOptimalSize(params.getSupportedPictureSizes(), mWidthPixel, mHeightPixel);
            if (null != pictureSize) {
                if (pictureSize.width > 1920) params.setPictureSize(1920, 1080);
                else params.setPictureSize(pictureSize.width, pictureSize.height);
            }
            //设置图片格式
            params.setPictureFormat(ImageFormat.JPEG);
            params.setJpegQuality(100);
            params.setJpegThumbnailQuality(50);

//            List<String> modes = params.getSupportedFocusModes();
//            if (modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//                //支持自动聚焦模式
//                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//            }
            params.setFocusMode(Camera.Parameters.ANTIBANDING_AUTO);
            //设置闪光灯模式
            setFlashMode(mFlashMode);
            mCamera.setParameters(params);
            //开启屏幕朝向监听
            startOrientationChangeListener();
        }

    }


    /**
     * 初始化摄像
     */
    private void initRecord() {
        try {
            if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
            else mMediaRecorder.reset();
            mCaptureParameters = mCamera.getParameters();
            setFlashMode(FlashMode.OFF);
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setOnErrorListener(this);
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//音频源
            mMediaRecorder.setAudioChannels(1);//单声道
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//视频输出格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//音频格式
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//视频录制格式
            mMediaRecorder.setVideoSize(1280, 720);//设置分辨率,320, 240微信小视频的像素一样
            //mMediaRecorder.setVideoFrameRate(17);// 设置每秒帧数 这个设置三星手机会出问题
            mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);//清晰度
            mMediaRecorder.setOrientationHint(90);//输出旋转90度，保持竖屏录制

            if (fileHelper == null) fileHelper = FileHelper.getInstance(getContext());
            mRecordFile = new File(fileHelper.getVideoPath() + "/" + fileHelper.getId() + ".mp4");
            if (!mRecordFile.exists()) mRecordFile.getParentFile().mkdirs();

            //mMediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);

            mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
    }

    /**
     * 设置闪关灯模式
     *
     * @param flashMode
     */
    public void setFlashMode(FlashMode flashMode) {
        if (mCamera != null && null != mCamera.getParameters()) {
            mFlashMode = flashMode;
            Camera.Parameters parameters = mCamera.getParameters();
            switch (flashMode) {
                case ON:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                    break;
                case AUTO:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    break;
                case TORCH:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    break;
                default:
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    break;
            }
            mCamera.setParameters(parameters);
        }
    }

    public FlashMode getFlashMode() {
        return mFlashMode;
    }

    /**
     * 切换闪关灯
     *
     * @return 返回当前闪关灯状态
     */
    public FlashMode switchFlashMode() {
        if (getFlashMode() == FlashMode.ON) {
            setFlashMode(FlashMode.OFF);
            return FlashMode.OFF;
        } else if (getFlashMode() == FlashMode.OFF) {
            setFlashMode(FlashMode.AUTO);
            return FlashMode.AUTO;
        } else if (getFlashMode() == FlashMode.AUTO) {
            setFlashMode(FlashMode.TORCH);
            return FlashMode.TORCH;
        } else if (getFlashMode() == FlashMode.TORCH) {
            setFlashMode(FlashMode.ON);
            return FlashMode.ON;
        }

        return null;
    }

    /**
     * 启动屏幕朝向改变监听函数 用于在屏幕横竖屏切换时改变保存的图片的方向
     */
    private void startOrientationChangeListener() {
        OrientationEventListener orientationEventListener = new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int rotation) {

                if ((rotation >= 0 && rotation <= 45) || rotation > 315) {
                    rotation = 0;
                } else if (rotation > 45 && rotation <= 135) {
                    rotation = 90;
                } else if (rotation > 135 && rotation <= 225) {
                    rotation = 180;
                } else if (rotation > 225 && rotation <= 315) {
                    rotation = 270;
                } else {
                    rotation = 0;
                }
                if (rotation == mOrientation) return;
                mOrientation = rotation;
                updateCameraOrientation();
            }
        };
        orientationEventListener.enable();
    }

    /**
     * 根据当前朝向修改保存图片的旋转角度
     */
    private void updateCameraOrientation() {
        try {
            if (mCamera != null && null != mCamera.getParameters()) {
                Camera.Parameters parameters = mCamera.getParameters();
                //rotation参数为 0、90、180、270。水平方向为0。
                int rotation = 90 + mOrientation == 360 ? 0 : 90 + mOrientation;
                //前置摄像头需要对垂直方向做变换，否则照片是颠倒的
                if (mCurrCameraFacing == 1) {
                    if (rotation == 90) rotation = 270;
                    else if (rotation == 270) rotation = 90;
                }
                parameters.setRotation(rotation);//生成的图片转90°
                //预览图片旋转90°
                mCamera.setDisplayOrientation(90);//预览转90°
                mCamera.setParameters(parameters);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最优尺寸
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;
        if (w > h) targetRatio = (double) w / h;
        if (sizes == null) return null;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (size.height >= size.width) ratio = (float) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }


    /**
     * 检查是否有摄像头
     *
     * @param facing 前置还是后置
     * @return
     */
    private boolean checkCameraFacing(int facing) {
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        if (mCurrCameraFacing == 0) {
            mCurrCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCurrCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        if (null != mMediaRecorder && isRecording) stopRecord();

        openCamera();
        if (mCamera != null) {
            setCameraParameters();
            updateCameraOrientation();
            try {
                mCamera.setPreviewDisplay(getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 停止录制
     */
    public void stopRecord() {
        endTimeCount();
        releaseRecord();
        if (isRecording && null != listener && null != mRecordFile && !TextUtils.isEmpty(mRecordFile.getPath())) {
            listener.onRecordFinish(mRecordFile.getPath());
        }
        isRecording = false;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        if (mCamera == null) openCamera();
        if (mCamera == null) {
            return;
        }
        mCamera.autoFocus(null);
        initRecord();
        if (null != mMediaRecorder) {
            mMediaRecorder.start();//开始录制
            enableCountDown = true;
            isRecording = true;
            if (enableCountDown) {
                startTimeCount();
            }
        }
    }

    /**
     * 开始拍照
     */
    public void startCapture() {
        if (null != mCamera) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.takePicture(null, null, pictureCallback);
                }
            });
        }
    }

    private final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                if (listener!=null)listener.onCaptureFinish(data);
//                System.out.println("开始拍照时间：" + System.currentTimeMillis());
//                //解析生成相机返回的图片
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                //生成缩略图
//                // Bitmap thumbnail= ThumbnailUtils.extractThumbnail(bm, 213, 213);
//                try {
//                    if (fileHelper == null)
//                        fileHelper = FileHelper.getInstance(getContext());
//                    mCaptureFile = new File(fileHelper.getImagePath() + "/" + fileHelper.getId() + ".jpg");
//                    if (!mCaptureFile.exists()) mCaptureFile.getParentFile().mkdirs();
//                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mCaptureFile));
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                    bos.flush();
//                    bos.close();
//                    if (listener != null) listener.onCaptureFinish(mCaptureFile.getPath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "保存相片失败", Toast.LENGTH_SHORT).show();
//                } finally {
//                    saveRotatePicture(mCaptureFile.getPath());
//                    if (bitmap != null && !bitmap.isRecycled()) {
//                        bitmap.recycle();
//                        bitmap = null;
//                    }
//                }

            } else {
                Toast.makeText(getContext(), "拍照失败，请重试", Toast.LENGTH_SHORT).show();
            }
            //重新打开预览图，进行下一次的拍照准备
            camera.startPreview();
        }
    };


    public Bitmap zoomImage(Bitmap bitmap, float percent) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float newWidth = width * percent / 100;
        float newHeight = height * percent / 100;
        // 计算缩放比例
        float sw = newWidth / width;
        float sh = newHeight / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(sw, sh);
        // 得到新的图片
        Bitmap map = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return map;
    }

    private void saveRotatePicture(String path) {
        if (TextUtils.isEmpty(path)) return;
        int degree = getBitmapDegree(path);
        rotateBitmapByDegree(BitmapFactory.decodeFile(path), degree);
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     */
    private void rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap newBitmap = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            newBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            File rotateFile = new File(mCaptureFile.getPath().replace(".jpg", "_rotate.jpg"));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(rotateFile));
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (newBitmap == null) {
                newBitmap = bm;
            }
            if (bm != newBitmap) {
                bm.recycle();
            }
        }
    }


    /**
     * 开始计时
     */
    private void startTimeCount() {
        endTimeCount();
        mCountDownTimer = new CountDownTimer(maxDuration * 1000l + 1000l, 1000l) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (listener != null) {
                    listener.onRecordProgress(maxDuration, (int) (maxDuration - millisUntilFinished / 1000l));
                }
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onRecordProgress(maxDuration, maxDuration);
                }
                stopRecord();

            }
        }.start();
    }

    /**
     * 结束倒计时
     */
    private void endTimeCount() {
        if (null != mCountDownTimer) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * 释放录像资源
     */
    private void releaseRecord() {
        try {
            if (null != mMediaRecorder) {
                mMediaRecorder.setOnErrorListener(null);
                if (isRecording) mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                //恢复相机参数
                if (mCaptureParameters != null && mCamera != null) {
                    //重新连接相机
                    mCamera.reconnect();
                    //停止预览，注意这里必须先调用停止预览再设置参数才有效
                    mCamera.stopPreview();
                    //设置参数为录像前的参数，不然如果录像是低配，结束录制后预览效果还是低配画面
                    mCamera.setParameters(mCaptureParameters);
                    //重新打开
                    mCamera.startPreview();
                    mCaptureParameters = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (null != mCamera) {
            try {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null) mr.reset();
            isRecording = false;
            Toast.makeText(getContext(), "视频录制出错,请重试", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回录像文件
     *
     * @return
     */
    public File getRecordFile() {
        return mRecordFile;
    }

    /**
     * 返回拍照文件
     *
     * @return
     */
    public File getCaptureFile() {
        return mCaptureFile;
    }

    /**
     * 返回录像文件路径
     *
     * @return recordFile
     */
    public String getRecordFilePath() {
        return null == mRecordFile ? "" : mRecordFile.getPath();
    }


    /**
     * 返回拍照文件路径
     *
     * @return
     */
    public String getCaptureFilePath() {
        return null == mCaptureFile ? "" : mCaptureFile.getPath();
    }

    /**
     * 获取旋转后的图片路径
     *
     * @return
     */
    public String getRotateCaptureFilePath() {
        return getCaptureFilePath().replace(".jpg", "_rotate.jpg");
    }

    /**
     * 创建视频缩略图
     *
     * @param path
     * @return
     */
    public Bitmap createVideoThumbnail(String path) {
        Bitmap b = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            b = retriever.getFrameAtTime();
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
     * 闪光灯类型枚举 默认为关闭
     */
    public enum FlashMode {
        /**
         * ON:拍照时打开闪光灯
         */
        ON, /**
         * OFF：不打开闪光灯
         */
        OFF, /**
         * AUTO：系统决定是否打开闪光灯
         */
        AUTO, /**
         * TORCH：一直打开闪光灯
         */
        TORCH
    }
}
