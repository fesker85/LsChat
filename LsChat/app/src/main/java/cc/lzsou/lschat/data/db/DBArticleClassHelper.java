package cc.lzsou.lschat.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cc.lzsou.lschat.base.BaseApplication;

public class DBArticleClassHelper extends SQLiteOpenHelper {
    private static final String db_name="lschat.articleclass.db";
    private static final  int db_version = 1;
    public static final String table_name="articleclass";
    private static DBArticleClassHelper instance;
    public synchronized static DBArticleClassHelper getInstance(){
        if(instance==null)instance=new DBArticleClassHelper(BaseApplication.getInstance());
        return instance;
    }
    public DBArticleClassHelper(Context context){
        super(context,db_name,null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE  IF NOT EXISTS " + table_name
                + "( id INTEGER PRIMARY KEY AUTOINCREMENT,aid text,name text,sort integer,updatetime text,mine integer,i_filed INTEGER,t_field text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTable(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    private void dropTable(SQLiteDatabase db) {
        String sql = "DROP TABLE IF EXISTS "+table_name;
        db.execSQL(sql);
    }
}
