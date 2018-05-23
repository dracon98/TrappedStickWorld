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
        super(context, "GameDatabaseSQL.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PLAYER( ID INTEGER PRIMARY KEY AUTOINCREMENT, CURHEALTH INTEGER, HEALTH INTEGER, AGI INTEGER, STR INTEGER, INTEL INTEGER, ITEM TEXT, MONEY INTEGER, POSITION INTEGER, QUEST INTEGER, DONE INTEGER, TIME TEXT, WAVES INTEGER);");
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
        cv.put("CURHEALTH", 0);
        cv.put("HEALTH", 0);
        cv.put("AGI", 0);
        cv.put("STR", 0);
        cv.put("INTEL",0);
        cv.put("ITEM", "NONE");
        cv.put("MONEY", 0);
        cv.put("POSITION", 0);
        cv.put("QUEST", 0);
        cv.put("DONE", 0);
        cv.put("TIME", "0,12,awake");
        cv.put("WAVES", 1);
        ContentValues cv2 = new ContentValues();
        cv2.put("CURHEALTH", 0);
        cv2.put("HEALTH", 0);
        cv2.put("AGI", 0);
        cv2.put("STR", 0);
        cv2.put("INTEL",0);
        cv2.put("ITEM", "NONE");
        cv2.put("MONEY", 0);
        cv2.put("POSITION", 0);
        cv2.put("QUEST", 0);
        cv2.put("DONE", 0);
        cv2.put("TIME", "0,12,awake");
        cv2.put("WAVES", 1);
        if (!(cursor.moveToFirst())) {
            Log.d("create","0");
            this.getWritableDatabase().insertOrThrow("PLAYER", "", cv);
        } else {
            Log.d("Update","1");
            this.getWritableDatabase().update("PLAYER", cv2, "ID = 0",null);
        }
    }
    //load previous account
    public int load() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM PLAYER WHERE (ID = 0)", null);
        if (!(cursor.moveToFirst())) {
            return 0;
        } else {
            return 1;
        }
    }
    //load text progress in which here using "DONE"
    public int text_load(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT DONE FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //change text progess
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
    //add stats to database
    public void add_stats (int str, int agi, int intel) {
        //taking the previous database before replacing the previous one
        String old_stats;
        old_stats= load_stats();
        //divide the String into arrays with comma splitter
        List<String> stats = Arrays.asList(old_stats.split(","));
        ContentValues cv = new ContentValues();
        //adding the previous stats with the new one
        cv.put("AGI", Integer.valueOf(stats.get(0))+agi);
        cv.put("STR", Integer.valueOf(stats.get(1))+str);
        cv.put("INTEL", Integer.valueOf(stats.get(2))+intel);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //add money to database
    public void add_money (int mny) {
        //taking the previous database before replacing to the new one
        int old_mny;
        old_mny = load_money();
        String old_stats;
        old_stats= load_stats();
        //divide the String into arrays with comma splitter
        List<String> stats = Arrays.asList(old_stats.split(","));
        int intel = (Integer.valueOf(stats.get(2)));
        ContentValues cv = new ContentValues();
        cv.put("MONEY", old_mny+mny+(intel/8));
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load stats
    public String load_stats(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT AGI, STR, INTEL FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return String.valueOf(cursor.getInt(0)+","+cursor.getInt(1)+","+cursor.getInt(2));
        }
        return null;
    }
    //load money
    public int load_money(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT MONEY FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //if the character happen to get an item
    public void add_item (String item) {
        //taking the previous database before replacing to the new one
        String old_item;
        old_item = load_item();
        ContentValues cv = new ContentValues();
        //if back items equal to none means there is nt any item inside
        if(old_item.equals("NONE")){
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
    //change quest
    public void quest_change (int quest) {
        // put quest result without taking any previous results
        ContentValues cv = new ContentValues();
        cv.put("QUEST", quest);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load position
    public int load_position(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT POSITION FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //change position
    //to keep the player back to where they are
    public void position_change (int x) {
        ContentValues cv = new ContentValues();
        cv.put("POSITION", x);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load health
    public int load_health(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT HEALTH FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //change health
    //to keep up with the current strength the health must be updated
    public void change_health () {
        //taking the previous database before replacing to the new one
        String old_stats;
        old_stats= load_stats();
        List<String> stats = Arrays.asList(old_stats.split(","));
        ContentValues cv = new ContentValues();
        int str = Integer.valueOf(stats.get(1));
        cv.put("HEALTH", 100+(str*2));
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load wave
    public int load_wave(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT WAVES FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //change wave
    //increase old wave by one
    public void change_wave() {
        //taking the previous database before replacing to the new one
        int old_waves;
        old_waves= load_wave();
        ContentValues cv = new ContentValues();
        cv.put("WAVES", old_waves+1);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load time
    public String load_time(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT TIME FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }
    //change time, full time which is 12 deducted by hour wasted
    public void change_time (int hour) {
        //taking the previous database before replacing to the new one
        String old_time;
        old_time= load_time();
        //divide the String into arrays with comma splitter
        List<String> time = Arrays.asList(old_time.split(","));
        ContentValues cv = new ContentValues();
        int days = Integer.valueOf(time.get(0));
        int hours = Integer.valueOf(time.get(1));
        //if full time 0 or less than 0 day + 1
        if (hours-hour <= 0){
            cv.put("TIME",(Integer.valueOf(days)+1)+",12,rest");
        }
        else{
            cv.put("TIME",days+","+(Integer.valueOf(hours)-hour)+",awake");
        }
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    // wake up if the player status is rest than after sleep become awake
    public void wake_up() {
        //taking the previous database before replacing to the new one
        String old_time;
        old_time= load_time();
        //divide the String into arrays with comma splitter
        List<String> time = Arrays.asList(old_time.split(","));
        ContentValues cv = new ContentValues();
        int days = Integer.valueOf(time.get(0));
        int hours = Integer.valueOf(time.get(1));
        if (time.get(2).equals("awake")){
            cv.put("TIME",days+1+","+hours+",awake");
        }
        else
            cv.put("TIME",days+","+hours+",awake");
        cv.put("CURHEALTH",load_health());
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load current health
    public int load_curhealth(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT CURHEALTH FROM PLAYER WHERE (ID = 0)", null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return 0;
    }
    //this is for potion and any other consumables
    public void change_curhealth (int itemPoint) {
        //taking the previous database before replacing to the new one
        int load_oldcurhealt;
        load_oldcurhealt= load_curhealth();
        int old_health;
        old_health = load_health();
        ContentValues cv = new ContentValues();
        if (load_oldcurhealt+itemPoint>old_health){
            cv.put("CURHEALTH",old_health);
        }
        else
            cv.put("CURHEALTH", load_oldcurhealt+itemPoint);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
    //load full current health after sleep
    public void full_loadcurhealth () {
        //taking the previous database before replacing to the new one
        int old_health;
        old_health = load_health();
        ContentValues cv = new ContentValues();
        cv.put("CURHEALTH", old_health);
        this.getWritableDatabase().update("PLAYER", cv, "ID = 0" , null);
    }
}
