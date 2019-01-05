package cc.lzsou.lschat.chat.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.chat.fragment.FragmentChatEnter;
import cc.lzsou.lschat.chat.fragment.FragmentChatGif;
import cc.lzsou.lschat.chat.fragment.FragmentChatMood;
import cc.lzsou.lschat.chat.fragment.FragmentChatVoice;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.POIEntity;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.chat.Helper.TapeVoiceHelper;
import cc.lzsou.lschat.chat.adapter.ChatAdapter;
import cc.lzsou.lschat.chat.listener.OnChatItemListener;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.data.impl.MessageEntityImpl;
import cc.lzsou.lschat.data.impl.TempMessageEntityImpl;
import cc.lzsou.lschat.manager.ImageManager;
import cc.lzsou.lschat.manager.VideoManager;
import cc.lzsou.lschat.map.activity.LocationActivity;
import cc.lzsou.lschat.service.IMService;
import cc.lzsou.lschat.views.expression.ExpressionUtil;
import cc.lzsou.media.CameraActivity;
import cc.lzsou.media.PickerActivity;
import cc.lzsou.media.PickerConfig;
import cc.lzsou.media.entity.Media;

import static cc.lzsou.lschat.service.MessageSender.MESSAGE_MODE_CHAT;
import static cc.lzsou.lschat.service.MessageSender.MESSAGE_ID;
import static cc.lzsou.lschat.service.MessageSender.MESSAGE_MODE;
import static cc.lzsou.lschat.service.IMService.SEND_MESSAGE;
import static cc.lzsou.lschat.service.IMService.APPEND_FRIENDID;
import static cc.lzsou.lschat.service.IMService.REMOVE_FRIENDID;

