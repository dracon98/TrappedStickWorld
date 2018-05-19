package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class WeaponStore extends AppCompatActivity {
    //declaration object
    ImageButton knife;
    ImageButton axe;
    ImageButton knuckle;
    ImageButton gun;
    TextView moveText;
    Database connect;
    ImageButton Back;
    TextView Money;
    String[] questCompleted;
    Handler myHandler = new Handler();
    final Timer myTimer = new Timer();
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weapon_store);
        //connect database
        connect = new Database(this,"",null,1);
        //load json
        loadJson();
        //initialisation
        moveText = (TextView)findViewById(R.id.MovingText);
        knife = (ImageButton)findViewById(R.id.knife);
        knuckle = (ImageButton)findViewById(R.id.knuckle);
        axe = (ImageButton)findViewById(R.id.axe);
        Money = (TextView)findViewById(R.id.money);
        gun = (ImageButton)findViewById(R.id.gun);
        Back = (ImageButton) findViewById(R.id.back);
        //defining the object
        Money.setText("$ "+connect.load_money());
        if (connect.load_quest()==4){
            moveText.setText("Welcome to the weapon store!! Q: Buy a knife");
            connect.add_money(100);
        }
        else
            moveText.setText("Welcome to the weapon store!!");
        //onclick
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connect.load_quest()==4&&connect.text_load()==6){
                    connect.quest_change(5);
                    connect.add_stats(1,1,1);
                    connect.change_text(7);
                    TimerTask myTaskquest = new TimerTask()
                    {public void run() {
                        text_quest(); // text update method
                    }
                    };
                    myTimer.schedule(myTaskquest,0,2000);
                }
                item("knife",100);
            }
        });
        axe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("axe",400);
            }
        });
        gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("gun",600);
            }
        });
        knuckle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item("knuckle",250);
            }
        });
    }
    //move text
    private void text_quest(){
        if (i < questCompleted.length) {
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
            moveText.setText(questCompleted[i -1]); // update text
        }
    };
    //go to map
    public void NextMap(){
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //go to sleep if status equal to rest
    public void sleep(){
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if(timeArray.get(2).equals("rest")){
            House();
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
    //buying mechanism
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
    //check item first before buying
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
    //json load
    public void loadJson() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.text);//read json file
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        //move json file
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        try {
            JSONObject root = new JSONObject(s);
            //trace each branch of json tree
            JSONObject txt = root.getJSONObject("text");
            JSONArray stories = txt.getJSONArray("quest");
            for (int n = 0; n < stories.length(); n++) {
                JSONObject story = stories.getJSONObject(n);
                if (story.getInt("id") == connect.load_quest()) {
                    JSONArray storylines = story.getJSONArray("completed");
                    //add text to array
                    questCompleted = new String[storylines.length()];
                    for (int j = 0; j < storylines.length(); j++) {
                        questCompleted[j] = storylines.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
