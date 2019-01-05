package cc.lzsou.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cc.lzsou.media.entity.Media;
import cc.lzsou.media.utils.CropUtils;
import cc.lzsou.media.utils.FileHelper;
import cc.lzsou.media.view.CameraButton;
import cc.lzsou.media.view.CameraView;
import cc.lzsou.selectorlibrary.R;

public class CameraActivity extends Activity implements CameraView.OnCameraListener, View.OnClickListener, CameraButton.OnClickListener {
    private CameraButton btnDone;
    private CameraView cameraView;
    private ImageView btnFlash;
    private ImageView imageView;
    private RelativeLayout imageLayout;
    private RelativeLayout cameraLayout;
    private Bitmap captureBitmap;//拍照临时图片
    private int recordTime;//拍摄视频时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        init();
    }
    private void init() {
        imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        cameraLayout = (RelativeLayout) findViewById(R.id.cameraLayout);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        cameraView.setMaxDuration(10);
        cameraView.addOnCameraListener(this);
        btnFlash = (ImageView) findViewById(R.id.btnFlash);
        btnFlash.setOnClickListener(this);
        btnDone = (CameraButton) findViewById(R.id.btnDone);
        btnDone.addOnClickListener(this);
        findViewById(R.id.btnSwitch).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnClose).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        release("");
        super.onDestroy();
    }


    @Override
    public void onRecordFinish(String path) {
        if (recordTime < 2) {
            release(path);
            Toast.makeText(CameraActivity.this, "拍摄时间太短了", Toast.LENGTH_SHORT).show();
            return;
        }
        if (path == null && path.equals("")) {
            release("");
            Toast.makeText(CameraActivity.this, "保存视频失败", Toast.LENGTH_SHORT).show();
            return;
        }
        cameraLayout.setVisibility(View.GONE);
        imageLayout.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(cameraView.createVideoThumbnail(path));
        imageView.setTag(path);
    }

    @Override
    public void onRecordProgress(int total, int current) {
        recordTime = current;
    }

    @Override
    public void onCaptureFinish(byte[] data) {
        if (data == null || data.length < 1) {
            release("");
            Toast.makeText(CameraActivity.this, "拍照失败", Toast.LENGTH_SHORT).show();
            return;
        }
        cameraLayout.setVisibility(View.GONE);
        imageLayout.setVisibility(View.VISIBLE);
        captureBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageView.setImageBitmap(captureBitmap);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnFlash) {
            cameraView.switchFlashMode();
            CameraView.FlashMode mode = cameraView.getFlashMode();
            switch (mode) {
                case ON:
                    btnFlash.setImageResource(R.drawable.ic_flash_on_white_32dp);
                    break;
                case TORCH:
                    btnFlash.setImageResource(R.drawable.ic_flash_on_white_32dp);
                    break;
                case OFF:
                    btnFlash.setImageResource(R.drawable.ic_flash_off_white_32dp);
                    break;
                case AUTO:
                    btnFlash.setImageResource(R.drawable.ic_flash_auto_white_32dp);
                    break;
            }
        } else if (i == R.id.btnSwitch) {
            cameraView.switchCamera();
        } else if (i == R.id.btnCancel) {
            String path = imageView.getTag() == null ? "" : imageView.getTag().toString();
            release(path);
            imageLayout.setVisibility(View.GONE);
            cameraLayout.setVisibility(View.VISIBLE);
        } else if (i == R.id.btnClose) {
            CameraActivity.this.finish();
        } else if (i == R.id.btnOk) {
            String videoPath = imageView.getTag() == null ? "" : imageView.getTag().toString();
            if (videoPath != null && !videoPath.equals(""))
                done(new Media(videoPath, "", 0, 3, 0, 0, ""));//图片
            else {
                if (captureBitmap == null) {
                    Toast.makeText(CameraActivity.this, "保存图片失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    File file = new File(FileHelper.getInstance(CameraActivity.this).getImagePath() + "/" + FileHelper.getInstance(CameraActivity.this).getId() + ".jpg");
                    if (!file.exists()) file.getParentFile().mkdirs();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    captureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    done(new Media(file.getPath(), "", 0, 1, 0, 0, ""));
                } catch (IOException e) {
                    Toast.makeText(CameraActivity.this, "保存图片失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri resultUri = UCrop.getOutput(data);
            done(new Media(resultUri.getPath(), "", 0, 1, 0, 0, ""));
        }
    }

    private void release(String videoPath) {
        imageView.setTag("");
        if (videoPath != null && !videoPath.equals(""))
            FileHelper.getInstance(CameraActivity.this).remove(videoPath);
        if (captureBitmap != null && !captureBitmap.isRecycled()) {
            captureBitmap.recycle();
            captureBitmap = null;
        }
    }

    private void done(Media media) {
        ArrayList<Media> selects = new ArrayList<>();
        selects.add(media);
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("data", selects);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick() {
        cameraView.startCapture();
    }

    @Override
    public void onTouchStart() {
        cameraView.startRecord();
    }

    @Override
    public void onTouchEnd() {
        cameraView.stopRecord();
    }
}
