package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Gym extends AppCompatActivity {

    ImageButton treadmill;
    ImageButton book;
    ImageButton dumbbell;
    ImageButton training;
    TextView moveText;
    Database connect;
    ImageButton Back;
    TextView CurTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        connect = new Database(this,"",null,1);
        moveText = (TextView)findViewById(R.id.MovingText);
        book = (ImageButton)findViewById(R.id.book);
        dumbbell = (ImageButton)findViewById(R.id.dumbbell);
        training = (ImageButton)findViewById(R.id.training);
        CurTime = (TextView)findViewById(R.id.time);
        treadmill = (ImageButton)findViewById(R.id.treadmill);
        Back = (ImageButton) findViewById(R.id.back);
        CurTime.setText(connect.load_time()+":00");
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(2);
            }
        });
        dumbbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(1);
            }
        });
        treadmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(1);
            }
        });
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    public void NextMap(){
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sleep(){
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if(timeArray.get(2).equals("rest")){
            House();
        }
    }
    public void House(){
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void train(int type){
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        if (type==0) {
            connect.add_stats(Integer.valueOf(statsArray.get(0)) / 6, 0, 0);
        }
        if(type==1){
            connect.add_stats(0, Integer.valueOf(statsArray.get(1)) / 6, 0);
        }
        else
            connect.add_stats(0,0,Integer.valueOf(statsArray.get(2)) / 6);
        connect.change_time(4);
        CurTime.setText(connect.load_time()+":00");
        sleep();
    }
}
