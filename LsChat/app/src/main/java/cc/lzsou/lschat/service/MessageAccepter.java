package cc.lzsou.lschat.service;

import android.content.Intent;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;


import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.core.handler.DownLoadHandler;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.MessageEntityImpl;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.lschat.data.impl.TempMessageEntityImpl;
import cc.lzsou.lschat.main.activity.MainActivity;
import cc.lzsou.lschat.manager.AppStatusManager;
import cc.lzsou.lschat.manager.NoticeManager;
import cc.lzsou.lschat.views.expression.ExpressionUtil;
import cc.lzsou.servercore.LinkServer;

public class MessageAccepter {

    private IMService imService;

    public MessageAccepter(IMService imService) {
        this.imService = imService;
    }

    public void onAccept(Stanza packet) {
        if (packet instanceof Message) {
            Message message = (Message) packet;
            //System.out.println("收到信息："+ message.getFrom()+"\r\n"+message.getType()+"\r\n"+message.getBody());
            if(message.getType()!=Message.Type.error){
                System.out.println("收到信息："+ JsonHelper.objectToJson(message));
            }
            if (message.getType() == Message.Type.chat) new ChatHandler().onProcess(message);
            if(message.getType()==Message.Type.normal) new NormalHandler().onProcess(message);

        }
    }

