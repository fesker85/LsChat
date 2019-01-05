package cc.lzsou.lschat.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.manager.ImageManager;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.core.utils.QRCodeUtil;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;

public class QRCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backView;
    private TextView titleView;
    private ImageView imageView;
    private String title;
    private String parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        title = getIntent().getStringExtra("title");
        parameter = getIntent().getStringExtra("parm");

        backView = (ImageButton) findViewById(R.id.backView);
        backView.setOnClickListener(this);
        titleView = (TextView) findViewById(R.id.titleView);
        imageView = (ImageView) findViewById(R.id.imageView);

        if (title != null && !title.equals("")) titleView.setText(title);

        if(title.equals("我的二维码")) {
            MemberEntity entity = MemberEntityImpl.getInstance().selectRow();
            Bitmap logo = ImageLoaderManager.getInstance().loadAvatar(entity.getAvatar());
            if(logo==null)
                logo= ImageManager.readBitMap(QRCodeActivity.this,R.drawable.default_avatar);
            Bitmap bitmap= QRCodeUtil.createQRCodeBitmap(parameter,320, logo,0.2f);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Bitmap bitmap =QRCodeUtil.createQRCodeBitmap(parameter,320);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.backView)
            finish();
    }
}
