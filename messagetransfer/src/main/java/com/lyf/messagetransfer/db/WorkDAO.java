package com.lyf.messagetransfer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lyf.messagetransfer.bean.Barcode;
import com.lyf.messagetransfer.interfaces.ContentInterface;

import java.util.ArrayList;

/**
 * Created by lyf on 2016/12/19 0019.
 * e_mail:helloliuyf@163.com
 */

public class WorkDAO  {

    private final DataBaseOpenHelper helper;

    public WorkDAO (Context context){
        helper = new DataBaseOpenHelper(context);
    }

    /**
     * 增
     * @param name
     * @param time
     * @return
     */
    public boolean add(String name,String time){
//       String sql = "insert into "+ ContentInterface.TABLE_NAME+"";
//        sql += "(_id, iid, time, date, content, color) values(?, ?, ?, ?, ?, ?)";
        long id = -1;
        if (helper != null) {
            SQLiteDatabase sqlite = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ContentInterface.TABLE_COLUMN_NAME, name);
            values.put(ContentInterface.TABLE_COLUMN_TIME, time);
            sqlite.insert(ContentInterface.TABLE_NAME, null, values);
            sqlite.close();
        }
        return id != -1;
    }

    /**
     * 删
     * @param name
     * @return
     */
    public boolean delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        int num = 0;
        if (db != null) {
            // 条件
            String whereClause = ContentInterface.TABLE_NAME + " = ?";
            // 条件里?对应的值
            String[] whereArgs = new String[] { name };
            num = db.delete(ContentInterface.TABLE_NAME, whereClause, whereArgs);
            db.close();
        }
        return num > 0;
    }

    /**
     * 改
     * @param name
     * @param time
     * @return
     */
    public boolean upDate(String name,String time){
        SQLiteDatabase db = helper.getWritableDatabase();
        int num = 0;
        if (db != null) {
            // 更新的数据
            ContentValues values = new ContentValues();
            // values.put(DbConstants.COLUMN_NUMBER, number);
            values.put(ContentInterface.TABLE_COLUMN_TIME, time);
            // 条件
            String whereClause = ContentInterface.TABLE_COLUMN_NAME + " = ?";
            // 条件里?对应的值
            String[] whereArgs = new String[] { name };
            num = db.update(ContentInterface.TABLE_NAME, values, whereClause,
                    whereArgs);
            db.close();
        }
        return num > 0;
    }

    /**
     * 查询单一的数据
     *
     * @return
     */
    public int findType(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int type = -1;
        if (db != null) {
            String sql = "select " + ContentInterface.TABLE_COLUMN_TIME
                    + " from blacklist where " + ContentInterface.TABLE_COLUMN_NAME
                    + " = ?";
            String[] selectionArgs = new String[] { name };
            Cursor cursor = db.rawQuery(sql, selectionArgs);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    type = cursor.getInt(0);
                }
                cursor.close();
            }
            db.close();
        }
        return type;
    }

    /**
     * 查询所有的数据
     *
     * @return
     */
    public ArrayList<Barcode> findAll() {
        ArrayList<Barcode> infos = new ArrayList<Barcode>();

        SQLiteDatabase db = helper.getWritableDatabase();
        if (db != null) {
            String sql = "select " + ContentInterface.TABLE_COLUMN_NAME + ","
                    + ContentInterface.TABLE_COLUMN_TIME + " from "+ContentInterface.TABLE_NAME;
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String barcode = cursor.getString(1);
                    Barcode info = new Barcode(name,barcode,0);
                    infos.add(info);
                }
                cursor.close();
            }
            db.close();
        }
        return infos;
    }
}
