package com.example.richardoalvin.trappedstickworld;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Richardo Alvin on 4/26/2018.
 */

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "SQLDatabase.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PLAYER( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, HEALTH INTEGER, AGI INTEGER, STR INTEGER, INTEL INTEGER, ITEM TEXT, MONEY INTEGER, POSITION INTEGER, QUEST INTEGER, DONE INTEGER, TIME TEXT);");
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
        cv.put("ITEM", "NONE");
        cv.put("MONEY", 0);
        cv.put("POSITION", 0);
        cv.put("QUEST", 0);
        cv.put("DONE", 0);
        cv.put("TIME", "0,12");
        ContentValues cv2 = new ContentValues();
        cv2.put("NAME", "0");
        cv2.put("HEALTH", 0);
        cv2.put("AGI", 0);
        cv2.put("STR", 0);
        cv2.put("INTEL",0);
        cv2.put("ITEM", "NONE");
        cv2.put("MONEY", 0);
        cv2.put("POSITION", 0);
        cv2.put("QUEST", 0);
        cv2.put("DONE", 0);
        cv2.put("TIME", "0,12");
        if (!(cursor.moveToFirst())) {
            Log.d("create","0");
            this.getWritableDatabase().insertOrThrow("PLAYER", "", cv);
        } else {
            Log.d("Update","1");
            this.getWritableDatabase().update("PLAYER", cv2, "ID = 0",null);
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
    public int text_load(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT DONE FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    public void change_text (int state) {
        ContentValues cv = new ContentValues();
        cv.put("DONE", state);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load item from database
    public String load_item(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT ITEM FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }
    public void add_stats (int str, int agi, int intel) {
        String old_stats;
        old_stats= load_stats();
        List<String> stats = Arrays.asList(old_stats.split(","));
        ContentValues cv = new ContentValues();
        cv.put("AGI", Integer.valueOf(stats.get(0))+agi);
        cv.put("STR", Integer.valueOf(stats.get(1))+str);
        cv.put("INTEL", Integer.valueOf(stats.get(2))+intel);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public void add_money (int mny) {
        int old_mny;
        old_mny = load_money();
        String old_stats;
        old_stats= load_stats();
        List<String> stats = Arrays.asList(old_stats.split(","));
        int intel = (Integer.valueOf(stats.get(2)));
        ContentValues cv = new ContentValues();
        cv.put("MONEY", old_mny+mny+(intel/2));
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public String load_stats(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT AGI, STR, INTEL FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getInt(0)+","+cursor.getInt(1)+","+cursor.getInt(2));
        }
        return null;
    }
    public int load_money(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT MONEY FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //if the character happen to get an item
    public void add_item (String item) {
        String old_item;
        old_item = load_item();
        ContentValues cv = new ContentValues();
        if(old_item == "NONE"){
            cv.put("ITEM",  item);
        }
        else{cv.put("ITEM",  item + "," + old_item);}
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public int load_quest(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT QUEST FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    public void quest_change (int quest) {
        ContentValues cv = new ContentValues();
        cv.put("QUEST", quest);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public int load_position(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT POSITION FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    public void position_change (int x) {
        ContentValues cv = new ContentValues();
        cv.put("POSITION", x);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public int load_health(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT HEALTH FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    public void change_health () {
        String old_stats;
        old_stats= load_stats();
        List<String> stats = Arrays.asList(old_stats.split(","));
        ContentValues cv = new ContentValues();
        int str = Integer.valueOf(stats.get(1));
        cv.put("HEALTH", 100+(str/2));
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    public String load_time(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT TIME FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }
    public void change_time (int hour) {
        String old_time;
        old_time= load_time();
        List<String> time = Arrays.asList(old_time.split(","));
        ContentValues cv = new ContentValues();
        int days = Integer.valueOf(time.get(0));
        int hours = Integer.valueOf(time.get(1));
        if (hours-hour <= 0){
            cv.put("TIME",(Integer.valueOf(days)+1)+",12");
        }
        else{
            cv.put("TIME",days+","+(Integer.valueOf(hours)-hour));
        }
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }

}
