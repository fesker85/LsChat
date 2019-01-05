package cc.lzsou.lschat.selector.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//import com.yalantis.ucrop.UCrop;
//import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.data.bean.ImageDirEntity;
import cc.lzsou.lschat.data.bean.ImageEntity;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.selector.adapter.ImageDirAdapter;
import cc.lzsou.lschat.selector.adapter.ImageSelectorAdapter;
import cc.lzsou.lschat.selector.adapter.SpacesItemDecoration;
import cc.lzsou.lschat.selector.listener.OnImageChangeListener;
import cc.lzsou.lschat.selector.listener.OnImageDirItemListener;
import cc.lzsou.lschat.selector.listener.OnImageItemClickListener;

public class ImageSelectorActivity extends AppCompatActivity implements OnImageItemClickListener, OnImageChangeListener, View.OnClickListener, OnImageDirItemListener, PopupWindow.OnDismissListener {

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_CUT = 2;// 结果

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageDirEntity> listImageDir = new ArrayList<>();
    /**
     * 图片数量
     */
    private int totalCount = 0;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();
    /**
     * 所有的图片
     */
    private List<ImageEntity> listImage = new ArrayList<>();
    /**
     * 选中的图片集合
     */
    private ArrayList<ImageEntity> selectListImage = new ArrayList<>();
    /**
     * 最大的图片数
     */
    private int maxImageCount = 9;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 用来存储选中的文件
     */
    private File mSelectFile;

    /**
     * 用于显示全部文件夹
     */
    private PopupWindow mPopupWindow;
    /**
     * 当PopupWindow显示或者消失时改变背景色
     */
    private WindowManager.LayoutParams lp;
    /**
     * 拿到传过来的值，测试选择图片
     */
    private int select;
    /**
     * 获取到选择的是否裁剪
     */
    private int crop = 0;//不裁剪 1裁剪

    /**
     * 存储拍照和选中的图片
     */
    private File file;

    private ImageDirEntity imageDirEntity;
    private ImageEntity imageEntity;

