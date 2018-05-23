package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Win extends AppCompatActivity {

    ImageButton backbtn;
    ImageButton nextbtn;
    TextView text;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        connect = new Database(this,"",null,1);
        backbtn = (ImageButton)findViewById(R.id.back);
        nextbtn = (ImageButton)findViewById(R.id.right);
        text = (TextView)findViewById(R.id.days);
        String times = connect.load_time();
        List<String> timesArray = Arrays.asList(times.split(","));
        int days = Integer.valueOf(timesArray.get(0));
        text.setText(days+" days");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu();
            }
        });
    }
    public void NextMap() {
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Menu() {
        try {
            Intent k = new Intent(this, MenuActivity.class);
            startActivity(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
