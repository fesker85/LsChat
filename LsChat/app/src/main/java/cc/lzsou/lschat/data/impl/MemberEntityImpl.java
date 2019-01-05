package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.data.db.DBMemberHelper;

public class MemberEntityImpl extends DBMemberHelper.MemberFiled {
    private static MemberEntityImpl instance;

    private SQLiteDatabase db;
    public MemberEntityImpl(){
        if(db==null)db=DBMemberHelper.getInstance().getWritableDatabase();
    }

    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }

    public synchronized static MemberEntityImpl getInstance(){
        if(instance==null)instance = new MemberEntityImpl();
        return instance;
    }

    public void insertRow(MemberEntity entity){
        deleteRow();
        ContentValues cv = new ContentValues();
        cv.put(id,entity.getId());
        cv.put(platid,entity.getPlatid());
        cv.put(name,entity.getName());
        cv.put(idnumber,entity.getIdnumber());
        cv.put(nickname,entity.getNickname());
        cv.put(sex,entity.getSex());
        cv.put(mobile,entity.getMobile());
        cv.put(email,entity.getEmail());
        cv.put(username,entity.getUsername());
        cv.put(password,entity.getPassword());
        cv.put(paypwd,entity.getPaypwd());
        cv.put(avatar,entity.getAvatar());
        cv.put(region,entity.getRegion());
        cv.put(regionid,entity.getRegionid());
        cv.put(address,entity.getAddress());
        cv.put(jobunit,entity.getJobunit());
        cv.put(phone,entity.getPhone());
        cv.put(wxid,entity.getWxid());
        cv.put(qqid,entity.getQqid());
        cv.put(wbid,entity.getWbid());
        cv.put(aliid,entity.getAliid());
        cv.put(vip,entity.getVip());
        cv.put(vipexpire,entity.getVipexpire());
        cv.put(createtime,entity.getCreatetime());
        cv.put(updatetime,entity.getUpdatetime());
        db.insert(table_name,"id",cv);
    }
    public void updateRow(MemberEntity entity){
        ContentValues cv = new ContentValues();
        cv.put(platid,entity.getPlatid());
        cv.put(name,entity.getName());
        cv.put(idnumber,entity.getIdnumber());
        cv.put(nickname,entity.getNickname());
        cv.put(sex,entity.getSex());
        cv.put(mobile,entity.getMobile());
        cv.put(email,entity.getEmail());
        cv.put(username,entity.getUsername());
        cv.put(password,entity.getPassword());
        cv.put(paypwd,entity.getPaypwd());
        cv.put(avatar,entity.getAvatar());
        cv.put(region,entity.getRegion());
        cv.put(regionid,entity.getRegionid());
        cv.put(address,entity.getAddress());
        cv.put(jobunit,entity.getJobunit());
        cv.put(phone,entity.getPhone());
        cv.put(wxid,entity.getWxid());
        cv.put(qqid,entity.getQqid());
        cv.put(wbid,entity.getWbid());
        cv.put(aliid,entity.getAliid());
        cv.put(vip,entity.getVip());
        cv.put(vipexpire,entity.getVipexpire());
        cv.put(createtime,entity.getCreatetime());
        cv.put(updatetime,entity.getUpdatetime());
        db.update(table_name,cv,"id=?",new String[]{String.valueOf(entity.getId())});
    }

    public MemberEntity selectRow(){
        MemberEntity entity = new MemberEntity();
        String sql="select * from "+ table_name+" limit 1";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()){
            entity.setId(c.getInt(c.getColumnIndex(id)));
            entity.setPlatid(c.getString(c.getColumnIndex(platid)));
            entity.setName(c.getString(c.getColumnIndex(name)));
            entity.setIdnumber(c.getString(c.getColumnIndex(idnumber)));
            entity.setNickname(c.getString(c.getColumnIndex(nickname)));
            entity.setSex(c.getInt(c.getColumnIndex(sex)));
            entity.setMobile(c.getString(c.getColumnIndex(mobile)));
            entity.setEmail(c.getString(c.getColumnIndex(email)));
            entity.setUsername(c.getString(c.getColumnIndex(username)));
            entity.setPassword(c.getString(c.getColumnIndex(password)));
            entity.setPaypwd(c.getString(c.getColumnIndex(paypwd)));
            entity.setAvatar(c.getString(c.getColumnIndex(avatar)));
            entity.setRegion(c.getString(c.getColumnIndex(region)));
            entity.setRegionid(c.getString(c.getColumnIndex(regionid)));
            entity.setAddress(c.getString(c.getColumnIndex(address)));
            entity.setJobunit(c.getString(c.getColumnIndex(jobunit)));
            entity.setPhone(c.getString(c.getColumnIndex(phone)));
            entity.setWxid(c.getString(c.getColumnIndex(wxid)));
            entity.setQqid(c.getString(c.getColumnIndex(qqid)));
            entity.setWbid(c.getString(c.getColumnIndex(wbid)));
            entity.setAliid(c.getString(c.getColumnIndex(aliid)));
            entity.setVip(c.getInt(c.getColumnIndex(vip)));
            entity.setVipexpire(c.getString(c.getColumnIndex(vipexpire)));
            entity.setCreatetime(c.getString(c.getColumnIndex(createtime)));
            entity.setUpdatetime(c.getString(c.getColumnIndex(updatetime)));
        }
        c.close();
        return  entity;
    }

    public void deleteRow(){
        db.delete(table_name,null,null);
    }

}
