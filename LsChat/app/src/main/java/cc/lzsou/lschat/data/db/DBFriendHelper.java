package cc.lzsou.lschat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;

import cc.lzsou.lschat.base.BaseApplication;

public class DBFriendHelper extends SQLiteOpenHelper {
    private static final String db_name="lschat.friend.db";
    private static final  int db_version = 1;
    private static DBFriendHelper instance;
    public synchronized static DBFriendHelper getInstance(){
        if(instance==null)instance=new DBFriendHelper(BaseApplication.getInstance());
        return instance;
    }

    public DBFriendHelper(Context context){
        super(context,db_name,null,db_version);
    }
    public DBFriendHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FriednFiled.createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class FriednFiled{
        public static final String table_name="friend";
        public static final String id ="id";
        public static final String exid="exid";
        public static final String mode="mode";
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
        public static final String relation="relation";
        public static final String createSQL = "CREATE TABLE  IF NOT EXISTS " + table_name
                + "( "
                +id+" INTEGER PRIMARY KEY,"
                +exid+" TEXT,"
                +mode+" INTEGER,"
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
                +relation+" INTEGER"
                +" )";
    }
}