public class ChatActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ALBUM = 3;//图片
    private static final int REQUEST_CODE_LOACTION = 4;//位置
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.listView)
    EasyRecyclerView listView;
    @BindView(R.id.frameInput)
    FrameLayout frameInput;
    @BindView(R.id.frameOther)
    FrameLayout frameOther;


    public static ChatActivity instance;


    private MessageReceiver receiver;//监听广播
    private FragmentManager fragmentManager;
    private FragmentChatEnter inputFragment;//输入fragment

    private Fragment currentFragment;
    private List<Fragment> fragmentList;

    public FriendEntity friendEntity;//好友信息
    private MemberEntity memberEntity;//当前用户
    private LinearLayoutManager layoutManager;
    private ChatAdapter adapter;
    private boolean isScrolledToBottom = true;


    //录音
    TapeVoiceHelper voiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Integer fid = getIntent().getIntExtra("fid", 0);
        friendEntity = FriendEntityImpl.getInstance().selectRow(fid);
        memberEntity = MemberEntityImpl.getInstance().selectRow();
        createReceiver();
        instance = this;
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        init();
        TempMessageEntityImpl.getInstance().delete("chat" + friendEntity.getId());
        sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        removeReceiver();
        super.onDestroy();
    }

    private void init() {
        titleView.setText(friendEntity.getNickname());
        inputFragment = new FragmentChatEnter();
        inputFragment.addOnEnterClickListener(enterListener);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameInput, inputFragment);
        transaction.commit();

        fragmentList = new ArrayList<>();
        FragmentChatVoice fragmentChatVoice = new FragmentChatVoice();//语音
        fragmentChatVoice.addOnViewCreated(new FragmentChatVoice.OnViewCreated() {
            @Override
            public void onCreated(TextView textView) {
                voiceHelper.init(textView);
            }

            @Override
            public boolean onVoiceTouch(MotionEvent event) {
                return voiceHelper.tape(event);
            }
        });
        fragmentList.add(fragmentChatVoice);

        FragmentChatMood fragmentChatMood = new FragmentChatMood();//表情
        fragmentChatMood.AddOnMoodListener(new FragmentChatMood.OnMoodListener() {
            @Override
            public void onMoodClickListener(boolean isLastItem, String moodName) {
                if (isLastItem) {
                    inputFragment.editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    return;
                }
                int curPosition = inputFragment.editText.getSelectionStart();
                StringBuilder sb = new StringBuilder(inputFragment.editText.getText().toString());
                sb.insert(curPosition, moodName);
                // 特殊文字处理,将表情等转换一下
                inputFragment.editText.setText(ExpressionUtil.getText(ChatActivity.this, sb.toString()));
                // 将光标设置到新增完表情的右侧
                inputFragment.editText.setSelection(curPosition + moodName.length());
            }
        });
        fragmentList.add(fragmentChatMood);

        FragmentChatGif fragmentChatGif = new FragmentChatGif();//gif
        fragmentChatGif.AddOnGifListener(new FragmentChatGif.OnGifListener() {
            @Override
            public void onGifClickListener(boolean isLastItem, String gifName) {
                sendGif(gifName);
            }
        });
        fragmentList.add(fragmentChatGif);

        adapter = new ChatAdapter(ChatActivity.this, memberEntity, friendEntity);
        adapter.addItemClickListener(chatItemListener);
        layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        listView = (EasyRecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(adapter);
        listView.setRefreshing(false);
        listView.setOnScrollListener(scrollListener);
        adapter.addAll(MessageEntityImpl.getInstance().selectChatList(friendEntity.getId(), "", 0));
        adapter.notifyDataSetChanged();
        listView.scrollToPosition(adapter.getCount() - 1);
        voiceHelper = new TapeVoiceHelper(ChatActivity.this);
    }


    public void hideFragment() {
        frameOther.setVisibility(View.GONE);
    }

    public void showFragment(int position) {
        frameOther.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!fragmentList.get(position).isAdded()) {
            if (currentFragment == null)
                transaction.add(R.id.frameOther, fragmentList.get(position));
            else transaction.hide(currentFragment).add(R.id.frameOther, fragmentList.get(position));
        } else transaction.hide(currentFragment).show(fragmentList.get(position));
        currentFragment = fragmentList.get(position);
        transaction.commit();
    }

    //输入框点击事件
    private FragmentChatEnter.OnEnterClickListener enterListener = new FragmentChatEnter.OnEnterClickListener() {
        @Override
        public void onSendClick(EditText editText) {
            if(editText.getText().length()>0){
                sendText(editText.getText().toString());
                editText.setText("");
            }
        }

        @Override
        public void onMicphoneClick(boolean visibility) {
            if (visibility) showFragment(0);
            else hideFragment();
        }

        @Override
        public void onPhotoClick(boolean visibility) {
            Intent intentImage = new Intent(ChatActivity.this, PickerActivity.class);
            intentImage.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);
            startActivityForResult(intentImage, REQUEST_CODE_ALBUM);
        }

        @Override
        public void onCameraClick(boolean visibility) {
            startActivity(new Intent(ChatActivity.this, CameraActivity.class));
        }

        @Override
        public void onLocationClick(boolean visibility) {
            startActivityForResult(new Intent(ChatActivity.this, LocationActivity.class), REQUEST_CODE_LOACTION);
        }

        @Override
        public void onGifClick(boolean visibility) {
            if (visibility) showFragment(2);
            else hideFragment();
        }

        @Override
        public void onRedEnvelopeClick(boolean visibility) {

        }

        @Override
        public void onGiftClick(boolean visibility) {

        }

        @Override
        public void onMoodClick(boolean visibility) {
            if (visibility) showFragment(1);
            else hideFragment();
        }

        @Override
        public void onPlusClick(boolean visibility) {

        }
    };


    //聊天记录点击事件
    private OnChatItemListener chatItemListener = new OnChatItemListener() {
        @Override
        public void onHeaderClick(int position) {

        }

        @Override
        public void onImageClick(View view, int position, MessageEntity data) {

        }

        @Override
        public void onVoiceClick(ImageView imageView, int position, MessageEntity data) {
            voiceHelper.play(imageView, data);
        }

        @Override
        public void onVideoClick(ImageView imageView, int position, MessageEntity data) {

        }
    };
    //聊天列表滚动
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            int lastPosition = -1;
            int firstPosition = -1;
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                }
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {//滚到底部
                    isScrolledToBottom = true;
                }
                if (firstPosition == 0) {//滚到顶部
                    if (adapter.getAllData().size() < 1) {
                        adapter.addAll(MessageEntityImpl.getInstance().selectChatList(friendEntity.getId(), "", 0));
                    } else {
                        String lasttime = adapter.getAllData().get(0).getCurtime();
                        adapter.insertAll(MessageEntityImpl.getInstance().selectChatList(friendEntity.getId(), lasttime, -1), 0);
                    }
                    if (adapter.getCount() > 15) isScrolledToBottom = false;
                }
            }
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        }
    };

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ActionFlag.ACTION_MESSAGE)) {

                String msgId = intent.getStringExtra("msgid");
                if (msgId != null && !msgId.equals("")) {
                    int state = intent.getIntExtra("state", MessageEntity.STATE_SEND_FAILED);
                    for (MessageEntity item : adapter.getAllData()) {
                        if (item.getId().equals(msgId)) {
                            item.setStatus(state);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                } else {
                    if (adapter.getAllData().size() < 1) {
                        adapter.addAll(MessageEntityImpl.getInstance().selectChatList(friendEntity.getId(), "", 0));
                    } else {
                        String lasttime = adapter.getAllData().get(adapter.getAllData().size() - 1).getCurtime();
                        adapter.addAll(MessageEntityImpl.getInstance().selectChatList(friendEntity.getId(), lasttime, 1));
                    }
                    if (isScrolledToBottom) listView.scrollToPosition(adapter.getCount() - 1);
                }
            }

        }
    }

    @OnClick({R.id.backButton, R.id.personButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.personButton:
                break;
        }
    }

    private void createReceiver() {
        receiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionFlag.ACTION_MESSAGE);
        registerReceiver(receiver, intentFilter);

        Intent intent = new Intent(APPEND_FRIENDID);
        intent.setClass(this, IMService.class);
        intent.putExtra("fid", friendEntity.getId());
        startService(intent);
    }

    private void removeReceiver() {
        if (receiver != null) unregisterReceiver(receiver);
        Intent intent = new Intent(REMOVE_FRIENDID);
        intent.setClass(this, IMService.class);
        startService(intent);
    }

    //发送gif 图片
    private void sendGif(String emotionName) {
        sendMessage(null, emotionName, 0);
    }

    //发送文字
    private void sendText(String msg) {
        sendMessage(null, msg, 0);
    }

    //发送位置
    private void sendLocation(Intent data) {
        String image = data.getStringExtra("img");
        String id = data.getStringExtra("id");
        POIEntity entity = JsonHelper.jsonToObject(data.getStringExtra("data"), POIEntity.class);
        if (entity == null) return;
        String msg = MessageFlag.MESSAGE_FLAG_LOCATION + "][" + entity.getName() + "][" + entity.getAddress() + "][" + entity.getLatitude() + "," + entity.getLongitude() + "][" + image + "]";
        sendMessage(id, msg, 0);
    }

    //发送图片和视频
    public void sendImageAndVideo(Intent data) {
        ArrayList<Media> list = data.getParcelableArrayListExtra("data");
        if (list == null || list.size() < 1) return;
        for (Media item : list) {
            new AsynHandler(ChatActivity.this, true, "发送中") {
                @Override
                protected Object onProcess() {
                    Bitmap bitmap = null;
                    long time = 0;
                    String msg = "";
                    if (item.mediaType == 1) {//图片
                        bitmap = ImageManager.createThumbnail(item.path);
                        String base64String = ImageManager.bitmap2Base64String(bitmap);
                        msg = MessageFlag.MESSAGE_FLAG_IMAGE + "][" + base64String + "][" + item.path + "]";
                    }
                    if (item.mediaType == 3) {//视频
                        bitmap = ImageManager.createThumbnail(VideoManager.createThumbnail(item.path));
                        time = VideoManager.getDuration(item.path);
                        String base64String = ImageManager.bitmap2Base64String(bitmap);
                        msg = MessageFlag.MESSAGE_FLAG_VIDEO + "][" + time + "][" + base64String + "][" + item.path + "]";

                    }
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    return new String[]{msg,""+time};
                }

                @Override
                protected void onResult(Object object) {
                    String[] arrString = (String[])object;
                    sendMessage(null, arrString[0], Long.parseLong(arrString[1]));
                }
            };


        }


    }

    //发送信息
    public void sendMessage(String id, String msg, long voicetime) {
        if (id == null || id.equals("")) id = Common.getId();
        MessageEntity entity = new MessageEntity();
        entity.setStatus(MessageEntity.STATE_SEND_SENDING);
        entity.setRectime(voicetime);
        entity.setCurtime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
        entity.setPath(MessageEntity.PATH_TO);
        entity.setMsg(msg);
        entity.setUid(friendEntity.getId());
        entity.setMid("chat" + friendEntity.getId());
        entity.setMode(MessageEntity.MODE_CHAT);
        entity.setId(id);

        MessageEntityImpl.getInstance().insertRow(entity);
        adapter.add(entity);
        isScrolledToBottom = true;
        Intent intent = new Intent();
        intent.setAction(SEND_MESSAGE);
        intent.setClass(getApplication(), IMService.class);
        intent.putExtra(MESSAGE_MODE, MESSAGE_MODE_CHAT);
        intent.putExtra(MESSAGE_ID, entity.getId());
        getApplication().startService(intent);
        getApplication().sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        if (res != RESULT_OK) return;
        switch (req) {
            case REQUEST_CODE_ALBUM: //选择图片
                sendImageAndVideo(data);
                break;
            case REQUEST_CODE_LOACTION:
                sendLocation(data);
                break;
            default:
                break;
        }
        super.onActivityResult(req, res, data);
    }


}
