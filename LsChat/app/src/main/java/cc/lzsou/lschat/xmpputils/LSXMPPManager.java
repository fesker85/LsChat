package cc.lzsou.lschat.xmpputils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import org.minidns.record.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cc.lzsou.lschat.base.AppEntity;


public class LSXMPPManager {

//    private Context context;
//    private ConnectivityManager connManager = null;
//
//    private LSMessageModuleHandler messageHandler;
//    private LSPresenceModuleHandler presenceHandler;
//    private LSMucModuleHandler mucHandler;
//
//    private String userid;
//
//    public String getUserid() {
//        return userid;
//    }
//
//    public void setUserid(String userid) {
//        //this.userid = userid;
//    }
//
//    private final Jaxmpp client = new Jaxmpp();
//    private static LSXMPPManager instance;
//
//    private int usedNetworkType;
//    ;
//    private final Executor taskExecutor = Executors.newFixedThreadPool(1);
//
//
//    public LSXMPPManager(Context context) {
//        this.context = context;
//        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        userid = MemberDatabaseHelper.getInstance().selectMemberId();
//    }
//
//    public static LSXMPPManager getInstance(Context context) {
//        if (instance == null) instance = new LSXMPPManager(context);
//        return instance;
//    }
//
//    public Jaxmpp getClient() {
//        removeHandler();
//        try {
//            tigase.jaxmpp.j2se.Presence.initialize(client);
//            tigase.jaxmpp.j2se.Roster.initialize(client);
//            client.getConnectionConfiguration().setUserJID(getFullName(userid));
//            client.getConnectionConfiguration().setUserPassword(userid);
//            client.getConnectionConfiguration().setDisableTLS(true);
//            client.getConnectionConfiguration().setPort(AppEntity.chat_ServerPort);
//            client.getConnectionConfiguration().setServer(AppEntity.chat_ServerDomain);
//            client.getConnectionConfiguration().setResource(AppEntity.chat_ResourceName);
//            client.getProperties().setUserProperty(Connector.EXTERNAL_KEEPALIVE_KEY, true);
//            client.getProperties().setUserProperty(SessionObject.USER_BARE_JID, BareJID.bareJIDInstance(getFullName(userid)));
//            client.getProperties().setUserProperty(SessionObject.PASSWORD, userid);
//            client.getModulesManager().register(new MessageModule());//注册信息类
//            client.getModulesManager().register(new InBandRegistrationModule());//注册 用户注册类
//            client.getModulesManager().register(new MucModule());//注册群聊类
//            client.getModulesManager().register(new VCardModule());
//            client.getModulesManager().register(new AdHocCommansModule());
//            client.getModulesManager().register(new PushNotificationModule());
//            client.getModulesManager().register(new MessageArchivingModule());
//            client.getModulesManager().register(new MessageArchiveManagementModule());
//            client.getModulesManager().register(new HttpFileUploadModule());
//            client.getModulesManager().register(new PingModule());
//            client.getModulesManager().register(new EntityTimeModule());
//            client.getModulesManager().register(new CapabilitiesModule());
//            client.getModulesManager().register(new MessageCarbonsModule());
//            //消息监听
//            client.getEventBus().addHandler(MessageModule.MessageReceivedHandler.MessageReceivedEvent.class, messageHandler);
//            client.getEventBus().addHandler(MessageModule.ChatUpdatedHandler.ChatUpdatedEvent.class, messageHandler);
//            client.getEventBus().addHandler(MessageModule.ChatCreatedHandler.ChatCreatedEvent.class, messageHandler);
//            client.getEventBus().addHandler(MessageModule.ChatClosedHandler.ChatClosedEvent.class, messageHandler);
//            //联系人监听
//            client.getEventBus().addHandler(PresenceModule.ContactUnsubscribedHandler.ContactUnsubscribedEvent.class, presenceHandler);
//            client.getEventBus().addHandler(PresenceModule.SubscribeRequestHandler.SubscribeRequestEvent.class, presenceHandler);
//            client.getEventBus().addHandler(PresenceModule.ContactUnavailableHandler.ContactUnavailableEvent.class, presenceHandler);
//            client.getEventBus().addHandler(PresenceModule.ContactAvailableHandler.ContactAvailableEvent.class, presenceHandler);
//            client.getEventBus().addHandler(PresenceModule.BeforePresenceSendHandler.BeforePresenceSendEvent.class, presenceHandler);
//            //群聊消息监听
//            client.getEventBus().addHandler(MucModule.OccupantLeavedHandler.OccupantLeavedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.OccupantComesHandler.OccupantComesEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.OccupantChangedPresenceHandler.OccupantChangedPresenceEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.OccupantChangedNickHandler.OccupantChangedNickEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.NewRoomCreatedHandler.NewRoomCreatedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.MessageErrorHandler.MessageErrorEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.JoinRequestedHandler.JoinRequestedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.InvitationReceivedHandler.InvitationReceivedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.InvitationDeclinedHandler.InvitationDeclinedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.MucMessageReceivedHandler.MucMessageReceivedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.PresenceErrorHandler.PresenceErrorEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.RoomClosedHandler.RoomClosedEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.StateChangeHandler.StateChangeEvent.class, mucHandler);
//            client.getEventBus().addHandler(MucModule.YouJoinedHandler.YouJoinedEvent.class, mucHandler);
//            client.keepalive();
//
//        } catch (JaxmppException e) {
//            e.printStackTrace();
//        }
//        return client;
//    }
//
//    public int getUsedNetworkType() {
//        return this.usedNetworkType;
//    }
//
//    public void setUsedNetworkType(int type) {
//        this.usedNetworkType = type;
//    }
//
//    public void connectJaxmpp() {
//        setUsedNetworkType(getActiveNetworkType());
//        int tmpNetworkType = getUsedNetworkType();
//        if (tmpNetworkType != -1) {
//            taskExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if (getClient().isConnected()) {
//                            return;
//                        }
//                        final Connector.State state = getClient().getSessionObject().getProperty(Connector.CONNECTOR_STAGE_KEY);
//                        if (state != null && state != Connector.State.disconnected) {
//                            return;
//                        }
//                        System.out.println("使用用户id====" + userid);
//                        getClient().login(false);
//                    } catch (JaxmppException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    }
//
//    public int getActiveNetworkType() {
//        NetworkInfo info = connManager.getActiveNetworkInfo();
//        if (info == null) {
//            return -1;
//        }
//        if (!info.isConnected()) {
//            return -1;
//        }
//        return info.getType();
//    }
//
//    public void disconnectJaxmpp() {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (client.isConnected()) {
//                        removeHandler();
//                        client.disconnect(false);
//                    }
//                } catch (Exception e) {
//                }
//
//            }
//        });
//    }
//
//    public void removeHandler() {
//        if (messageHandler == null) messageHandler = new LSMessageModuleHandler();
//        if (client != null) client.getEventBus().remove(messageHandler);
//
//        if (presenceHandler == null) presenceHandler = new LSPresenceModuleHandler();
//        if (client != null) client.getEventBus().remove(presenceHandler);
//
//        if (mucHandler == null) mucHandler = new LSMucModuleHandler();
//        if (client != null) client.getEventBus().remove(mucHandler);
//    }
//
//    public void processPresenceUpdate() {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (!client.isConnected()) {
//                        connectJaxmpp();
//                    } else {
//                        client.getModule(PresenceModule.class).sendInitialPresence();
//                    }
//                } catch (JaxmppException e) {
//                }
//            }
//        });
//    }
//
//    public void keepAlive() {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (client.isConnected()) {
//                        client.getConnector().keepalive();
//                    } else if (Connector.State.disconnecting == client.getSessionObject().getProperty(Connector.CONNECTOR_STAGE_KEY)) {
//                        final Date x = client.getSessionObject().getProperty(Connector.CONNECTOR_STAGE_TIMESTAMP_KEY);
//                        if (x != null && x.getTime() < System.currentTimeMillis() - 45 * 1000) {
//                            client.getConnector().stop(true);
//                        }
//                    }
//                } catch (JaxmppException ex) {
//                }
//            }
//        });
//    }
//
//    public void sendAcks() {
//        taskExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                if (client.isConnected()) {
//                    try {
//                        client.getModule(StreamManagementModule.class).sendAck();
//                    } catch (JaxmppException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//
//    private String getFullName(String userid) {
//        return userid + "@" + AppEntity.chat_ServerDomain;
//    }

//    private static LSXMPPManager instance;
//    private static AbstractXMPPConnection connection;
//    private LSXMPPConnecionListener connecionListener;
//    private LSXMPPSyncStanzaListener syncStanzaListener;
//    private LSXMPPStanzaInterceptor stanzaInterceptor;
//    private LSXMPPStanzaFilter stanzaFilter;
//    private LSXMPPRosterListener rosterListener;
//
//    private boolean isAuthenticated = false;//判断登录状态
//    private boolean isConnectioned = false;//判断连接状态
//
//
//
//    public boolean isAuthenticated() {
//        return isAuthenticated;
//    }
//
//    public void setAuthenticated(boolean authenticated) {
//        isAuthenticated = authenticated;
//    }
//
//    public boolean isConnectioned() {
//        return isConnectioned;
//    }
//
//    public void setConnectioned(boolean connectioned) {
//        isConnectioned = connectioned;
//    }
//
//    public LSXMPPManager() {
//        connecionListener = new LSXMPPConnecionListener(this);
//        syncStanzaListener = new LSXMPPSyncStanzaListener();
//        stanzaInterceptor = new LSXMPPStanzaInterceptor();
//        stanzaFilter = new LSXMPPStanzaFilter();
//        rosterListener=new LSXMPPRosterListener();
//
//    }
//
//    public AbstractXMPPConnection getConnection() {
//        return connection;
//    }
//
//    public static LSXMPPManager getInstance() {
//        if (instance == null) instance = new LSXMPPManager();
//        return instance;
//    }
//
//    static {
//        try {
//            Class.forName("org.jivesoftware.smack.ReconnectionManager");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void init(String memberid) {
//        close();
//        isAuthenticated = false;
//        isConnectioned = false;
//        SmackConfiguration.DEBUG = true;
//        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
//        try {
//            //设置连接超时的最大时间
//            builder.setConnectTimeout(10 * 1000);
//            builder.setResource(AppEntity.chat_ResourceName);
//            //设置服务器名称
//            builder.setXmppDomain(AppEntity.chat_ServerDomain);
//            //设置用户帐号
//            builder.setUsernameAndPassword(memberid,memberid);
//            //设置主机IP地址
//            builder.setHostAddress(InetAddress.getByName(AppEntity.chat_serverIpAddr));//服务器ip 地址
//            //设置主机地址
//            // builder.setHost(AppEntity.chat_ServerHost);
//            //设置端口号
//            builder.setPort(AppEntity.chat_ServerPort);
//            //设置安全模式 禁用SSL连接
//            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
//            //设置离线状态
//            //builder.setSendPresence(false);
//            //设置开启压缩，可以节省流量
//            builder.setCompressionEnabled(true);
//            //需要经过同意才可以添加好友
//            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
//            connection = new XMPPTCPConnection(builder.build());
//
//
//            connection.setFromMode(XMPPConnection.FromMode.USER);
//
//            connection.addConnectionListener(connecionListener);
//            connection.addSyncStanzaListener(syncStanzaListener, stanzaFilter);
//            connection.addStanzaInterceptor(stanzaInterceptor, stanzaFilter);
//
//            DeliveryReceiptManager.getInstanceFor(connection).
//                    setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
//            DeliveryReceiptManager.getInstanceFor(connection).dontAutoAddDeliveryReceiptRequests();
//
//            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
//            reconnectionManager.setFixedDelay(2);
//            reconnectionManager.setReconnectionPolicy(ReconnectionManager.ReconnectionPolicy.FIXED_DELAY);
//            reconnectionManager.enableAutomaticReconnection();
//            reconnectionManager.addReconnectionListener(new LSXMPPReconnectionListener());
//
//            PingManager pingManager = PingManager.getInstanceFor(connection);
//            pingManager.setPingInterval(30);
//            pingManager.registerPingFailedListener(new LSXMPPPingFailedListener());
//            //好友在线状态监听
//            Roster.getInstanceFor(connection).removeRosterListener(rosterListener);
//            Roster.getInstanceFor(connection).addRosterListener(rosterListener);
//
//
//        } catch (XmppStringprepException e) {
//            e.printStackTrace();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public boolean open() {
//        System.out.println("打开连接");
//        try {
//            connection.connect();
//            return true;
//        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
//            return false;
//        }
//    }
//
//    public void close() {
//        System.out.println("关闭以前的连接");
//        if (connection != null) {
//            connection.removeConnectionListener(connecionListener);
//            connection.removeAsyncStanzaListener(syncStanzaListener);
//            connection.removeStanzaInterceptor(stanzaInterceptor);
//            try {
//                connection.disconnect();
//            } finally {
//                connection = null;
//            }
//        }
//    }
//
//    /**
//     * 登录
//     *
//     * @param memberid
//     * @return
//     */
//    public boolean login(String memberid) {
//        try {
//            init(memberid);
//            open();
//            connection.login();
//            return true;
//        } catch (XMPPException | SmackException | IOException | InterruptedException e) {
//            return false;
//        }
//    }
//
//
//    /**
//     * 更改用户状态
//     */
//    public void setPresence(int code) {
//        XMPPConnection con = connection;
//        if (con == null) return;
//        Presence presence;
//        try {
//            switch (code) {
//                case 0:
//                    presence = new Presence(Presence.Type.available);
//                    con.sendStanza(presence);
//                    Log.v("state", "设置在线");
//                    break;
//                case 1:
//                    presence = new Presence(Presence.Type.available);
//                    presence.setMode(Presence.Mode.chat);
//                    con.sendStanza(presence);
//                    Log.v("state", "设置Q我吧");
//                    break;
//                case 2:
//                    presence = new Presence(Presence.Type.available);
//                    presence.setMode(Presence.Mode.dnd);
//                    con.sendStanza(presence);
//                    Log.v("state", "设置忙碌");
//                    break;
//                case 3:
//                    presence = new Presence(Presence.Type.available);
//                    presence.setMode(Presence.Mode.away);
//                    con.sendStanza(presence);
//                    Log.v("state", "设置离开");
//                    break;
//                case 4:
////                    Roster roster = con.getRoster();
////                    Collection<RosterEntry> entries = roster.getEntries();
////                    for (RosterEntry entry : entries) {
////                        presence = new Presence(Presence.Type.unavailable);
////                        presence.setPacketID(Packet.ID_NOT_AVAILABLE);
////                        presence.setFrom(con.getUser());
////                        presence.setTo(entry.getUser());
////                        con.sendPacket(presence);
////                        Log.v("state", presence.toXML());
////                    }
////                    // 向同一用户的其他客户端发送隐身状态
////                    presence = new Presence(Presence.Type.unavailable);
////                    presence.setPacketID(Packet.ID_NOT_AVAILABLE);
////                    presence.setFrom(con.getUser());
////                    presence.setTo(StringUtils.parseBareAddress(con.getUser()));
////                    con.sendStanza(presence);
////                    Log.v("state", "设置隐身");
//                    break;
//                case 5:
//                    presence = new Presence(Presence.Type.unavailable);
//                    con.sendStanza(presence);
//                    Log.v("state", "设置离线");
//                    break;
//                default:
//                    break;
//            }
//        } catch (SmackException.NotConnectedException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


//    private String getFullUserName(String username) {
//        return username + "@" + AppEntity.chat_serverIpAddr;
//    }

//
//
//
//
//    /**
//     * 获取离线消息
//     *
//     * @return
//     */
//    public Map<String, List<HashMap<String, String>>> getHisMessage() {
//        if (connection == null) return null;
//        Map<String, List<HashMap<String, String>>> offlineMsgs = null;
//
//        try {
//            OfflineMessageManager offlineManager = new OfflineMessageManager(connection);
//            List<Message> messageList = offlineManager.getMessages();
//
//            int count = offlineManager.getMessageCount();
//            if (count <= 0) return null;
//            offlineMsgs = new HashMap<>();
//
//            for (Message message : messageList) {
//                String fromUser = message.getFrom().toString();
//                HashMap<String, String> history = new HashMap<>();
//                history.put("useraccount", connection.getUser().asEntityBareJidString());
//                history.put("friendaccount", fromUser);
//                history.put("info", message.getBody());
//                history.put("type", "left");
//                if (offlineMsgs.containsKey(fromUser)) {
//                    offlineMsgs.get(fromUser).add(history);
//                } else {
//                    List<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
//                    temp.add(history);
//                    offlineMsgs.put(fromUser, temp);
//                }
//            }
//            offlineManager.deleteMessages();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return offlineMsgs;
//    }
//
//    /**
//     * 判断OpenFire用户的状态 strUrl :
//     * url格式 - http://my.openfire.com:9090/plugins/presence
//     * /status?jid=user1@SERVER_NAME&type=xml
//     * 返回值 : 0 - 用户不存在; 1 - 用户在线; 2 - 用户离线
//     * 说明 ：必须要求 OpenFire加载 presence 插件，同时设置任何人都可以访问
//     */
//    public int getUserLineState(String user) {
//        String url = "http://" + AppEntity.chat_serverIpAddr + ":9090/plugins/presence/status?" + "jid=" + user + "@" + AppEntity.chat_serverIpAddr + "&type=xml";
//        int shOnLineState = 0; // 不存在
//        try {
//            URL oUrl = new URL(url);
//            URLConnection oConn = oUrl.openConnection();
//            if (oConn != null) {
//                BufferedReader oIn = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
//                String strFlag = oIn.readLine();
//                oIn.close();
//                System.out.println("strFlag" + strFlag);
//                if (strFlag.contains("type=\"unavailable\"")) {
//                    shOnLineState = 2;
//                }
//                if (strFlag.contains("type=\"error\"")) {
//                    shOnLineState = 0;
//                } else if (strFlag.contains("priority") || strFlag.contains("id=\"")) {
//                    shOnLineState = 1;
//                }
//            }
//        } catch (Exception e) {
//            shOnLineState = -1;
//        }
//
//        if (shOnLineState == 1) {
//            setConnectioned(true);
//            setAuthenticated(true);
//        }
//        if (shOnLineState == 2) {
//            setConnectioned(false);
//            setAuthenticated(false);
//        }
//        return shOnLineState;
//    }
}
