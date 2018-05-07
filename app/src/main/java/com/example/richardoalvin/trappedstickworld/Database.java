package com.example.richardoalvin.trappedstickworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Richardo Alvin on 4/26/2018.
 */

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "playerdata.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PLAYER( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, HEALTH INTEGER, AGI INTEGER, STR INTEGER, INTEL INTEGER, ITEM TEXT, MONEY INTEGER, QUEST TEXT, DONE TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST PLAYER;");
        onCreate(sqLiteDatabase);
    }
    // when the character was set up
    public void setup(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM PLAYER WHERE (ID = 0)", null);
        ContentValues cv = new ContentValues();
        cv.put("ID", 0);
        cv.put("NAME", "0");
        cv.put("HEALTH", 0);
        cv.put("AGI", 0);
        cv.put("STR", 0);
        cv.put("INTEL",0);
        cv.put("ITEM", "0");
        cv.put("MONEY", 0);
        cv.put("QUEST", "0");
        cv.put("DONE", "0");
        ContentValues cv2 = new ContentValues();
        cv.put("ID", 1);
        cv.put("NAME", "0");
        cv.put("HEALTH", 0);
        cv.put("AGI", 0);
        cv.put("STR", 0);
        cv.put("MONEY", 0);
        cv.put("QUEST", "0");
        cv.put("DONE", "0");
        if (!(cursor.moveToFirst())) {
            this.getWritableDatabase().insertOrThrow("PLAYER", "", cv);
            Log.d("create","0");
        } else {
            this.getWritableDatabase().update("PLAYER", cv2, "ID =" + 0 , null);
            Log.d("Update","1");
        }
    }
    public int load() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM PLAYER WHERE (ID = 0)", null);
        if (!(cursor.moveToFirst())) {
            return 0;
        } else {
            return 1;
        }
    }

    //load item from database
    public String load_item(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT ITEM FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getString(0));
        }
        return null;
    }
    //if the character happen to get an item
    public void add_item (String str) {
        String old_str;
        old_str = load_item();
        Log.d("test", old_str);
        ContentValues cv = new ContentValues();
        cv.put("ITEM",  str + "," + old_str);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public String load_quest(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT ITEM FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getString(0));
        }
        return null;
    }
    public void add_quest (String str) {
        String old_str;
        old_str = load_quest();
        Log.d("test", old_str);
        ContentValues cv = new ContentValues();
        cv.put("QUEST",  str + "," + old_str);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
}
