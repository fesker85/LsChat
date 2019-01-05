package cc.lzsou.lschat.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.main.activity.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        Intent goToSettings = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        goToSettings.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(goToSettings);
        checkPermisson();
    }

    private void checkPermisson() {

        if(ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.VIBRATE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.ACCESS_NETWORK_STATE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.ACCESS_WIFI_STATE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.CHANGE_WIFI_STATE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.BROADCAST_STICKY)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.GET_ACCOUNTS)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(WelcomeActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[]{
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.BROADCAST_STICKY,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION},2);
        }
        else{
            handler.postDelayed(runnable,3000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED && grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED && grantResults[6] == PackageManager.PERMISSION_GRANTED && grantResults[7] == PackageManager.PERMISSION_GRANTED && grantResults[8] == PackageManager.PERMISSION_GRANTED && grantResults[9] == PackageManager.PERMISSION_GRANTED && grantResults[10] == PackageManager.PERMISSION_GRANTED && grantResults[11] == PackageManager.PERMISSION_GRANTED && grantResults[12] == PackageManager.PERMISSION_GRANTED && grantResults[13] == PackageManager.PERMISSION_GRANTED && grantResults[14] == PackageManager.PERMISSION_GRANTED) {
                    handler.postDelayed(runnable, 3000);
                } else {
                    WelcomeActivity.this.finish();
                }

                break;
            default:
                break;
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            MemberEntity entity= MemberEntityImpl.getInstance().selectRow();
            if (entity==null||entity.getId()<1)
                intent.setClass(WelcomeActivity.this, LoginActivity.class);
            else intent.setClass(WelcomeActivity.this, MainActivity.class);


            WelcomeActivity.this.startActivity(intent);
            WelcomeActivity.this.finish();
        }
    };
    Handler handler = new Handler();


    public String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
