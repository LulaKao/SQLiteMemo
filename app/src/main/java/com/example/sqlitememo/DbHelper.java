package com.example.sqlitememo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String KEY_ID = "_id";
    // 注意：好像一定要把 primary key 的名字設定為 _id，不然會 error（？）
    public static final String KEY_DATE = "date";
    public static final String KEY_MEMO = "memo";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_BGCOLOR = "bgcolor";
    public static final String DATABASE_NAME = "Memos";
    public static final String TABLE_NAME = "memo";
    public static final int DB_VERSION = 1; //  版本 1

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        // super 關鍵字指的是目前這個 class 的上一層，也就是 SQLiteOpenHelper
    }

    @Override
    //  如果 Android 載入時找不到生成的資料庫檔案，就會觸發 onCreate
    public void onCreate(SQLiteDatabase db) {
        // 組裝建立資料表的 SQL 語法
        final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                KEY_ID + " integer PRIMARY KEY autoincrement," +
                KEY_DATE + "," +
                KEY_MEMO + "," +
                KEY_REMIND + "," +
                KEY_BGCOLOR + ");";
        /* 僅 KEY_ID 的型態指定為 int，其他的都是 String
                上面的程式是在生成一連串的指令字串（String）
                用 "," 來區隔各欄位
                "();"  也是 SQL 語法 */
        Log.d("SQL=",DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE); // 用 execSQL() 執行 SQL 指令
    }

    @Override
    // 如果資料庫版本（DB_VERSION）結構有改變了就會觸發 onUpgrade
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除舊資料表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // 重新建立新資料表
        onCreate(db);
    }
}