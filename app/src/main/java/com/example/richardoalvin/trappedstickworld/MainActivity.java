package com.example.richardoalvin.trappedstickworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    //declaration
    Handler loops = new Handler();
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    MediaPlayer backgroundsong;
    public float X;

    TextView dayText;
    ImageView statsButton;
    TextView agiText;
    TextView strText;
    TextView intText;
    TextView moveText;
    TextView bagItem;
    LinearLayout sview;
    ImageButton office;
    TextView healthView;
    Button Left;
    Button Right;
    int i = 0;
    int speed;
    int lane;
    ImageButton house;
    Dialog dialog;
    String[] text;
    public ImageView main;
    int move=40;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //database connection
        connect = new Database(this,"",null,1);
        //load json file
        loadJson();
        //media player that will go off after going to venue
        backgroundsong = MediaPlayer.create(this,R.raw.donkeykongshorty);
        backgroundsong.start();
        backgroundsong.setLooping(true);



    //Initialisation
        bagItem = (TextView) findViewById(R.id.bag);
        healthView = (TextView) findViewById(R.id.health);
        statsButton = (ImageButton) findViewById(R.id.stats);

        sview = (LinearLayout) findViewById(R.id.statsView);
        agiText = (TextView) findViewById(R.id.agi);
        strText = (TextView) findViewById(R.id.str);
        intText = (TextView) findViewById(R.id.intel);
        TextView Money = (TextView) findViewById(R.id.money);
        Left = (Button) findViewById(R.id.left);
        Right = (Button) findViewById(R.id.right);
        house = (ImageButton) findViewById(R.id.HouseDoor);
        office = (ImageButton) findViewById(R.id.OfficeDoor);
        moveText = (TextView) findViewById(R.id.MovingText);
        dayText = (TextView)findViewById(R.id.day);
        main = (ImageView) findViewById(R.id.StickMan);

        //defining declared objects
        main.setY(300);

        main.setX(connect.load_position());
        connect.change_health();
        String days = connect.load_time();
        List<String> timesArray = Arrays.asList(days.split(","));
        int hour = Integer.valueOf(timesArray.get(1));
        dayText.setText("Day "+timesArray.get(0)+", "+ (24-(hour*2))+":00");
        Money.setText("$ "+connect.load_money() );
        //if condition of text view interface
        if (connect.text_load()==0){
            move = 0;
        }
        if (connect.load_quest()==1){
            moveText.setText("Q: You need to go to office and and work once");
        }
        if (connect.load_quest()==2){
            moveText.setText("Q: You need to go to next town by moving to the right");
        }
        if (connect.load_quest()==3){
            moveText.setText("Q: You need to go to gym and beat the enemy on the training");
        }
        if (connect.load_quest()==4){
            moveText.setText("Q: You need to go to weapon shop and buy knife");
        }
        if (connect.load_quest()==5){
            moveText.setText("Q: You need to increase you stats and finish all the monster waves");
        }
        //defining speed
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        speed = Integer.valueOf(statsArray.get(0))/4;
        //onclicks
        //venue can only be clicked when near
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (house.getX() - main.getX() < 200 && house.getX() - main.getX() > -200) {
                    backgroundsong.stop();
                    House();
                }
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (office.getX() - main.getX() < 200 && office.getX() - main.getX() > -200) {
                    connect.position_change((int)main.getX());
                    backgroundsong.stop();
                    Office();
                }
            }
        });
        //stats button all of the button will become invisible when this button is clicked
        //defining the objcts
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stats = connect.load_stats();
                List<String> statsArray = Arrays.asList(stats.split(","));
                agiText.setText("AGI : " + statsArray.get(0));
                strText.setText("STR : " + statsArray.get(1));
                intText.setText("INT : " + statsArray.get(2));
                bagItem.setText(connect.load_item());
                healthView.setText("Health : " + connect.load_curhealth()+"/"+ connect.load_health());
                if (sview.getVisibility() == View.INVISIBLE){
                    sview.setVisibility(View.VISIBLE);
                    Left.setVisibility(View.INVISIBLE);
                    Right.setVisibility(View.INVISIBLE);
                }
                else{
                    sview.setVisibility(View.INVISIBLE);
                    Left.setVisibility(View.VISIBLE);
                    Right.setVisibility(View.VISIBLE);
                }
            }
        });
        //movement left
        Left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        loops.postDelayed(Lmove, 100);
                        changeL();
                        break;
                    case MotionEvent.ACTION_UP:
                        loops.removeCallbacks(Lmove);
                        break;
                }
                return false;
            }
            Runnable Lmove = new Runnable() {
                @Override
                public void run() {
                    mLeft();
                    changeL();
                    loops.postDelayed(this,180);
                }
            };
        });
        //movement right
        Right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loops.postDelayed(Rmove, 100);
                        changeR();
                        break;
                    case MotionEvent.ACTION_UP:

                        loops.removeCallbacks(Rmove);
                        break;
                }
                return false;
            }
            Runnable Rmove = new Runnable() {
                @Override
                public void run() {
                    mRight();
                    changeR();
                    loops.postDelayed(this,180);
                }
            };
        });
            TimerTask myTask = new TimerTask() {
                public void run() {
                    update_text(); // text update method
                }
            };
            myTimer.schedule(myTask, 0, 2000);
    }
    //onclick
    private void mLeft(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        X = imgDisplay.getX();
        if (X-move-speed<0){}
        else {
            imgDisplay.setX(X - move-speed);
            X = X - move-speed;
        }
    }
    //onclick
    private void mRight(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        X = imgDisplay.getX();
        imgDisplay.setX(X+move+speed);
        X = X+move+speed;
        if (X>moveText.getWidth()-80){
            connect.position_change(0);
            connect.change_time(1);
            backgroundsong.stop();
            next_map();
        }
    }
    // changing position
    private void changeR(){
        if (main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand).getConstantState()
                ||main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand1).getConstantState())
            main.setBackgroundResource(R.drawable.walk);
        else
            main.setBackgroundResource(R.drawable.stand);
    }
    //changing position
    private void changeL(){
        if (main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand).getConstantState()
                ||main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand1).getConstantState())
            main.setBackgroundResource(R.drawable.walk1);
        else
            main.setBackgroundResource(R.drawable.stand1);
    }
    public void moveRight(View view){
        mRight();
    }
    public void moveLeft (View view){
        mLeft();
    }
    final Runnable myRunnable = new Runnable() {
        public void run() {
            moveText.setText(text[i -1]); // update text
        }
    };

    // update_text method related to a Runnable
    private void update_text() {
        if (connect.text_load() == 0) {
            connect.change_curhealth(connect.load_health());
            if (i < text.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable); // relate this to a Runnable
            } else {
                connect.change_text(1);
                move = 40;
                myTimer.cancel(); // stop the timer
                return;
            }
        }
    }
    //move to house
        public void House(){
            try {
                Intent k = new Intent(this, House.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        //move to office
    public void Office(){
        try {
            Intent k = new Intent(this, Office.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //move to next map
    public void next_map(){
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //load json file
    public void loadJson() {
        Resources res = getResources();
        //read json file
        InputStream is = res.openRawResource(R.raw.text);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        //move json file to string builder
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }
    //parsing the string string builder
    private void parseJson(String s) {
        try{
            //going down through each of the branches
            JSONObject root = new JSONObject(s);
            JSONObject txt = root.getJSONObject("text");
            JSONArray stories = txt.getJSONArray("story");
            for (int n=0; n<stories.length();n++){
                JSONObject story = stories.getJSONObject(n);
                //when id equal to text load then
                if (story.getInt("id")==connect.text_load()){
                    JSONArray storylines = story.getJSONArray("storyline");
                    //add text to text array
                    text = new String[storylines.length()];
                    for (int j =0; j<storylines.length();j++){
                        text[j] = storylines.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
