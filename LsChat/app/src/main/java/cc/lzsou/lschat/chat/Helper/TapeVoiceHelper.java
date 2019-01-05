package cc.lzsou.lschat.chat.Helper;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.logging.FileHandler;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.chat.activity.ChatActivity;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.AudioRecoderHelper;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.core.utils.PopupWindowFactory;
import cc.lzsou.lschat.manager.MediaManager;

//聊天录音
public class TapeVoiceHelper {

    private Activity activity;
    //录音部分
    private View voicePopView;
    private PopupWindowFactory voicePop;
    private AudioRecoderHelper audioRecoderHelper;
    private TextView voicePopText;
    private TextView voiceButton;

    //播放部分
    private int animationRes = 0;
    private int res = 0;
    private AnimationDrawable animationDrawable = null;
    private ImageView animView;

    public TapeVoiceHelper(Activity activity) {
        this.activity = activity;
    }

    public void init(final TextView voiceButton) {
        this.voiceButton = voiceButton;
        audioRecoderHelper = new AudioRecoderHelper();
        voicePopView = LayoutInflater.from(activity).inflate(R.layout.layout_microphone, null);
        voicePop = new PopupWindowFactory(activity, voicePopView);
        //PopupWindow布局文件里面的控件
        final ImageView mImageView = (ImageView) voicePopView.findViewById(R.id.iv_recording_icon);
        final TextView mTextView = (TextView) voicePopView.findViewById(R.id.tv_recording_time);
        voicePopText = (TextView) voicePopView.findViewById(R.id.tv_recording_text);
        //录音回调
        audioRecoderHelper.setOnAudioStatusUpdateListener(new AudioRecoderHelper.OnAudioStatusUpdateListener() {
            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(DateHelper.long2String(time));
                if (time >= 59 * 1000) {
                    audioRecoderHelper.stopRecord();
                    voiceButton.setText("按住说话");
                    voiceButton.setTag("3");
                }
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(long time,String id) {
                if (time < 1100) {
                    MessageHandler.showToask("太短了");
                    return;
                }
                String filename = FileManager.getInstance().getVoicePath()+"/"+id;
                String base64String = FileManager.getInstance().fileToBase64String(filename);
                mTextView.setText(DateHelper.long2String(0));
                String msg = MessageFlag.MESSAGE_FLAG_VOICE + "][" + String.valueOf(time) + "]["+base64String+"]";
                ((ChatActivity) activity).sendMessage(null,msg, time);
            }

            @Override
            public void onError() {

            }
        });
    }

    public boolean tape(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                voicePop.showAtLocation(voiceButton, Gravity.CENTER, 0, 0);
                voiceButton.setText("松开结束");
                voicePopText.setText("手指上滑，取消发送");
                voiceButton.setTag("1");
                audioRecoderHelper.startRecord(activity);
                break;
            case MotionEvent.ACTION_MOVE:
                if (wantToCancle(x, y)) {
                    voiceButton.setText("松开结束");
                    voicePopText.setText("松开手指，取消发送");
                    voiceButton.setTag("2");
                } else {
                    voiceButton.setText("松开结束");
                    voicePopText.setText("手指上滑，取消发送");
                    voiceButton.setTag("1");
                }
                break;
            case MotionEvent.ACTION_UP:
                voicePop.dismiss();
                if (voiceButton.getTag().equals("2")) {
                    //取消录音（删除录音文件）
                    audioRecoderHelper.cancelRecord();
                } else {
                    //结束录音（保存录音文件）
                    audioRecoderHelper.stopRecord();
                }
                voiceButton.setText("按住说话");
                voiceButton.setTag("3");
                break;
        }
        return true;
    }

    private boolean wantToCancle(int x, int y) {
        // 超过按钮的宽度
        if (x < 0 || x > voiceButton.getWidth()) {
            return true;
        }
        // 超过按钮的高度
        if (y < -50 || y > voiceButton.getHeight() + 50) {
            return true;
        }
        return false;
    }

    public void play(ImageView imageView,MessageEntity entity){

        String filename = FileManager.getInstance().getVoicePath()+"/"+entity.getId()+".amr";
        File file = new File(filename);
        if(!file.exists()){
            String[] arrString = entity.getMsg().split("]");
            if(arrString.length<2)return;
            String base64String = arrString[2].substring(1);
            byte[] data = FileManager.getInstance().base64StringToBytes(base64String);
            if(FileManager.getInstance().saveFile(data,filename))play(filename,entity.getPath(),imageView);
            else MessageHandler.showToask("语音文件已损坏");
        }
        else play(filename,entity.getPath(),imageView);
    }

    private void play(String fileName,int mode,ImageView imageView){
        if (animView != null) {
            animView.setImageResource(res);
            animView = null;
        }
        switch (mode) {
            case MessageEntity.PATH_FTOM:
                animationRes = R.drawable.voice_left;
                res = R.mipmap.icon_voice_left3;
                break;
            case MessageEntity.PATH_TO:
                animationRes = R.drawable.voice_right;
                res = R.mipmap.icon_voice_right3;
                break;
        }
        animView = imageView;
        animView.setImageResource(animationRes);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        MediaManager.playSound(fileName, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                animView.setImageResource(res);
                animationDrawable.stop();
            }
        });
    }

}
