package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.data.db.DBMessageHelper;

public class MessageEntityImpl extends DBMessageHelper.MessageFiled {
    private static final int SHOW_MSG_COUNT = 10;
    private static MessageEntityImpl instance;
    private SQLiteDatabase db;
    public MessageEntityImpl(){
        if (db == null) db = DBMessageHelper.getInstance().getWritableDatabase();
    }
    public synchronized static MessageEntityImpl getInstance() {
        if (instance == null) instance = new MessageEntityImpl();
        return instance;
    }


    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }


    public void insertRow(MessageEntity entity) {
        ContentValues cv = new ContentValues();
        cv.put(id, entity.getId());
        cv.put(mode, entity.getMode());
        cv.put(mid, entity.getMid());
        cv.put(uid, entity.getUid());
        cv.put(msg, entity.getMsg());
        cv.put(path, entity.getPath());
        cv.put(status, entity.getStatus());
        cv.put(curtime, entity.getCurtime());
        cv.put(rectime, entity.getRectime());
        db.delete(table_name, "id=?", new String[]{entity.getId()});
        db.insert(table_name, "id", cv);
    }

    public void updateMessage(String msgid, String message) {
        ContentValues cv = new ContentValues();
        cv.put(msg, message);
        db.update(table_name, cv, "id=?", new String[]{msgid});
    }

    public void updateState(String msgid, int state) {
        ContentValues cv = new ContentValues();
        cv.put(status, state);
        db.update(table_name, cv, "id=?", new String[]{msgid});
    }


    /**
     * 取当前聊天信息
     *
     * @param fid
     * @param lasttime
     * @param m        0代表current 1代表next -1代表prve
     * @return
     */
    public List<MessageEntity> selectChatList(int fid, String lasttime, int m) {
        List<MessageEntity> list = new ArrayList<>();
        String sql = "";
        if (m == 0)
            sql = "select * from (select *from message where mid='chat" + fid + "' order by curtime desc limit 10) as tb  order by tb.curtime asc";
        else if (m == 1)
            sql = "select * from (select *from message where mid='chat" + fid + "' and curtime>'" + lasttime + "' order by curtime desc limit 10) as tb  order by tb.curtime asc";
        else
            sql = "select * from (select *from message where mid='chat" + fid + "' and curtime<'" + lasttime + "' order by curtime desc limit 10) as tb  order by tb.curtime asc";

        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            MessageEntity entity = new MessageEntity();
            entity.setId(c.getString(c.getColumnIndex(id)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
            entity.setMid(c.getString(c.getColumnIndex(mid)));
            entity.setUid(c.getInt(c.getColumnIndex(uid)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
            entity.setPath(c.getInt(c.getColumnIndex(path)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setCurtime(c.getString(c.getColumnIndex(curtime)));
            entity.setRectime(c.getLong(c.getColumnIndex(rectime)));
            list.add(entity);
        }
        c.close();
        return list;
    }

    public MessageEntity selectRow(String msgid) {
        MessageEntity entity = new MessageEntity();
        String sql = "select * from " + table_name + " where id=? limit 1";
        Cursor c = db.rawQuery(sql, new String[]{msgid});
        if (c == null || c.isClosed()) {
            db.close();
            return selectRow(msgid);
        }
        while (c.moveToNext()) {
            entity.setId(c.getString(c.getColumnIndex(id)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
            entity.setMid(c.getString(c.getColumnIndex(mid)));
            entity.setUid(c.getInt(c.getColumnIndex(uid)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
            entity.setPath(c.getInt(c.getColumnIndex(path)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setCurtime(c.getString(c.getColumnIndex(curtime)));
            entity.setRectime(c.getLong(c.getColumnIndex(rectime)));
        }
        c.close();
        return entity;
    }

    public List<MessageEntity> selectSending() {
        List<MessageEntity> list = new ArrayList<>();
        String sql = "select * from " + table_name + " where status = " + MessageEntity.STATE_SEND_SENDING + " order by curtime asc";
        Cursor c = db.rawQuery(sql, new String[]{});
        while (c.moveToNext()) {
            MessageEntity entity = new MessageEntity();
            entity.setId(c.getString(c.getColumnIndex(id)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
            entity.setMid(c.getString(c.getColumnIndex(mid)));
            entity.setUid(c.getInt(c.getColumnIndex(uid)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
            entity.setPath(c.getInt(c.getColumnIndex(path)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setCurtime(c.getString(c.getColumnIndex(curtime)));
            entity.setRectime(c.getLong(c.getColumnIndex(rectime)));
            list.add(entity);
        }
        c.close();
        return list;
    }

    public List<MessageEntity> selectLast() {
        List<MessageEntity> list = new ArrayList<>();
        String sql = "select * from " + table_name + "  group by mid order by curtime desc";
        Cursor c = db.rawQuery(sql, new String[]{});
        while (c.moveToNext()) {
            MessageEntity entity = new MessageEntity();
            entity.setId(c.getString(c.getColumnIndex(id)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
            entity.setMid(c.getString(c.getColumnIndex(mid)));
            entity.setUid(c.getInt(c.getColumnIndex(uid)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
            entity.setPath(c.getInt(c.getColumnIndex(path)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setCurtime(c.getString(c.getColumnIndex(curtime)));
            entity.setRectime(c.getLong(c.getColumnIndex(rectime)));
            list.add(entity);
        }
        c.close();
        return list;
    }

    public void delete() {
        db.delete(table_name, null, null);
    }

    public void delete(String mid) {
        db.delete(table_name, "mid=?", new String[]{mid});
    }
}