    //通知
    private class NormalHandler{
        public void onProcess(Message message){
            String msg = message.getBody();
            if(msg==null||message.equals(""))return;
            if(msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_ADD))onFriendAdd(msg);//添加好友
            if(msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_AGREE))onFriendAgree(msg);// 好友同意
            if(msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE))onFriendInfoChanged(msg);//好友信息变更
        }

        /**
         * 好友信息变更
         * @param msg
         */
        private void onFriendInfoChanged(String msg){
            String[] arrString = msg.split("]");
            String friendid = arrString[1].substring(1);
            Map<String,String> map = new HashMap<>();
            map.put("userid",""+imService.getUserid());
            map.put("friendid",friendid);

            new HttpPostHandler(imService,LinkServer.FriendAction.URL_FRIEND_INFO, map) {
                @Override
                protected void onResult(String r) {
                    FriendEntity entity = JsonHelper.jsonToObject(r,FriendEntity.class);
                    if(entity!=null&&entity.getId()>0){
                        FriendEntityImpl.getInstance().updateRow(entity);
                        imService.sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
                    }
                }
            };

        }
        //收到好友申请同意
        private void onFriendAgree(String msg){
           // [EAC0BE58AA7A44F59F1675B5E6AAGREE,][6][5][通过了朋友验证]
            String[] arrString = msg.split("]");
            int fid = Integer.parseInt(arrString[1].substring(1));
            new AsynHandler() {
                @Override
                protected Object onProcess() {
                    Map<String,String> map = new HashMap<>();
                    map.put("id",String.valueOf(fid));
                    String result = HttpHelper.doPost(LinkServer.MemberAction.URL_MEMBER_INFO,map);
                    MemberEntity memberEntity = JsonHelper.jsonToObject(result,MemberEntity.class);
                    if(memberEntity!=null&&memberEntity.getId()>0){
                        FriendEntity entity = new FriendEntity();
                        entity.setExid("");
                        entity.setId(memberEntity.getId());
                        entity.setUsername(memberEntity.getUsername());
                        entity.setAddress(memberEntity.getAddress());
                        entity.setAvatar(memberEntity.getAvatar());
                        entity.setEmail(memberEntity.getEmail());
                        entity.setIdnumber(memberEntity.getIdnumber());
                        entity.setJobunit(memberEntity.getJobunit());
                        entity.setMobile(memberEntity.getMobile());
                        entity.setMode(FriendEntity.MODE_PERSION);
                        entity.setName(memberEntity.getName());
                        entity.setNickname(memberEntity.getNickname());
                        entity.setPhone(memberEntity.getPhone());
                        entity.setPlatid(memberEntity.getPlatid());
                        entity.setRegion(memberEntity.getRegion());
                        entity.setRegionid(memberEntity.getRegionid());
                        entity.setRelation(FriendEntity.RELATION_FRIEND_YES);
                        entity.setRemark(memberEntity.getNickname());
                        entity.setSex(memberEntity.getSex());
                        entity.setVip(memberEntity.getVip());
                        entity.setVipexpire(memberEntity.getVipexpire());
                        return entity;
                    }
                    return null;
                }
                @Override
                protected void onResult(Object object) {
                    FriendEntity entity = (FriendEntity)object;
                    System.out.println("好友信息同构： "+JsonHelper.objectToJson(entity));
                    if(entity!=null&&entity.getId()>0){
                        FriendEntityImpl.getInstance().insertRow(entity);
                        MessageEntity messageEntity = new MessageEntity();
                        messageEntity.setRectime(0);
                        messageEntity.setCurtime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
                        messageEntity.setMsg(MessageFlag.MESSAGE_FLAG_FRIENDAGREE+"]["+entity.getRemark()+"已通过了您的朋友申请]");
                        messageEntity.setMid("chat"+entity.getId());
                        messageEntity.setId(Common.getId());
                        messageEntity.setMode(MessageEntity.MODE_CHAT);
                        messageEntity.setPath(MessageEntity.PATH_FTOM);
                        messageEntity.setStatus(MessageEntity.STATE_SEND_SUCCESS);
                        messageEntity.setUid(entity.getId());

                        //给自己发信息 说明我已添加好友
                        MessageEntityImpl.getInstance().insertRow(messageEntity);
                        //发送通知
                        Intent intent = new Intent(ShowNotificationReceiver.ACTION_SHOWNOTIFICATOIN);
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FRIEND,entity.getId());
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_TITLE,imService.getString(R.string.app_name));
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CONTENT,entity.getRemark()+"已通过了您的朋友申请");
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FLAG,ShowNotificationReceiver.FLAG_CHAT);
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS, NoticeManager.NOTIFICATION_SOUND);
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                        imService.sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
                        imService.sendBroadcast(intent);
                        //发送好友添加成功通知
                        imService.sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
                    }
                }
            };

        }
        //收到好友申请
        private void onFriendAdd(String msg){
            //[EAC0BE58AA7A44F59F1675B5E6AE7ADD,][6][5][我是Fesker][白藤] 标记 对方id 我的id 验证信息 对方对我的备注
            String[] arrString = msg.split("]");
            int fid = Integer.parseInt(arrString[1].substring(1));
            String verfiyMsg = arrString[3].substring(1);
            new AsynHandler() {
                @Override
                protected Object onProcess() {
                    Map<String,String> map = new HashMap<>();
                    map.put("id",String.valueOf(fid));
                    String result = HttpHelper.doPost(LinkServer.MemberAction.URL_MEMBER_INFO,map);
                    MemberEntity memberEntity = JsonHelper.jsonToObject(result,MemberEntity.class);
                    if(memberEntity!=null&&memberEntity.getId()>0){
                      TempFriendEntity entity = new TempFriendEntity();
                        entity.setUsername(memberEntity.getUsername());
                        entity.setAddress(memberEntity.getAddress());
                        entity.setAvatar(memberEntity.getAvatar());
                        entity.setEmail(memberEntity.getEmail());
                        entity.setId(memberEntity.getId());
                        entity.setIdnumber(memberEntity.getIdnumber());
                        entity.setJobunit(memberEntity.getJobunit());
                        entity.setMobile(memberEntity.getMobile());
                        entity.setName(memberEntity.getName());
                        entity.setNickname(memberEntity.getNickname());
                        entity.setPhone(memberEntity.getPhone());
                        entity.setPlatid(memberEntity.getPlatid());
                        entity.setRegion(memberEntity.getRegion());
                        entity.setRegionid(memberEntity.getRegionid());
                        entity.setStatus(TempFriendEntity.STATUS_NEW);
                        entity.setMsg(verfiyMsg);
                        entity.setRemark("");
                        entity.setSex(memberEntity.getSex());
                        entity.setVip(memberEntity.getVip());
                        entity.setVipexpire(memberEntity.getVipexpire());
                        return entity;
                    }else {
                        return null;
                    }
                }

                @Override
                protected void onResult(Object object) {
                    TempFriendEntity entity = (TempFriendEntity)object;
                    if(entity==null)return;
                    TempFriendEntityImpl.getInstance().insertRow(entity);

                    Intent intent = new Intent(ShowNotificationReceiver.ACTION_SHOWNOTIFICATOIN);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FRIEND,entity.getId());
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_TITLE,imService.getString(R.string.app_name));
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CONTENT,"[朋友申请]"+entity.getNickname()+"："+entity.getMsg());
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FLAG,ShowNotificationReceiver.FLAG_FRIEND_ADD);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_ALL);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,false);
                    imService.sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
                    imService.sendBroadcast(intent);
                }
            };

        }
    }

    //聊天
    private class ChatHandler {
        public void onProcess(Message message) {
            String msgId = message.getStanzaId();
            System.out.println("收到信息来源："+message.getFrom());
            int uid = Integer.parseInt(message.getFrom().toString().split("@")[0]);
            System.out.println("收到信息来源："+uid);
            String time = message.getSubject();
            String msg = message.getBody();

            MessageEntity entity = new MessageEntity();
            entity.setId(msgId);
            entity.setMode(MessageEntity.MODE_CHAT);
            entity.setMid("chat"+uid);
            entity.setUid(uid);
            entity.setMsg(msg);
            entity.setPath(MessageEntity.PATH_FTOM);
            entity.setCurtime(time);
            entity.setStatus(MessageEntity.STATE_RECP_NORMAL);
            if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE)) {
                //GIF图片
                saveText(entity);
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_ENVELOPE)) {
                //红包
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_EXPRESSION)) {
                //表情
                saveText(entity);
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFT)) {
                //礼物
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIVEMONEY)) {
                //转账
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_IMAGE)) {
                //图片
                saveText(entity);
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_LOCATION)) {
                //位置
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_VIDEO)) {
                //视频
            } else if (entity.getMsg().contains(MessageFlag.MESSAGE_FLAG_VOICE)) {
                String[] arr = entity.getMsg().split("]");
                String t = arr[1].substring(1);
                long l = Long.parseLong(t);
                entity.setRectime(l);
                //语音
                saveText(entity);
            } else {
                //其他
                saveText(entity);
            }
        }

        private void saveText(MessageEntity entity) {
            refreshUI(entity);
        }


        private void refreshUI(MessageEntity entity) {
            MessageEntityImpl.getInstance().insertRow(entity);
            FriendEntity friendEntity = FriendEntityImpl.getInstance().selectRow(entity.getUid());
            String name=friendEntity.getNickname();
            if(friendEntity.getRemark()!=null&&!friendEntity.getRemark().equals(""))
                name = friendEntity.getRemark();

            Intent intent = new Intent(ShowNotificationReceiver.ACTION_SHOWNOTIFICATOIN);
            intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FRIEND,entity.getUid());
            intent.putExtra(ShowNotificationReceiver.INTENT_KEY_TITLE,imService.getString(R.string.app_name));
            intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CONTENT,name+"："+getNoticeContentText(entity.getMsg()));
            intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FLAG,ShowNotificationReceiver.FLAG_CHAT);

            if(AppStatusManager.isForeground(imService,"ChatActivity")){
                if(imService.getFriendId()==entity.getUid()){
                    //震动
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_NONE);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                }
                else {
                    //出通知 声音
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_SOUND);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                    //加新消息
                    TempMessageEntityImpl.getInstance().insertRow(entity);
                }
            }
            else {
                if(AppStatusManager.isForeground(imService, "MainActivity")){
                    //震动
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_VIBRATE);
                    intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                    //加新消息
                    TempMessageEntityImpl.getInstance().insertRow(entity);
                }
                else {
                    if(AppStatusManager.isExsitMianActivity(imService,MainActivity.class)){
                        //出通知 声音
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_SOUND);
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                        //加新消息
                        TempMessageEntityImpl.getInstance().insertRow(entity);
                    }
                    else {
                        //出通知 声音+呼吸灯
                        //加新消息
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS,NoticeManager.NOTIFICATION_ALL);
                        intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,false);
                        TempMessageEntityImpl.getInstance().insertRow(entity);
                    }
                }
            }
            imService.sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
            imService.sendBroadcast(intent);

        }

        private String getNoticeContentText(String msg) {
            if (msg.contains(MessageFlag.MESSAGE_FLAG_IMAGE))//图片
                return "[图片]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_VOICE))//语音
                return "[语音]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE))//动画表情
                return "[动画表情]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_EXPRESSION))//表情
                return ExpressionUtil.getText(imService, msg).toString();
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_LOCATION))//位置
                return "[位置]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_ENVELOPE))//红包
                return "[红包]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_GIVEMONEY))//转账
                return "[转账]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_GIFT))//礼物
                return "[礼物]";
            else if (msg.contains(MessageFlag.MESSAGE_FLAG_VIDEO))//视频
                return "[视频]";
            else return msg;
        }
    }
}
