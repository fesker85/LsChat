package cc.lzsou.lschat.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cc.lzsou.lschat.data.db.DBArticleClassHelper;
import cc.lzsou.lschat.data.bean.ArticleClassEntity;

public class ArticleClassEntityImpl {
    private static ArticleClassEntityImpl instance;
    private SQLiteDatabase db;

    public void destroy() {
        if (db != null && db.isOpen()) db.close();
    }

    public ArticleClassEntityImpl() {
        if (db == null) db = DBArticleClassHelper.getInstance().getWritableDatabase();
    }

    public synchronized static ArticleClassEntityImpl getInstance() {
        if (instance == null) instance = new ArticleClassEntityImpl();
        return instance;
    }

    public void delete() {
        db.delete(DBArticleClassHelper.table_name, null, null);
    }

    public void updateRow(ArticleClassEntity entity) {
        ContentValues cv = new ContentValues();
        cv.put("name", entity.getName());
        cv.put("mine", entity.getMine());
        cv.put("sort", entity.getSort());
        cv.put("updatetime", entity.getUpdatetime());
        db.update(DBArticleClassHelper.table_name, cv, "aid=?", new String[]{entity.getId()});
    }

    public List<ArticleClassEntity> selectAll() {
        List<ArticleClassEntity> list = new ArrayList<>();
        String sql = "select * from " + DBArticleClassHelper.table_name;
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            ArticleClassEntity entity = new ArticleClassEntity();
            entity.setId(c.getString(c.getColumnIndex("aid")));
            entity.setName(c.getString(c.getColumnIndex("name")));
            entity.setMine(c.getInt(c.getColumnIndex("mine")));
            entity.setSort(c.getInt(c.getColumnIndex("sort")));
            entity.setUpdatetime(c.getString(c.getColumnIndex("updatetime")));
            list.add(entity);
        }
        c.close();
        return list;
    }


    public void insert(List<ArticleClassEntity> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        db.beginTransaction();
        try {
            for (ArticleClassEntity entity : list) {
                ContentValues cv = new ContentValues();
                cv.put("aid", entity.getId());
                cv.put("name", entity.getName());
                cv.put("sort", entity.getSort());
                cv.put("updatetime", entity.getUpdatetime());
                cv.put("mine", entity.getMine());
                db.insert(DBArticleClassHelper.table_name, "id", cv);
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
}
