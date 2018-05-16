package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class Foodhall extends AppCompatActivity {

    ImageButton Spotion;
    ImageButton Bpotion;
    ImageButton drink;
    ImageButton Burger;
    TextView moveText;
    Database connect;
    ImageButton Back;
    TextView Money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodhall);
        connect = new Database(this,"",null,1);
        moveText = (TextView)findViewById(R.id.MovingText);
        Spotion = (ImageButton)findViewById(R.id.spotion);
        Bpotion = (ImageButton)findViewById(R.id.bpotion);
        drink = (ImageButton)findViewById(R.id.shealth);
        Money = (TextView)findViewById(R.id.money);
        Burger = (ImageButton)findViewById(R.id.burger);
        Back = (ImageButton) findViewById(R.id.back);
        Money.setText("$ "+connect.load_money());
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        Burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food(20,25);
            }
        });
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                food(10,10);
            }
        });
        Spotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                potion(40,5,1,2);
            }
        });
        Bpotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                potion(999,connect.load_health(),33,6);
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
    public void food(int price,int effect){
        if (connect.load_money() - price < 0) {
            moveText.setText("You dont have enough money to buy this");
        } else {
            connect.add_money(-price);
            connect.change_time(1);
            connect.change_curhealth(effect);
            Money.setText("$ "+connect.load_money());
            sleep();
        }
    }
    public void potion(int price, int health, int stats,int time){
        if (connect.load_money() - price  < 0) {
            moveText.setText("You dont have enough money to buy this");
        } else {
            connect.add_money(-price);
            connect.add_stats(stats,stats,stats);
            connect.change_health();
            connect.change_curhealth(health);
            connect.change_time(time);
            Money.setText("$ "+connect.load_money());
            sleep();
        }
    }
}
