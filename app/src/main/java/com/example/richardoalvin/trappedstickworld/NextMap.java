package com.example.richardoalvin.trappedstickworld;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class NextMap extends AppCompatActivity {

    //object declaration
    Handler loops = new Handler();
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    MediaPlayer backgroundsong1;
    public float X;
    TextView agiText;
    TextView strText;
    TextView intText;
    TextView moveText;
    TextView bagItem;
    TextView dayText;
    ImageView statsButton;
    Button foodhall;
    ImageButton weaponstore;
    Button Left;
    Button Right;
    int i = 0;
    int speed;
    int move=40;
    LinearLayout sview;
    ImageButton gym;
    Dialog dialog;
    TextView healthView;
    public String[] text;
    public ImageView main;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_map);
        backgroundsong1 = MediaPlayer.create(this,R.raw.cartoontheme);
        backgroundsong1.start();
        backgroundsong1.setLooping(true);
        //database connection
        connect = new Database(this,"",null,1);
        //json file
        loadJson();

        //initialisation
        bagItem = (TextView) findViewById(R.id.bag);
        healthView = (TextView) findViewById(R.id.health);
        statsButton = (ImageButton) findViewById(R.id.stats);
        TextView Money = (TextView) findViewById(R.id.money);
        sview = (LinearLayout) findViewById(R.id.statsView);
        agiText = (TextView) findViewById(R.id.agi);
        strText = (TextView) findViewById(R.id.str);
        intText = (TextView) findViewById(R.id.intel);
        main = (ImageView) findViewById(R.id.StickMan);
        Left = (Button) findViewById(R.id.left);
        Right = (Button) findViewById(R.id.right);
        gym = (ImageButton) findViewById(R.id.gymdoor);
        foodhall = (Button) findViewById(R.id.fooddoor);
        weaponstore = (ImageButton)findViewById(R.id.shopdoor);
        moveText = (TextView) findViewById(R.id.MovingText);
        dayText = (TextView)findViewById(R.id.day);
        //defining declared object
        main.setY(300);
        main.setX(connect.load_position());
        sleep();
        String days = connect.load_time();
        List<String> timesArray = Arrays.asList(days.split(","));
        int hour = Integer.valueOf(timesArray.get(1));
        dayText.setText("Day "+timesArray.get(0)+", "+ (24-(hour*2))+":00");
        Money.setText("$ "+connect.load_money() );
        //diferent text interface for different text load and load quest
        if (connect.text_load()==3){
            move = 0;
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

        //find speed
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        speed = Integer.valueOf(statsArray.get(0))/4;
        //onclicks
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gym.getX() - main.getX() < 200 && gym.getX() - main.getX() > -200) {
                    connect.position_change((int)main.getX());
                    backgroundsong1.stop();
                    Gym();
                }
            }
        });
        foodhall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodhall.getX() - main.getX() < 200 && foodhall.getX() - main.getX() > -200) {
                    connect.position_change((int)main.getX());
                    backgroundsong1.stop();
                    Foodhall();
                }
            }
        });
        weaponstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weaponstore.getX() - main.getX() < 200 && weaponstore.getX() - main.getX() > -200) {
                    connect.position_change((int)main.getX());
                    backgroundsong1.stop();
                    WeaponStore();
                }
            }
        });
        //stats button
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load old stats
                // defining declared object
                String stats = connect.load_stats();
                List<String> statsArray = Arrays.asList(stats.split(","));
                agiText.setText("AGI : " + statsArray.get(0));
                strText.setText("STR : " + statsArray.get(1));
                intText.setText("INT : " + statsArray.get(2));
                bagItem.setText(connect.load_item());
                connect.change_health();
                healthView.setText("Health : " + connect.load_curhealth()+"/"+ connect.load_health());
                if (sview.getVisibility() == View.INVISIBLE){
                    sview.setVisibility(View.VISIBLE);
                    Left.setVisibility(View.INVISIBLE);
                    Right.setVisibility(View.INVISIBLE);
                    foodhall.setVisibility(View.INVISIBLE);
                }
                else{
                    sview.setVisibility(View.INVISIBLE);
                    Left.setVisibility(View.VISIBLE);
                    Right.setVisibility(View.VISIBLE);
                    foodhall.setVisibility(View.VISIBLE);
                }
            }
        });
        //onclick
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
        myTimer.schedule(myTask,0,2000);
    }
    //go to left
    private void mLeft(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        X = imgDisplay.getX();
        if (X-move-speed<-20){
            connect.position_change(moveText.getWidth()-120);
            connect.change_time(1);
            backgroundsong1.stop();
            BackToMain();
        }
        else {
            imgDisplay.setX(X - move-speed);
            X = X - move-speed;
        }
    }
    //go to right
    private void mRight(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        X = imgDisplay.getX();
        imgDisplay.setX(X+move+speed);
        X = X+move+speed;
        if (X>moveText.getWidth()-80){
            String items = connect.load_item();
            List<String> itemsArray = Arrays.asList(items.split(","));
            if (!itemsArray.get(0).equals("NONE")) {
                connect.change_time(1);
                backgroundsong1.stop();
                Waves();
            }
            else {
                //waves without weapon
                moveText.setText("WARNING: You cannot go to challenge the waves without any weapon");
                main.setX(sview.getWidth()-100);
            }
        }
    }
    //change position state
    private void changeR(){
        if (main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand).getConstantState()
                ||main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand1).getConstantState())
            main.setBackgroundResource(R.drawable.walk);
        else
            main.setBackgroundResource(R.drawable.stand);
    }
    //change position state
    private void changeL(){
        if (main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand).getConstantState()
                ||main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand1).getConstantState())
            main.setBackgroundResource(R.drawable.walk1);
        else
            main.setBackgroundResource(R.drawable.stand1);
    }
    //onclick
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
        //if text load database equal to
        if (connect.text_load() == 3) {
            connect.change_curhealth(connect.load_health());
            if (i < text.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable); // relate this to a Runnable
            } else {
                myTimer.cancel(); // stop the timer
                move = 40;
                connect.change_text(4);
                return;
            }
        }
    }
    //move to main activity
    public void BackToMain(){
        try {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sleep() {
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if (timeArray.get(2).equals("rest")) {
            House();
        }
    }
    public void House(){
        try {
            Intent k = new Intent(this, Foodhall.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //Move to food hall
    public void Foodhall(){
        try {
            Intent k = new Intent(this, Foodhall.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //move to weapon store
    public void WeaponStore(){
        try {
            Intent k = new Intent(this, WeaponStore.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //move to gym
    public void Gym(){
        try {
            Intent k = new Intent(this, Gym.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //move to waves
    public void Waves(){
        try {
            Intent k = new Intent(this, Waves.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

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
