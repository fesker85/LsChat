package cc.lzsou.lschat.core.handler;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;


public class TimerDownHandler extends CountDownTimer {

    private Button btn;
    public TimerDownHandler(long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.btn=btn;
    }
    @Override
    public void onTick(long l) {
        this.btn.setEnabled(false);
        this.btn.setClickable(false);
        this.btn.setTextColor(Color.parseColor("#aaaaaa"));
        this.btn.setText("("+(l/1000)+")秒后获取");
    }

    @Override
    public void onFinish() {
        this.btn.setEnabled(true);
        this.btn.setClickable(true);
        this.btn.setTextColor(Color.parseColor("#666666"));
        this.btn.setText("获取验证码");
    }
}
