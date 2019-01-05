package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;

import java.util.ArrayList;
import java.util.List;

import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.db.DBFriendHelper;


public class FriendEntityImpl extends DBFriendHelper.FriednFiled {

    private static FriendEntityImpl instance;

    private SQLiteDatabase db;
    public FriendEntityImpl(){
        if(db==null)db=DBFriendHelper.getInstance().getWritableDatabase();
    }

    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }
    public synchronized static FriendEntityImpl getInstance(){
        if(instance==null)instance=new FriendEntityImpl();
        return instance;
    }

    public void insertRow(FriendEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(id,entity.getId());
        cv.put(exid,entity.getExid());
        cv.put(mode,entity.getMode());
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
        cv.put(relation,entity.getRelation());
        db.insert(table_name, "id",cv);
    }

    public void updateRow(FriendEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(id,entity.getId());
        cv.put(exid,entity.getExid());
        cv.put(mode,entity.getMode());
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
        cv.put(relation,entity.getRelation());
        db.update(table_name,cv,"id=?",new String[]{String.valueOf(entity.getId())});
    }

    public int getCount(){
        int count = 0 ;
        String sql ="select count(id) from "+ table_name;
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public void deleteRow(int fid){
        db.delete(table_name,"id=?",new String[]{String.valueOf(fid)});
    }

    public void delete(){
        db.delete(table_name,null,null);
    }

    public void  insert(List<FriendEntity> list){
        db.beginTransaction();
        try{
            for (FriendEntity entity:list){
                ContentValues cv = new ContentValues();
                cv.put(id,entity.getId());
                cv.put(exid,entity.getExid());
                cv.put(mode,entity.getMode());
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
                cv.put(relation,entity.getRelation());
                db.insert(table_name, "id",cv);
            }
            db.setTransactionSuccessful();

        }catch (Exception e){
        }
        finally {
            db.endTransaction();
        }
    }


    public FriendEntity selectRow(int fid,String eid,int m){
        String sql="";
        if(m==FriendEntity.MODE_PERSION)
            sql= "select * from "+ table_name+" where id="+fid+" and mode="+m+" limit 1";
        else
            sql= "select * from "+ table_name+" where exid='"+eid+"' and mode="+m+" limit 1";

        FriendEntity entity = new FriendEntity();
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()){
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setExid(c.getString(c.getColumnIndex(exid)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
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
            entity.setRelation(c.getInt(c.getColumnIndex(relation)));
        }
        c.close();
        return  entity;
    }

    public FriendEntity selectRow(int fid){
        String sql="select * from "+ table_name+" where id=? limit 1";
        FriendEntity entity = new FriendEntity();
        Cursor c = db.rawQuery(sql,new String[]{String.valueOf(fid)});
        while (c.moveToNext()){
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setExid(c.getString(c.getColumnIndex(exid)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
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
            entity.setRelation(c.getInt(c.getColumnIndex(relation)));
        }
        c.close();
        return  entity;
    }

    public List<FriendEntity> selectFriendList(){
        String sql = "select * from "+ table_name+" where mode="+FriendEntity.MODE_PERSION+" and relation="+FriendEntity.RELATION_FRIEND_YES;
        List<FriendEntity> list = new ArrayList<>();
        Cursor c= db.rawQuery(sql,null);
        while (c.moveToNext()){
            FriendEntity entity = new FriendEntity();
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setExid(c.getString(c.getColumnIndex(exid)));
            entity.setMode(c.getInt(c.getColumnIndex(mode)));
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
            entity.setRelation(c.getInt(c.getColumnIndex(relation)));
            list.add(entity);
        }
        c.close();
        return list;
    }


}
