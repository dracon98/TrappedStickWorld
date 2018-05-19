package com.example.richardoalvin.trappedstickworld;

import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    //declaration
    Button newGame;
    Database connect;
    Button loadGame;
    MediaPlayer backgroundsong;
    MediaPlayer buttonclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //background song oncreate play
        backgroundsong = MediaPlayer.create(this,R.raw.garagebandbackground);
        backgroundsong.start();
        backgroundsong.setLooping(true);
        //database connection
        connect = new Database(this,"",null,1);
        //defining declared object
        newGame = (Button)findViewById(R.id.newG);
        loadGame = (Button)findViewById(R.id.con);
        //onclick
        //setup database and turn off the background song
        //move to custom class
        newGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                connect.setup();
                backgroundsong.stop();
                MoveToCustom();
            }
        });
        //if there is no data reject the login
        loadGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (connect.load() == 0){
                    blink();
                }
                else{
                    //move to main
                    MovetoMain();
                    backgroundsong.stop();
                }
            }
        });

    }
    //intent
    //move to custom activity
    public void MoveToCustom(){
        try {
            Intent k = new Intent(this, CustomActivity.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //move to main activity
    public void MovetoMain(){
        try {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //blink rejection
    private void blink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout alertText = (LinearLayout) findViewById(R.id.alert);
                        if(alertText.getVisibility() == View.VISIBLE){
                            alertText.setVisibility(View.INVISIBLE);
                        }else{
                            alertText.setVisibility(View.VISIBLE);
                        }
                        blink();
                    }
                });
            }
        }).start();
    }
    //button click effect
}
