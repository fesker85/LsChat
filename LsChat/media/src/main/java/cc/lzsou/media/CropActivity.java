package cc.lzsou.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import cc.lzsou.media.entity.Media;
import cc.lzsou.media.utils.CropUtils;
import cc.lzsou.media.utils.FileHelper;

public class CropActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filePath  = getIntent().getStringExtra(PickerConfig.DEFAULT_CROP_FILEPATH);
        if(filePath==null||filePath.equals("")){
            finish();
            return;
        }
        CropUtils.startCrop(new Media(filePath,"",0,1,0,0,""),CropActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK)return;
        ArrayList<Media> medias = new ArrayList<>();
        switch (requestCode){
            case UCrop.REQUEST_CROP:
                Uri resultUri = UCrop.getOutput(data);
                File file = new File(resultUri.getPath());
                medias.add(new Media(file.getPath(), file.getName(), 0, 1, file.length(), 0, ""));
                done(medias);
                break;
        }
    }

    private void done(ArrayList<Media> medias){
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("data", medias);
        setResult(RESULT_OK, intent);
        finish();
    }





}
