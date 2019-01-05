package cc.lzsou.lschat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cc.lzsou.lschat.base.BaseApplication;

public class DBMessageHelper extends SQLiteOpenHelper {
    private static final String db_name="lschat.message.db";
    private static final  int db_version = 1;
    private static DBMessageHelper instance;
    public synchronized static DBMessageHelper getInstance(){
        if(instance==null)instance=new DBMessageHelper(BaseApplication.getInstance());
        return instance;
    }

    public DBMessageHelper(Context context){
        super(context,db_name,null,db_version);
    }
    public DBMessageHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageFiled.createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class MessageFiled{
        public static final String table_name="message";
        public static final String id ="id";
        public static final String mode="mode";//消息模式
        public static final String mid="mid";//群里id
        public static final String uid="uid";//好友或群聊人员id
        public static final String msg="msg";//消息
        public static final String path="path";//消息方向 0 接收 1发送
        public static final String status="status";//状态
        public static final String curtime="curtime";//消息时间
        public static final String rectime="rectime";//语音或视频时间
        public static final String createSQL = "CREATE TABLE  IF NOT EXISTS " + table_name
                + "( "
                +id+" TEXT PRIMARY KEY,"
                + mode+" INTEGER,"
                + mid+" TEXT,"
                +uid+" INTEGER,"
                +msg+ " TEXT,"
                +path+ " INTEGER,"
                +status+ " INTEGER,"
                +curtime+ " TEXT,"
                +rectime+ " INTEGER"
                +" )";
    }
}
