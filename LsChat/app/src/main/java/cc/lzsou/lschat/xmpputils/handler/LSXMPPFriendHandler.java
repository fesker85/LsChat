package cc.lzsou.lschat.xmpputils.handler;


import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;

/**
 * 好友处理
 */
public class LSXMPPFriendHandler {

//    public static LSXMPPFriendHandler instance;
//    public static LSXMPPFriendHandler getInstance(){
//        if(instance==null)instance=new LSXMPPFriendHandler();
//        return instance;
//    }
//
//    //更新好友信息
//    public void onUpdate(String msg){
//        String[] arrString = msg.split("]");
//        String friendid = arrString[1].substring(1);
//        String userid = MemberBuss.getInstance().selectMemberId();
//        Map<String,String> map= new HashMap<>();
//        map.put("userid",userid);
//        map.put("friendid",friendid);
//        new HttpPostHandler(null, LsLinkAPIManger.getInstance().getFriendInfoAction(), map, false, "") {
//            @Override
//            protected void onResult(String r) {
//                FriendEntity temp = JsonHelper.jsonToObject(r,FriendEntity.class);
//                if(temp!=null&&temp.getUserid()!=null&&!temp.getUserid().equals("")
//                        &&temp.getFriendid()!=null&&!temp.getFriendid().equals("")){
//                    FriendBuss.getInstance().updateRow(temp);
//                    refreshUI();
//                }
//            }
//        };
//    }
//
//    //同意好友申请
//    public void onAgree(String msg) {
//        //[EAC0BE58AA7A44F59F1675B5E6AAGREE,]["+from+"]["+to+"][通过了朋友验证]
//        String[] arrString = msg.split("]");
//        String friendid = arrString[1].substring(1);
//        String memberid = arrString[2].substring(1);
//        FriendEntity temp = NewFriendBuss.getInstane().selectRow(memberid, friendid);
//        FriendEntity friendEntity = new FriendEntity();
//        friendEntity.setNick(temp.getNick());
//        friendEntity.setFriendid(friendid);
//        friendEntity.setUserid(memberid);
//        friendEntity.setUpdatetime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
//        friendEntity.setCreatetime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
//        friendEntity.setAvatar(temp.getAvatar());
//        friendEntity.setRemark(temp.getRemark());
//        FriendBuss.getInstance().insertRow(friendEntity);
//        NewFriendBuss.getInstane().updateDeal(FriendEntity.DEAL_PASSED, memberid, friendid);
//        refreshUI();
//    }
//
//    //收到添加好友申请
//    public void onAdd(final String msg) {
//        //[EAC0BE58AA7A44F59F1675B5E6AE7ADD,][from][to][applymsg][from对to的remark]
//        String[] arrString = msg.split("]");
//        final String friendid = arrString[1].substring(1);
//        final String memberid = arrString[2].substring(1);
//        final String applymsg = arrString[3].substring(1);
//        final String remark = arrString[4].substring(1);
//        final Context context = BaseApplication.getInstance();
//        Map<String, String> map = new HashMap<>();
//        map.put("id", friendid);
//        new HttpPostHandler(context, LsLinkAPIManger.getInstance().getMemberInfoAction(), map) {
//            @Override
//            protected void onResult(String result) {
//                MemberEntity memberEntity = JsonHelper.jsonToObject(result, MemberEntity.class);
//                if (memberEntity == null || memberEntity.getId() == null || memberEntity.getId().equals(""))
//                    return;
//                FriendEntity friendEntity = new FriendEntity();
//                friendEntity.setRemark(remark);
//                friendEntity.setVerfirymsg(applymsg);
//                friendEntity.setUserid(memberid);
//                friendEntity.setFriendid(friendid);
//                friendEntity.setAvatar(memberEntity.getAvatar());
//                friendEntity.setNick(memberEntity.getNickname());
//                friendEntity.setSex(memberEntity.getSex());
//                friendEntity.setUsername(memberEntity.getUsername());
//                friendEntity.setMobile(memberEntity.getMobile());
//                friendEntity.setRegion(memberEntity.getRegion());
//                friendEntity.setCreatetime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
//                friendEntity.setUpdatetime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
//                friendEntity.setIsdeal(FriendEntity.DEAL_NORMAL);
//                NewFriendBuss.getInstane().insertRow(friendEntity);
//                refreshUI();
//            }
//        };
//    }
//
//    //好友申请拒绝
//    public void onRefuse(String msg){
//
//    }
//
//    //更新UI 发送广播
//    private void refreshUI(){
//       BaseApplication.getInstance().sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
//    }

}
