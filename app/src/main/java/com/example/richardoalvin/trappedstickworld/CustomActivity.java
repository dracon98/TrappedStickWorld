package com.example.richardoalvin.trappedstickworld;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

import java.util.Random;

/*
 * Created by Richardo Alvin on 3/24/2018.
 */

public class CustomActivity extends AppCompatActivity {

    MediaPlayer Backgroundsong;
    private ImageButton back;
    private Button Start;
    private EditText Agi;
    private EditText Intel;
    private EditText Str;
    private ImageButton roll;
    private int j;
    private int n;
    private int m;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        //background song player
        //looping
        Backgroundsong = MediaPlayer.create(this,R.raw.custom);
        Backgroundsong.start();
        Backgroundsong.setLooping(true);
        //setting a connection to database
        connect = new Database(this,"",null,1);
        //initialisation
        back = (ImageButton) findViewById(R.id.bback);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Backgroundsong.stop();
                MoveToMenu();
            }
    });
        //initialisation
        Agi = (EditText) findViewById(R.id.agi);
        Str = (EditText) findViewById(R.id.str);
        Intel = (EditText) findViewById(R.id.intel);
        Agi.setEnabled(false);
        Str.setEnabled(false);
        Intel.setEnabled(false);
        RandomPoint();
        //onclick
        //change the number on edit text
        roll = (ImageButton) findViewById(R.id.rollId);
        roll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RandomPoint();
            }
        });
        //move to main activity which is the game itself
        Start = (Button) findViewById(R.id.startID);
        Start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Backgroundsong.stop();
                MoveToMain();
            }
        });
    }
    //moving to main menu
        public void MoveToMenu(){
            try {
                //intent moving
                Intent k = new Intent(this, MenuActivity.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    //moving to main
        public void MoveToMain(){
            try {
                //intent moving
                Intent k = new Intent(this, MainActivity.class);
                connect.add_stats(j,n,m);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        //randomise stats point
        public  void RandomPoint(){
            //algorithm
            Random r = new Random();
            int i = r.nextInt(10) + 1;
            int o = r.nextInt(10) + 1;
            int in = r.nextInt(10) + 1;
            j = 21 - o - i;
            n = 21 - in - i;
            m = 21 - in - o;
            //renew textview
            Agi.setText(String.valueOf(j));
            Str.setText(String.valueOf(n));
            Intel.setText(String.valueOf(m));
        }
}
