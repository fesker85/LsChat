package cc.lzsou.lschat.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import cc.lzsou.lschat.data.impl.MemberEntityImpl;

public class IMService extends Service implements NetworkReceiver.OnNetworkChanged, ScreenReceiver.OnScreenChanged {
    public static final String CONNECT_SINGLE = "connect-single";
    public static final String SEND_MESSAGE = "message-send";
    public static final String APPEND_FRIENDID="append-friendid";
    public static final String REMOVE_FRIENDID="remove-friendid";

    public static final String SEND_MESSAGE_TO_ALLFRIENDS="message-send-allfriend";

    public static final String XMPP_DOMAIN = "sntianyuan.com";
    public static final int XMPP_PORT = 5222;
    public static final String XMPP_RESOUCE = "LSChat";
    public static final int XMPP_TIMEOUT = 10 * 1000;
    private static final long SELF_INTEVAL = 10 * 1000;

    private final NetworkReceiver networkReceiver = new NetworkReceiver();
    private final ScreenReceiver screenReceiver = new ScreenReceiver();
    private final MessageAccepter accepter = new MessageAccepter(this);
    private final MessageSender sender = new MessageSender(this);
    private int friendId;
    public int getFriendId() {
        return friendId;
    }
    private String groupid;
    public String getGroupid() {
        return groupid;
    }


    private int userid;
    public int getUserid(){
        return userid;
    }

