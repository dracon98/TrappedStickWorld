package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class WeaponStore extends AppCompatActivity {

    ImageButton knife;
    ImageButton axe;
    ImageButton knuckle;
    ImageButton gun;
    TextView moveText;
    Database connect;
    ImageButton Back;
    TextView Money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon_store);
        connect = new Database(this,"",null,1);
        moveText = (TextView)findViewById(R.id.MovingText);
        knife = (ImageButton)findViewById(R.id.knife);
        knuckle = (ImageButton)findViewById(R.id.knuckle);
        axe = (ImageButton)findViewById(R.id.axe);
        Money = (TextView)findViewById(R.id.money);
        gun = (ImageButton)findViewById(R.id.gun);
        Back = (ImageButton) findViewById(R.id.back);
        Money.setText("$ "+connect.load_money());
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("KNIFE",100);
            }
        });
        axe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("AXE",400);
            }
        });
        gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("GUN",600);
            }
        });
        knuckle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("KNUCKLE",250);
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
    public void item(String type, int price){
        if (checkItem(type)==0) {
            if (connect.load_money() - price < 0) {
                moveText.setText("You dont have enough money to buy this");
            } else {
                connect.add_item(type);
                connect.add_money(-(price));
                connect.change_time(1);
                Money.setText("$ " + connect.load_money());
                sleep();
            }
        }
    }
    public int checkItem(String type){
        String items = connect.load_item();
        List<String> itemsArray = Arrays.asList(items.split(","));
        if (itemsArray.size()!=0) {
            for (int i = 0; i < itemsArray.size(); i++) {
                if (itemsArray.get(i).equals(type)) {
                    moveText.setText("You already have this item");
                    return 1;
                }
            }
        }
        return 0;
    }
}
