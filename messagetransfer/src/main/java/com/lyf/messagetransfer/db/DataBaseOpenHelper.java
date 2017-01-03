package com.lyf.messagetransfer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lyf.messagetransfer.interfaces.ContentInterface;

/**
 * Created by lyf on 2016/12/19 0019.
 * e_mail:helloliuyf@163.com
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {


    String sql = "creat table "+ContentInterface.TABLE_NAME+" (_id integer primary key autoincrement,"+ContentInterface.TABLE_COLUMN_NAME+" varchar(20),"+ContentInterface.TABLE_COLUMN_TIME+" varchar(20))";
    /**
     * 构造方法
     * @param context
     */
    public DataBaseOpenHelper(Context context) {
        super(context, ContentInterface.DATABASE_NAME, null, ContentInterface.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
