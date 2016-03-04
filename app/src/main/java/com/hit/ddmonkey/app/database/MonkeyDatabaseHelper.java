package com.hit.ddmonkey.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by dao on 15-12-21.
 */
public class MonkeyDatabaseHelper extends SQLiteOpenHelper {
    /*建表语句*/
    public static final String CREATE_GOAL = "create table Goal("
    +"id integer Primary key,"
    +"goalname text,"
    +"time integer,"
    +"money real,"
            +"date text"+
            ")";

    public static final String CREATE_BILL = "create table Bill("
            +"id integer Primary key,"
            +"io text,"
            +"money real,"
            +"category text,"
            +"note text,"
            +"date text,"
            +"dateid text"+
            ")";

    /*提供上下文*/
    private Context mcontext;

    /*重写构造方法*/
    public MonkeyDatabaseHelper(Context context, String name,SQLiteDatabase.CursorFactory factory,int version) {
        super(context, name, factory, version);
        mcontext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BILL);
        db.execSQL(CREATE_GOAL);

        Toast.makeText(mcontext,"初始化成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        System.out.println("--------onUpdate Called--------" + oldVersion + "--->" + newVersion);

    }
}
