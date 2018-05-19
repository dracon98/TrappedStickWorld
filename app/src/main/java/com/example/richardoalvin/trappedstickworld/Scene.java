package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Scene extends AppCompatActivity {

    //declaration
    MediaPlayer dmgSound;
    MediaPlayer hitSound;
    ImageButton scissorBtn;
    ImageButton rockBtn;
    ImageButton paperBtn;
    TextView healthText;
    TextView enemyHealth;
    Database connect;
    int enemyhealth;
    int Agi;
    int Str;
    int Int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        //database connection
        connect = new Database(this,"",null,1);
        //initialisation
        scissorBtn = (ImageButton)findViewById(R.id.scissor);
        rockBtn = (ImageButton)findViewById(R.id.rock);
        paperBtn = (ImageButton)findViewById(R.id.paper);
        healthText = (TextView) findViewById(R.id.health);
        enemyHealth = (TextView)findViewById(R.id.enemyhealth);
        //defining declared object
        healthText.setText(connect.load_curhealth()+"/"+connect.load_health());
        enemyhealth=100;
        enemyHealth.setText(enemyhealth+"/100");
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        Agi = Integer.valueOf(statsArray.get(0));
        Str = Integer.valueOf(statsArray.get(1));
        Int = Integer.valueOf(statsArray.get(2));
        //onclicks
        rockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(1);
                quit();
            }
        });
        scissorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(3);
                quit();
            }
        });
        paperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(2);
                quit();
            }
        });
    }
    //attack move
    // rock paper scissor logics
    private void attack (int type){
        Random r = new Random();
        int enemyMoves= r.nextInt(3-1+1)+1;
        if (type==1){
            if (enemyMoves==2){connect.change_curhealth(-(Int-5));}
            if (enemyMoves==3){enemyhealth = enemyhealth-Str;}
        }
        if(type==2){
            if (enemyMoves==1){enemyhealth = enemyhealth-Int;}
            if (enemyMoves==3){connect.change_curhealth(-(Agi-5));}
        }
        if(type==3){
            if (enemyMoves==1){connect.change_curhealth(-(Str-5));}
            if (enemyMoves==2){enemyhealth = enemyhealth-Agi;}
        }
        hitEnemy();
        enemyHealth.setText(enemyhealth+"/100");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 0.5s = 500ms
                hitPlayer();
                enemyHealth.setText(connect.load_curhealth() + "/" + connect.load_health());
            }
        }, 500);
    }
    //quit when cur health <= 0 or enemy curhealth<=0
    private void quit(){
        if (connect.load_curhealth()<=0){
            connect.add_stats(-Str/10,-Agi/10,-Int/10);
            connect.change_time(12);
            sleep();
        }
        if (enemyhealth<=0){
            if (connect.load_quest()==3){
                connect.quest_change(4);
                connect.add_stats(3,3,3);
            }
            connect.add_money(50);
            connect.change_time(3);
            Gym();
        }
    }
    //go to house
    public void House(){
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //back to sleep when status rest
    public void sleep(){
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if(timeArray.get(2).equals("rest")){
            House();
        }
    }
    //go to gym
    public void Gym(){
        try {
            Intent k = new Intent(this, Gym.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void hitPlayer(){
        hitSound = MediaPlayer.create(this,R.raw.highhit);
        hitSound.start();
    }
    private void hitEnemy(){
        dmgSound = MediaPlayer.create(this,R.raw.grassythud);
        dmgSound.start();
    }
}
