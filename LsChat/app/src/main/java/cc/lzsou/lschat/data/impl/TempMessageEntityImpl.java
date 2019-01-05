package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.data.db.DBTempMessageHelper;

public class TempMessageEntityImpl extends DBTempMessageHelper.TempMessageFiled {

    private SQLiteDatabase db;
    public TempMessageEntityImpl(){
        if(db==null)db=DBTempMessageHelper.getInstance().getWritableDatabase();
    }

    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }
    private static TempMessageEntityImpl instance;
    public synchronized static TempMessageEntityImpl getInstance(){
        if (instance==null)
            instance=new TempMessageEntityImpl();
        return instance;
    }
    public void delete(){
        db.delete(table_name,null,null);
    }

    public void insertRow(MessageEntity entity) {
        ContentValues cv = new ContentValues();
        cv.put(id,entity.getId());
        cv.put(mode, entity.getMode());
        cv.put(mid, entity.getMid());
        cv.put(uid, entity.getUid());
        cv.put(msg, entity.getMsg());
        cv.put(path, entity.getPath());
        cv.put(status, entity.getStatus());
        cv.put(curtime, entity.getCurtime());
        cv.put(rectime, entity.getRectime());
        db.delete(table_name,"id=?",new String[]{entity.getId()});
        db.insert(table_name, "id", cv);
    }


    public void delete(String mid) {
        db.delete(table_name, "mid=?", new String[]{mid});
    }



    public int getCount(String mid){
        System.out.println("获取信息条数："+mid);
        int count = 0;
        String sql = "select count(id) from " + table_name + " where mid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(mid)});
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


    public int getAllCount() {
        int count = 0;
        String sql = "select count(id) from " + table_name + "";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
