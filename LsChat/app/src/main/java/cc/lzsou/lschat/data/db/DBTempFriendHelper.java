package cc.lzsou.lschat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cc.lzsou.lschat.base.BaseApplication;

public class DBTempFriendHelper extends SQLiteOpenHelper {

    private static final String db_name="lschat.tempfriend.db";
    private static final  int db_version = 1;
    private static DBTempFriendHelper instance;
    public synchronized static DBTempFriendHelper getInstance(){
        if(instance==null)instance=new DBTempFriendHelper(BaseApplication.getInstance());
        return instance;
    }

    public DBTempFriendHelper(Context context){
        super(context,db_name,null,db_version);
    }
    public DBTempFriendHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TempFriednFiled.createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class TempFriednFiled{
        public static final String table_name="friend";
        public static final String id ="id";
        public static final String platid="platid";
        public static final String name="name";
        public static final String username="username";
        public static final String idnumber="idnumber";
        public static final String nickname="nickname";
        public static final String sex="sex";
        public static final String mobile="mobile";
        public static final String email="email";
        public static final String avatar="avatar";
        public static final String region="region";
        public static final String regionid="regionid";
        public static final String address="address";
        public static final String jobunit="jobunit";
        public static final String phone="phone";
        public static final String vip="vip";
        public static final String vipexpire="vipexpire";
        public static final String remark="remark";
        public static final String status="status";
        public static final String msg="msg";
        public static final String createSQL = "CREATE TABLE  IF NOT EXISTS " + table_name
                + "( "
                +id+" INTEGER PRIMARY KEY,"
                + platid+" TEXT,"
                + name+" TEXT,"
                +username+" TEXT,"
                +idnumber+" TEXT,"
                +nickname+ " TEXT,"
                +sex+ " INTEGER,"
                +mobile+ " TEXT,"
                +email+ " TEXT,"
                +avatar+ " TEXT,"
                +region+ " TEXT,"
                +regionid+ " TEXT,"
                +address+ " TEXT,"
                +jobunit+ " TEXT,"
                +phone+ " TEXT,"
                +vip+" INTEGER,"
                +vipexpire+" TEXT,"
                +remark+" TEXT,"
                +status+" INTEGER,"
                +msg+" TEXT"
                +" )";
    }
}
