package cc.lzsou.lschat.base.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.data.bean.RegionEntity;
import cc.lzsou.lschat.manager.FileManager;

public class DBRegionHelper {

    private static DBRegionHelper instance;
    public static DBRegionHelper getInstance() {
        if (instance == null) instance = new DBRegionHelper();
        return instance;
    }

    public List<RegionEntity> select(String code) {
        String sql = " select code,name from t_region";
        String where = " where  ";
        if (code.equals("")) {
            where += "  province <> '00' and city ='00' and zone = '00' order by code asc ";
        } else {
            where += " province = '" + code.substring(0, 2) + "' ";
            if (code.substring(2, 4).equals("00")) {
                where += "   and city <> '" + code.substring(2, 4) + "' and zone = '" + code.substring(4) + "' order by code asc ";
            } else {
                where += "   and city = '" + code.substring(2, 4) + "' and zone <> '" + code.substring(4) + "' order by code asc ";
            }
        }
        sql += where;
        List<RegionEntity> list = new ArrayList<>();
        SQLiteDatabase db = openSQLiteDatabase();
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            RegionEntity entity = new RegionEntity();
            entity.setCode(c.getString(c.getColumnIndex("code")).substring(0, 6));
            entity.setName(c.getString(c.getColumnIndex("name")));
            list.add(entity);
        }
        c.close();
        db.close();
        return list;
    }

    private SQLiteDatabase openSQLiteDatabase() {
        File file = new File(FileManager.getInstance().getRegionDbPath());
        if (file.exists()) return SQLiteDatabase.openOrCreateDatabase(file, null);
        InputStream is = BaseApplication.getInstance().getClass().getClassLoader().getResourceAsStream("assets/database/" + "region.db");
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            byte[] buffer = new byte[10240];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return openSQLiteDatabase();
    }

}