    private ImageButton backView;
    private Button complateButton;
    private RecyclerView recyclerView;
    private TextView dirView;
    private TextView countView;
    private LinearLayout bottomLayout;
    private ImageSelectorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);
        select = getIntent().getIntExtra("select", 0);
        crop = getIntent().getIntExtra("crop", 0);
        maxImageCount = select;
        backView = (ImageButton) findViewById(R.id.backView);
        backView.setOnClickListener(this);
        complateButton = (Button) findViewById(R.id.complateButton);
        complateButton.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        dirView = (TextView) findViewById(R.id.dirView);
        countView = (TextView) findViewById(R.id.countView);
        bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        mPopupWindow = new PopupWindow(this);
        //获取屏幕高度，设给PopupWindow
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        lp = getWindow().getAttributes();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        complateButton.setText("确定" + selectListImage.size() + "/" + maxImageCount);
        getImageList();
        setImageDirData();
        dirView.setOnClickListener(this);
        countView.setOnClickListener(this);
        mPopupWindow.setOnDismissListener(this);
        file = new File(FileManager.getInstance().getCropPath()+"/temp.jpg");
    }

    private void setData() {
        adapter = new ImageSelectorAdapter(this, maxImageCount, listImage, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        countView.setText(totalCount + "张");
    }

    //图片文件数据
    private void setImageDirData() {
        if (!listImageDir.isEmpty()) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_image_dir, null);
            RecyclerView rcyViewImageDir = (RecyclerView) contentView.findViewById(R.id.rcyView_imageDir);
            rcyViewImageDir.setLayoutManager(new LinearLayoutManager(this));
            rcyViewImageDir.setAdapter(new ImageDirAdapter(this, listImageDir, this));

            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight((int) (mScreenHeight * 0.7f));
            mPopupWindow.setContentView(contentView);

            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.showAsDropDown(bottomLayout, 0, 0);
            // 设置背景颜色变暗
            lp.alpha = 0.5f;
            getWindow().setAttributes(lp);
        }
    }

    private void getImageList() {
        //判断是否有内存卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
        } else {
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dirView:
                setImageDirData();
                break;
            case R.id.backView:
                finish();
                break;
            case R.id.complateButton:
                if (selectListImage.size() != 0) {
                    onSelected(selectListImage);
                } else {
                    Toast.makeText(this, "请选择至少一张图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDismiss() {
        // 设置背景颜色变暗
        lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    @Override
    public void OnChangeListener(int position, boolean isChecked) {
        if (isChecked) {
            listImage.get(position).setSelect(true);
            if (!contains(selectListImage, listImage.get(position))) {
                selectListImage.add(listImage.get(position));
                if (selectListImage.size() == maxImageCount) {
                    adapter.notifyData(selectListImage);
                }
            }
        } else {
            listImage.get(position).setSelect(false);
            if (contains(selectListImage, listImage.get(position))) {
                selectListImage.remove(listImage.get(position));
                if (selectListImage.size() == maxImageCount - 1) {
                    adapter.notifyData(selectListImage);
                }
            }
        }
        complateButton.setText("确定" + selectListImage.size() + "/" + maxImageCount);
    }

    @Override
    public void onImageDirItemListener(View view, int position) {
        listImage.clear();
        listImage.add(null);
        if (selectListImage.size() > 0) {
            selectListImage.clear();
        }
        String dir = listImageDir.get(position).getDir();
        mSelectFile = new File(dir);
        List<String> imagePath = Arrays.asList(mSelectFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        for (int i = 0; i < imagePath.size(); i++) {
            imageEntity = new ImageEntity();
            imageEntity.setPath(dir + "/" + imagePath.get(i));
            imageEntity.setSelect(false);
            listImage.add(imageEntity);
        }

        dirView.setText(listImageDir.get(position).getImageName());
        countView.setText(listImageDir.get(position).getImageCount() + "张");
        adapter.notifyDataSetChanged();
        mPopupWindow.dismiss();
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (position != 0) {
            if (maxImageCount == 1) {
                if (crop == 1)//开始裁剪图片
                    clipPhoto(Uri.fromFile(new File(listImage.get(position).getPath())));//开始裁减图片
                else {
                    ArrayList<ImageEntity> temp = new ArrayList<>();
                    ImageEntity ie = new ImageEntity();
                    ie.setPath(listImage.get(position).getPath());
                    temp.add(ie);
                    onSelected(temp);
                }
            }
        } else {
            file = new File(FileManager.getInstance().getCropPath()+"/"+ Common.getId()+".jpg");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 下面这句指定调用相机拍照后的照片存储的路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cameraIntent, PHOTO_REQUEST_CAMERA);// CAMERA_OK是用作判断返回结果的标识
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) switch (requestCode) {
            case PHOTO_REQUEST_CAMERA:
                if (crop == 1)//开始裁剪图片
                    clipPhoto(Uri.fromFile(file));//开始裁减图片
                else {
                    ArrayList<ImageEntity> temp = new ArrayList<>();
                    ImageEntity ie = new ImageEntity();
                    ie.setPath(file.getPath());
                    temp.add(ie);
                    onSelected(temp);
                }
                break;
//            case UCrop.REQUEST_CROP:
//                Uri resultUri = UCrop.getOutput(data);
//                ArrayList<ImageEntity> temp = new ArrayList<>();
//                ImageEntity ie = new ImageEntity();
//                ie.setPath(resultUri.getPath());
//                temp.add(ie);
//                onSelected(temp);
//                break;
        }
    }

    //裁剪
    private void clipPhoto(Uri uri) {
//        File file = new File(FileManager.getInstance().getCropPath(), "crop.jpg");
//        UCrop.Options options = new UCrop.Options();
//        //设置裁剪图片可操作的手势
//        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
//        //设置toolbar颜色
//        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
//        //设置状态栏颜色
//        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));
//        options.setHideBottomControls(true);
//        UCrop.of(uri, Uri.fromFile(file)).withAspectRatio(1, 1).withMaxResultSize(300, 300).withOptions(options).start(ImageSelectorActivity.this);
    }

    private boolean contains(List<ImageEntity> list, ImageEntity imageBean) {
        for (ImageEntity bean : list) {
            if (bean.getPath().equals(imageBean.getPath())) {
                return true;
            }
        }
        return false;
    }

    private void initData() {
        new AsynHandler(ImageSelectorActivity.this, true, "加载中") {
            @Override
            protected Object onProcess() {
                //第一张图片
                String firstImage = null;
                //获取内存卡路径
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                //通过内容解析器解析出png和jpeg格式的图片
                ContentResolver mContentResolver = ImageSelectorActivity.this.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/png", "image/jpeg"}, MediaStore.Images.Media.DATE_MODIFIED);
                //判断是否存在图片
                if (mCursor.getCount() > 0) {
                    listImage.add(null);
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                        if (path.toUpperCase().contains("LSCHAT")) continue;

                        // 拿到第一张图片的路径
                        if (firstImage == null) firstImage = path;
                        // 获取该图片的文件名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null) continue;
                        //获取到文件地址
                        String dirPath = parentFile.getAbsolutePath();
                        imageEntity = new ImageEntity();
                        imageEntity.setPath(path);
                        listImage.add(imageEntity);
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            imageDirEntity = new ImageDirEntity();
                            imageDirEntity.setDir(dirPath);
                            imageDirEntity.setImagePath(path);
                        }
                        int picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        }).length;
                        totalCount += picSize;

                        imageDirEntity.setImageCount(picSize);
                        listImageDir.add(imageDirEntity);
                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                        }
                    }
                    mCursor.close();
                    mDirPaths = null;
                }
                return null;
            }

            @Override
            protected void onResult(Object object) {
                setData();
            }
        };
    }

    //完成选择
    private void onSelected(ArrayList<ImageEntity> list) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("data", list);
        setResult(RESULT_OK, intent);
        finish();
    }
}
