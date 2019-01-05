package cc.lzsou.lschat.service;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;


import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.core.handler.UploadHandler;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.data.impl.MessageEntityImpl;
import cc.lzsou.servercore.LinkServer;

public class MessageSender {

    public static final int MESSAGE_MODE_CHAT = 1;
    public static final int MESSAGE_MODE_MUC = 2;
    public static final int MESSAGE_MODE_NORMAL=3;

    public static final String MESSAGE_MODE = "MESSAGE_MODE";
    public static final String MESSAGE_ID = "MESSAGE_ID";

    private IMService imService;

    public MessageSender(IMService imService) {
        this.imService = imService;
    }

    //发送消息回调
    public void onSendBack(Stanza packet) {
        if (packet instanceof Message) {
            Message message = (Message) packet;
            String msgId = message.getStanzaId();
            updateMessageState(msgId, message.getError() == null ? MessageEntity.STATE_SEND_SUCCESS : MessageEntity.STATE_SEND_FAILED);
        }
    }

    public void sendMessage(Intent intent) {
        int mode = intent.getIntExtra(MESSAGE_MODE, -1);
        String msgid = intent.getStringExtra(MESSAGE_ID);
        if (mode == MESSAGE_MODE_CHAT) sendChat(msgid);
        else if (mode == MESSAGE_MODE_MUC) sendMuc(msgid);
        else if(mode==MESSAGE_MODE_NORMAL)sendNormal(intent);
    }

    private void sendChat(String msgid) {
        MessageEntity entity = MessageEntityImpl.getInstance().selectRow(msgid);
        sendChat(entity);
    }

    private void sendChat(MessageEntity entity) {
        if (entity == null || entity.getId() == null || entity.getId().equals("")) return;
        new ChatHandler().onProcess(entity);
    }

    private void sendMuc(String msgid) {


    }

    private void sendMuc(MessageEntity entity) {
    }

    public void resendMessage() {
        List<MessageEntity> list = MessageEntityImpl.getInstance().selectSending();
        for (MessageEntity entity : list) {
            if (entity.getMode() == MessageEntity.MODE_CHAT) sendChat(entity);
            else if (entity.getMode() == MessageEntity.MODE_GROUP) sendMuc(entity);
        }
    }

    private void updateMessageState(String msgId, int state) {
        MessageEntityImpl.getInstance().updateState(msgId, state);
        Intent intent = new Intent(ActionFlag.ACTION_MESSAGE);
        intent.putExtra("msgid", msgId);
        intent.putExtra("state", state);
        imService.sendBroadcast(intent);
    }

    private void sendNormal(Intent data){
        new NormalHandler().onProcess(data);
    }


    /**
     * 单聊处理
     */
    private class ChatHandler {
        public void onProcess(MessageEntity entity) {
            if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE)) {
                //GIF图片
                doSend(entity.getId(), entity.getMsg(), entity.getUid());
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_ENVELOPE)) {
                //红包
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_EXPRESSION))
                doSend(entity.getId(), entity.getMsg(), entity.getUid());
            else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFT)) {
                //礼物
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIVEMONEY)) {
                //转账
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_IMAGE)) {
                //图片
                sendImage(entity.getId(), entity.getMsg(), entity.getUid());
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_LOCATION)) {
                //位置
                doSend(entity.getId(), entity.getMsg(), entity.getUid());
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_VIDEO)) {
                //视频
                sendVideo(entity.getId(),entity.getMsg(),entity.getUid());
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_VOICE)) {
                //语音
                doSend(entity.getId(), entity.getMsg(), entity.getUid());
            } else doSend(entity.getId(), entity.getMsg(), entity.getUid());
        }
        //发送视频
        private void sendVideo(String msgid,String msg,int fid){

            String[] arrString = msg.split("]");
            String flag = arrString[0];
            String time = arrString[1].substring(1);
            String base64=arrString[2].substring(1);
            String local = arrString[3].substring(1);
            Map<String,String> map = new HashMap<>();
            map.put("id",String.valueOf(imService.getUserid()));
            map.put("ext",local.substring(local.lastIndexOf(".")));
            new UploadHandler(LinkServer.FileAction.URL_FILE_VIDEO_UPLOAD,map,local){

                @Override
                protected void onResult(AjaxResult ajaxResult) {
                    System.out.println("结束上传视频");
                    if(ajaxResult==null||!ajaxResult.isSuccess()){
                        updateMessageState(msgid, MessageEntity.STATE_SEND_FAILED);
                        return;
                    }
                    String serverpath = ajaxResult.getMsg();
                    String resultMessage =flag+"]["+time+"]["+base64+"]["+serverpath+"]";
                    MessageEntityImpl.getInstance().updateMessage(msgid, resultMessage);
                    doSend(msgid, resultMessage, fid);
                }
            }.run();
        }
        //发送图片
        private void sendImage(String msgid, String msg, int fid) {
            String[] arrString = msg.split("]");
            String flag = arrString[0];
            String base64 = arrString[1].substring(1);
            String local = arrString[2].substring(1);
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(imService.getUserid()));
            map.put("ext",local.substring(local.lastIndexOf(".")));
            new UploadHandler(LinkServer.FileAction.URL_FILE_IMAGE_UPLOAD, map, local) {
                @Override
                protected void onResult(AjaxResult ajaxResult) {
                    if (ajaxResult == null || !ajaxResult.isSuccess()) {
                        updateMessageState(msgid, MessageEntity.STATE_SEND_FAILED);
                        return;
                    }
                    String serverpath = ajaxResult.getMsg();
                    String resultMessage = flag + "][" + base64 + "][" + serverpath + "]";
                    MessageEntityImpl.getInstance().updateMessage(msgid, resultMessage);
                    doSend(msgid, resultMessage, fid);
                }
            }.run();

        }


        public void doSend(String msgId, String body, int fid) {
            if (!send(msgId, body, fid)) send(msgId, body, fid);
        }

        public boolean send(String msgId, String body, int fid) {
            try {
                Message message = new Message();
                message.setBody(body);
                message.setTo(JidCreate.bareFrom(imService.getFullName(String.valueOf(fid))));
                message.setType(Message.Type.chat);
                message.setStanzaId(msgId);
                message.setSubject(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
                imService.getConnection().sendStanza(message);
                return true;
            } catch (XmppStringprepException e) {
                e.printStackTrace();
                return false;
            } catch (SmackException.NotConnectedException e) {
                return false;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    /**
     * 通知类处理
     */
    private class NormalHandler{

        public void onProcess(Intent data){
            String msg = data.getStringExtra("msg");
            if(msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE))sendToAllFriend(msg,imService.getUserid());//向所有发送通知
        }

        /**
         * 向所有好友发送通知
         * @param msg
         * @param userid
         */
        private void sendToAllFriend(String msg,int userid){
            Map<String,String> map = new HashMap<>();
            map.put("userid",userid+"");
            map.put("msg",msg);
            new HttpPostHandler(BaseApplication.getInstance(), LinkServer.BroadcastAction.URL_BROADCAST_SEND_ALL_FRIEND, map,false,"") {
                @Override
                protected void onResult(String r) {

                }
            };
        }
    }
}
