package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.db.DBTempFriendHelper;

public class TempFriendEntityImpl extends DBTempFriendHelper.TempFriednFiled {

    private SQLiteDatabase db;
    public TempFriendEntityImpl(){
        if(db==null)db=DBTempFriendHelper.getInstance().getWritableDatabase();
    }

    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }
    private static TempFriendEntityImpl instance;
    public synchronized static TempFriendEntityImpl getInstance(){
        if(instance==null)instance = new TempFriendEntityImpl();
        return instance;
    }

    public void insertRow(TempFriendEntity entity){
        delete(entity.getId());
        ContentValues cv = new ContentValues();
        cv.put(id,entity.getId());
        cv.put(platid,entity.getPlatid());
        cv.put(name,entity.getName());
        cv.put(username,entity.getUsername());
        cv.put(idnumber,entity.getIdnumber());
        cv.put(nickname,entity.getNickname());
        cv.put(sex,entity.getSex());
        cv.put(mobile,entity.getMobile());
        cv.put(email,entity.getEmail());
        cv.put(avatar,entity.getAvatar());
        cv.put(region,entity.getRegion());
        cv.put(regionid,entity.getRegionid());
        cv.put(address,entity.getAddress());
        cv.put(jobunit,entity.getJobunit());
        cv.put(phone,entity.getPhone());
        cv.put(vip,entity.getVip());
        cv.put(vipexpire,entity.getVipexpire());
        cv.put(remark,entity.getRemark());
        cv.put(status,entity.getStatus());
        cv.put(msg,entity.getMsg());
        db.insert(table_name, "id",cv);
    }

    public void setRead(){
        ContentValues cv = new ContentValues();
        cv.put(status,TempFriendEntity.STATUS_READED);
        db.update(table_name,cv,"status=?",new String[]{String.valueOf(TempFriendEntity.STATUS_NEW)});
    }
    public void updateState(int fid,int state){
        ContentValues cv = new ContentValues();
        cv.put(status,state);
        db.update(table_name,cv,"id=?",new String[]{String.valueOf(fid)});
    }
    private void delete(int fid){
        db.delete(table_name,"id=?",new String[]{String.valueOf(fid)});
    }
    public void delete(){
        db.delete(table_name,null,null);
    }
    public void updateRow(TempFriendEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(platid,entity.getPlatid());
        cv.put(name,entity.getName());
        cv.put(username,entity.getUsername());
        cv.put(idnumber,entity.getIdnumber());
        cv.put(nickname,entity.getNickname());
        cv.put(sex,entity.getSex());
        cv.put(mobile,entity.getMobile());
        cv.put(email,entity.getEmail());
        cv.put(avatar,entity.getAvatar());
        cv.put(region,entity.getRegion());
        cv.put(regionid,entity.getRegionid());
        cv.put(address,entity.getAddress());
        cv.put(jobunit,entity.getJobunit());
        cv.put(phone,entity.getPhone());
        cv.put(vip,entity.getVip());
        cv.put(vipexpire,entity.getVipexpire());
        cv.put(remark,entity.getRemark());
        cv.put(status,entity.getStatus());
        cv.put(msg,entity.getMsg());
        db.update(table_name,cv,"id=?",new String[]{String.valueOf(entity.getId())});
    }


    public void deleteRow(int fid){
        db.delete(table_name,"id=?",new String[]{String.valueOf(fid)});
    }

    public TempFriendEntity selectRow(int fid){
        String sql="select * from "+table_name+" where id=?";
        TempFriendEntity entity = new TempFriendEntity();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(fid)});
        while (c.moveToNext()){
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setPlatid(c.getString(c.getColumnIndex(platid)));
            entity.setName(c.getString(c.getColumnIndex(name)));
            entity.setUsername(c.getString(c.getColumnIndex(username)));
            entity.setIdnumber(c.getString(c.getColumnIndex(idnumber)));
            entity.setNickname(c.getString(c.getColumnIndex(nickname)));
            entity.setSex(c.getInt(c.getColumnIndex(sex)));
            entity.setMobile(c.getString(c.getColumnIndex(mobile)));
            entity.setEmail(c.getString(c.getColumnIndex(email)));
            entity.setAvatar(c.getString(c.getColumnIndex(avatar)));
            entity.setRegion(c.getString(c.getColumnIndex(region)));
            entity.setRegionid(c.getString(c.getColumnIndex(regionid)));
            entity.setAddress(c.getString(c.getColumnIndex(address)));
            entity.setJobunit(c.getString(c.getColumnIndex(jobunit)));
            entity.setPhone(c.getString(c.getColumnIndex(phone)));
            entity.setVip(c.getInt(c.getColumnIndex(vip)));
            entity.setVipexpire(c.getString(c.getColumnIndex(vipexpire)));
            entity.setRemark(c.getString(c.getColumnIndex(remark)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
        }
        c.close();
        return  entity;
    }

    public List<TempFriendEntity> select(){
        String sql = "select * from "+ table_name;
        List<TempFriendEntity> list = new ArrayList<>();
        Cursor c= db.rawQuery(sql,null);
        while (c.moveToNext()){
            TempFriendEntity entity = new TempFriendEntity();
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setPlatid(c.getString(c.getColumnIndex(platid)));
            entity.setName(c.getString(c.getColumnIndex(name)));
            entity.setUsername(c.getString(c.getColumnIndex(username)));
            entity.setIdnumber(c.getString(c.getColumnIndex(idnumber)));
            entity.setNickname(c.getString(c.getColumnIndex(nickname)));
            entity.setSex(c.getInt(c.getColumnIndex(sex)));
            entity.setMobile(c.getString(c.getColumnIndex(mobile)));
            entity.setEmail(c.getString(c.getColumnIndex(email)));
            entity.setAvatar(c.getString(c.getColumnIndex(avatar)));
            entity.setRegion(c.getString(c.getColumnIndex(region)));
            entity.setRegionid(c.getString(c.getColumnIndex(regionid)));
            entity.setAddress(c.getString(c.getColumnIndex(address)));
            entity.setJobunit(c.getString(c.getColumnIndex(jobunit)));
            entity.setPhone(c.getString(c.getColumnIndex(phone)));
            entity.setVip(c.getInt(c.getColumnIndex(vip)));
            entity.setVipexpire(c.getString(c.getColumnIndex(vipexpire)));
            entity.setRemark(c.getString(c.getColumnIndex(remark)));
            entity.setStatus(c.getInt(c.getColumnIndex(status)));
            entity.setMsg(c.getString(c.getColumnIndex(msg)));
            list.add(entity);
        }
        c.close();
        return list;
    }

    public int getStatusCount(int state){
        int count = 0 ;
        String sql ="select count(id) from "+ table_name+" where status=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(state)});
        while(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }


}
