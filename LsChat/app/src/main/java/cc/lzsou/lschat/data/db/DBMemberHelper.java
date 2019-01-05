package cc.lzsou.lschat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cc.lzsou.lschat.base.BaseApplication;

public class DBMemberHelper extends SQLiteOpenHelper {

    private static final String db_name="lschat.member.db";
    private static final  int db_version = 1;
    private static DBMemberHelper instance;
    public synchronized static DBMemberHelper getInstance(){
        if(instance==null)instance=new DBMemberHelper(BaseApplication.getInstance());
        return instance;
    }

    public DBMemberHelper(Context context){
        super(context,db_name,null,db_version);
    }
    public DBMemberHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MemberFiled.createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class MemberFiled{
        public static final String table_name="member";
        public static final String id="id";
        public static final String platid="platid";
        public static final String name="name";//姓名
        public static final String idnumber="idnumber";//身份证号
        public static final String nickname="nickname";//昵称
        public static final String sex="sex";//性别
        public static final String mobile="mobile";//手机号码
        public static final String email="email";//电子邮箱
        public static final String username="username";//帐号
        public static final String password="password";//登录密码
        public static final String paypwd="paypwd";//支付密码
        public static final String avatar="avatar";//头像地址
        public static final String region="region";//所在区域
        public static final String regionid="regionid";//所在区域id
        public static final String address="address";//详细地址
        public static final String jobunit="jobunit";//工作单位
        public static final String phone="phone";//固定电话
        public static final String wxid="wxid";
        public static final String qqid="qqid";
        public static final String wbid="wbid";
        public static final String aliid="aliid";
        public static final String vip="vip";//会员
        public static final String vipexpire="vipexpire";//会员到期
        public static final String createtime="createtime";
        public static final String updatetime="updatetime";
        public static final String createSQL = "CREATE TABLE  IF NOT EXISTS " + table_name
                + "( "
                +id+" INTEGER PRIMARY KEY,"
                + platid+" TEXT,"
                + name+" TEXT,"
                + idnumber+" TEXT,"
                +nickname+ " TEXT,"
                +sex+ " INTEGER,"
                +mobile+ " TEXT,"
                +email+ " TEXT,"
                +username+ " TEXT,"
                +password+ " TEXT,"
                +paypwd+ " TEXT,"
                +avatar+ " TEXT,"
                +region+ " TEXT,"
                +regionid+ " TEXT,"
                +address+ " TEXT,"
                +jobunit+ " TEXT,"
                +phone+ " TEXT,"
                +wxid+ " TEXT,"
                +qqid+ " TEXT,"
                +wbid+ " TEXT,"
                +aliid+ " TEXT,"
                +vip+" INTEGER,"
                +vipexpire+" TEXT,"
                +createtime+" TEXT,"
                +updatetime+" TEXT"
                +" )";
    }
}
