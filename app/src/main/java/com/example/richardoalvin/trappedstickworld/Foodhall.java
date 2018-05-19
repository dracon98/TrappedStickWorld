package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Foodhall extends AppCompatActivity {

    //declaration
    ImageButton Spotion;
    ImageButton Bpotion;
    ImageButton drink;
    ImageButton Burger;
    TextView moveText;
    Database connect;
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    ImageButton Back;
    int i = 0;
    TextView Money;
    String[] questComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodhall);
        //connect to database
        connect = new Database(this,"",null,1);
        //load json
        loadJson();
        //initialisation
        moveText = (TextView)findViewById(R.id.MovingText);
        Spotion = (ImageButton)findViewById(R.id.spotion);
        Bpotion = (ImageButton)findViewById(R.id.bpotion);
        drink = (ImageButton)findViewById(R.id.shealth);
        Money = (TextView)findViewById(R.id.money);
        Burger = (ImageButton)findViewById(R.id.burger);
        Back = (ImageButton) findViewById(R.id.back);
        //setting up declared object
        Money.setText("$ "+connect.load_money());
        //different outcome will be found depending to the load quest database
        if (connect.load_quest()==2){
            connect.add_money(40);
            moveText.setText("Welcome to Food Hall!! Q: Drink small potion");
        }
        else
            moveText.setText("Welcome to Food Hall!!");
        //on click for the image buttons
        // there are 2 function connected to them
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        //burger and drink connected to food function
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
        //potions connected to potion function
        Spotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connect.load_quest()==2&&connect.text_load()==4){
                    connect.quest_change(3);
                    connect.change_text(5);
                    connect.add_stats(1,1,1);
                    TimerTask myTaskquest = new TimerTask()
                    {public void run() {
                        text_quest(); // text update method
                    }
                    };
                    myTimer.schedule(myTaskquest,0,2000);
                }
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
    private void text_quest(){
        if (i < questComplete.length) {
            i++;
            // text_data.setText(String.valueOf(i)); = avoid the RunTime error
            myHandler.post(myRunnableQ); // relate this to a Runnable
        } else {
            myTimer.cancel(); // stop the timer
            return;
        }
    }
    final Runnable myRunnableQ = new Runnable() {
        public void run() {
            moveText.setText(questComplete[i -1]); // update text
        }
    };
    //next map go to the previous next map
    public void NextMap(){
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    // sleep checking up whether the status of stickman is rest or not
    // if yes go to house
    public void sleep(){
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if(timeArray.get(2).equals("rest")){
            House();
        }
    }
    //go back to the house
    public void House(){
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //food function - the result of buying food
    public void food(int price,int effect){
        //if doesnt have enough money then
        if (connect.load_money() - price < 0) {
            moveText.setText("You dont have enough money to buy this");
        } else {
            //adding result to database
            connect.add_money(-price);
            connect.change_time(1);
            connect.change_curhealth(effect);
            Money.setText("$ "+connect.load_money());
            sleep();
        }
    }
    //food function - the result of buying potion
    public void potion(int price, int health, int stats,int time){
        if (connect.load_money() - price  < 0) {
            moveText.setText("You dont have enough money to buy this");
        } else {
            //if have enough money then adding result to database
            connect.add_money(-price);
            connect.add_stats(stats,stats,stats);
            connect.change_health();
            connect.change_curhealth(health);
            connect.change_time(time);
            Money.setText("$ "+connect.load_money());
            sleep();
        }
    }
    //load to json
    public void loadJson() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.text);
        //scanning the json file
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        //reading all the json file to a string builder
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        try {
            //finding the path for each branches
            JSONObject root = new JSONObject(s);
            JSONObject txt = root.getJSONObject("text");
            JSONArray stories = txt.getJSONArray("quest");
            for (int n = 0; n < stories.length(); n++) {
                JSONObject story = stories.getJSONObject(n);
                //find the same id then
                if (story.getInt("id") == connect.load_quest()) {
                    JSONArray storylines = story.getJSONArray("completed");
                    //adding text to the array
                    questComplete = new String[storylines.length()];
                    for (int j = 0; j < storylines.length(); j++) {
                        questComplete[j] = storylines.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
