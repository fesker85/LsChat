package cc.lzsou.lschat.data.bean;


/**
 * 聊天信息
 */
public class MessageEntity {
    public static final int MODE_CHAT = 0; //单聊
    public static final int MODE_GROUP = 1;//群聊
    public static final int MODE_NOTICE = 2;//通知
    public static final int PATH_FTOM = 0;//接收
    public static final int PATH_TO = 1;//发送

    public static final int STATE_SEND_SENDING=0;//发送 正在发送
    public static final int STATE_SEND_SUCCESS=1;//发送 成功
    public static final int STATE_SEND_FAILED=2;//发送 失败

    public static final int STATE_RECP_NORMAL=3;//接收 未读
    public static final int STATE_RECP_READED=4;//接收 已读


    private String id;
    private int mode;//消息模式
    private String mid;//群里id
    private int uid;//好友或群聊人员id
    private String msg;//消息
    private int path;//消息方向 0 接收 1发送
    private int status;//状态
    private String curtime;//消息时间
    private long rectime;//语音或视频时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurtime() {
        return curtime;
    }

    public void setCurtime(String curtime) {
        this.curtime = curtime;
    }

    public long getRectime() {
        return rectime;
    }

    public void setRectime(long rectime) {
        this.rectime = rectime;
    }

}
