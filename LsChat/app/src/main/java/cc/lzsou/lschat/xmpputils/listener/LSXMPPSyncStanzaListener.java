package cc.lzsou.lschat.xmpputils.listener;

//接收监视器
public class LSXMPPSyncStanzaListener
//        implements StanzaListener
{

//    @Override
//    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
//        if (packet instanceof Message) {//表示接收到是消息包
//            Message message = (Message) packet;
//            if (message.getBody() == null) return;
//            if (message.getType() == Message.Type.chat)
//                LSXMPPChatHandler.getInstance().onProcess(message); //单聊处理
//
//            if (message.getType() == Message.Type.groupchat)
//                LSXMPPGroupChatHandler.getInstance().onProcess(message);//群聊处理
//
//            if (message.getType() == Message.Type.error) {//表示错误信息
//                System.out.println("收到error：Stanza->" + message.getFrom() + ":" + message.getBody());
//            }
//            if (message.getType() == Message.Type.headline) { //离线信息
//                System.out.println("收到headline：Stanza->" + message.getFrom() + ":" + message.getBody());
//            }
//            if (message.getType() == Message.Type.normal) { //广播
//                String msg = message.getBody();
//
//                System.out.println("收到广播:->" + message.getFrom() + ":" + message.getBody());
//
//                if (msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_ADD))
//                    LSXMPPFriendHandler.getInstance().onAdd(msg);  //收到好友添加申请
//
//                if (msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_AGREE))
//                    LSXMPPFriendHandler.getInstance().onAgree(msg); //收到好友同意申请
//
//                if (msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE))
//                    LSXMPPFriendHandler.instance.onUpdate(msg);//好友信息更改
//
//                if (msg.contains(MessageFlag.MESSAGE_FLAG_FRIEND_REFUSE))
//                    LSXMPPFriendHandler.instance.onRefuse(msg);//拒绝添加好友
//
//
//
//                if (msg.contains(MessageFlag.MESSAGE_FLAG_NOTICE)) {
//                    //通知
//                } else {
//                    //消息
//                }
//            }
//        }
//    }


}