    private AbstractXMPPConnection connection;
    private long LAST_ACCEPT_TIME = 0;//最后接受时间

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private PowerManager.WakeLock mWakeLock;




    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        userid = MemberEntityImpl.getInstance().selectRow().getId();
        networkReceiver.setOnNetworkChanged(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
        screenReceiver.setOnScreenChanged(this);
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction("android.intent.action.SCREEN_OFF");
        intentFilter1.addAction("android.intent.action.SCREEN_ON");
        intentFilter1.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(screenReceiver, intentFilter1);
        registerAlarmManager();
        acquireWakeLock();
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(CONNECT_SINGLE)) {
            connectXmpp();
        }
        if (intent != null && intent.getAction() != null && intent.getAction().equals(SEND_MESSAGE)) {
            sender.sendMessage(intent);
        }
        if(intent!=null&&intent.getAction()!=null&&intent.getAction().equals(APPEND_FRIENDID)){
            this.friendId = intent.getIntExtra("fid",0);
        }
        if(intent!=null&&intent.getAction()!=null&&intent.getAction().equals(REMOVE_FRIENDID)){
            this.friendId = 0;
        }
        if(intent!=null&&intent.getAction()!=null&&intent.getAction().equals(SEND_MESSAGE_TO_ALLFRIENDS)){
            sender.sendMessage(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(networkReceiver);
        unregisterReceiver(screenReceiver);
        releaseWakeLock();
        super.onDestroy();
    }


    public AbstractXMPPConnection getConnection() {
        if (connection == null) {
            try {
                if (userid<1)
                    userid = MemberEntityImpl.getInstance().selectRow().getId();

                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder().
                        setUsernameAndPassword(getFullName(String.valueOf(userid)), String.valueOf(userid)).
                        setXmppDomain(XMPP_DOMAIN).setPort(XMPP_PORT).
                        setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).
                        setCompressionEnabled(true).
                        setConnectTimeout(XMPP_TIMEOUT).
                        setResource(XMPP_RESOUCE).
                        setSendPresence(true).
                        build();
                SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
                SASLAuthentication.registerSASLMechanism(new SASLPlainMechanism());
                connection = new XMPPTCPConnection(config);
                ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
                reconnectionManager.setFixedDelay(2);
                reconnectionManager.enableAutomaticReconnection();
                connection.addConnectionListener(new ConnectionListener() {
                    @Override
                    public void connected(XMPPConnection connection) {
                        System.out.println("已链接");
                    }

                    @Override
                    public void authenticated(XMPPConnection connection, boolean resumed) {
                        System.out.println("已登录");
                        LAST_ACCEPT_TIME = System.currentTimeMillis();
                        selfHandler.removeCallbacks(selfRunnable);
                        selfHandler.postDelayed(selfRunnable, SELF_INTEVAL);
                        sender.resendMessage();
                    }

                    @Override
                    public void connectionClosed() {
                        System.out.println("已断开");
                    }

                    @Override
                    public void connectionClosedOnError(Exception e) {
                        System.out.println("已断开(Error)");
                    }
                });

                connection.addAsyncStanzaListener(new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
                        if (packet instanceof Message){
                            LAST_ACCEPT_TIME = System.currentTimeMillis();
                            if(((Message)packet).getFrom().toString().split("@")[0].equals(userid)){
                                System.out.println("定时检测发送数据："+LAST_ACCEPT_TIME);
                            }
                        }
                        accepter.onAccept(packet);
                    }
                }, new StanzaFilter() {
                    @Override
                    public boolean accept(Stanza stanza) {
                        return true;
                    }
                });
                connection.addStanzaSendingListener(new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
                        sender.onSendBack(packet);
                    }
                }, new StanzaFilter() {
                    @Override
                    public boolean accept(Stanza stanza) {
                        return true;
                    }
                });

            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }


    public void connectXmpp() {
        if (getConnection() == null) return;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    if(!getConnection().isConnected())
                        getConnection().connect().login();
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public void disconnectXmpp() {
        if (getConnection() != null) getConnection().disconnect();
    }

    public String getFullName(String s) {
        return s + "@" + XMPP_DOMAIN;
    }

    @Override
    public void onConnected() {
        System.out.println("网络已链接，重新登录");
        connectXmpp();
    }

    @Override
    public void onLosted() {
        System.out.println("网络已断开，断开链接");
        disconnectXmpp();
    }

    @Override
    public void onScreenOff() {
        System.out.println("屏幕关闭：");
        acquireWakeLock();
    }

    @Override
    public void onScreenOn() {
        System.out.println("屏幕亮起：");
        releaseWakeLock();
    }


    private Handler selfHandler = new Handler();
    private Runnable selfRunnable = new Runnable() {
        @Override
        public void run() {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        Message message = new Message();
                        message.setBody("1");
                        message.setTo(JidCreate.bareFrom(String.valueOf(userid)));
                        message.setType(Message.Type.headline);
                        getConnection().sendStanza(message);
                        if (System.currentTimeMillis() - LAST_ACCEPT_TIME > SELF_INTEVAL * 2) {
                            System.out.println("大于20秒未收到信息：重新链接");
                            disconnectXmpp();
                            connectXmpp();
                        }
                    } catch (SmackException.NotConnectedException e) {
                        disconnectXmpp();
                        connectXmpp();
                        System.out.println("发送失败：" + e.getMessage());
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (XmppStringprepException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
            selfHandler.postDelayed(selfRunnable, SELF_INTEVAL);

        }
    };


    @SuppressLint("WrongConstant")
    public void registerAlarmManager() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, IMService.class), Intent.FLAG_ACTIVITY_NEW_TASK);
        long now = System.currentTimeMillis();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now, 5000, pendingIntent);
    }

    //申请设备电源锁
    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lock");
        }
        mWakeLock.acquire();
        setWifiDormancy(this);
    }

    private void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mWakeLock = null;
        restoreWifiDormancy(this);
    }

    public void setWifiDormancy(Context context) {
        int value = Settings.System.getInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("WIFI_SLEEP_POLICY", value);
        editor.commit();

        if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_NEVER);
        }
    }

    public void restoreWifiDormancy(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("wifi_sleep_policy", Context.MODE_PRIVATE);
        int defaultPolicy = prefs.getInt("WIFI_SLEEP_POLICY", Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        Settings.System.putInt(context.getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, defaultPolicy);
    }

    public class LocalBinder extends Binder {
        public IMService getService() {
            return IMService.this;
        }
    }

}
