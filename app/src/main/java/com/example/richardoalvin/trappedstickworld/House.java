package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class House extends AppCompatActivity {

    //declaration
    public String[] text;
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    Handler blink = new Handler();
    Database connect;
    ImageButton Back;
    ImageButton bedButton;
    TextView moveText;
    RelativeLayout houseView;
    ImageView statsButton;
    TextView agiText;
    TextView strText;
    TextView intText;
    TextView healthView;
    TextView bagItem;
    LinearLayout sview;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        //connecting to database
        connect = new Database(this,"",null,1);
        //load text json
        loadJson();
        //initialisation
        bagItem = (TextView) findViewById(R.id.bag);
        healthView = (TextView) findViewById(R.id.health);
        statsButton = (ImageButton) findViewById(R.id.stats);

        sview = (LinearLayout) findViewById(R.id.statsView);
        agiText = (TextView) findViewById(R.id.agi);
        strText = (TextView) findViewById(R.id.str);
        intText = (TextView) findViewById(R.id.intel);

        moveText = (TextView) findViewById(R.id.MovingText);
        bedButton = (ImageButton) findViewById(R.id.bed);
        Back = (ImageButton) findViewById(R.id.back);
        houseView = (RelativeLayout) findViewById(R.id.house);
        //divining all of the declared object
        //oncliks
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.position_change(0);
                Main();
            }
        });
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        //back is not visible when status is rest
        if (timeArray.get(2).equals("rest")){
            Back.setVisibility(View.INVISIBLE);
        }
        //onclick stats button
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //divining all of the declared object
                String stats = connect.load_stats();
                List<String> statsArray = Arrays.asList(stats.split(","));
                agiText.setText("AGI : " + statsArray.get(0));
                strText.setText("STR : " + statsArray.get(1));
                intText.setText("INT : " + statsArray.get(2));
                bagItem.setText(connect.load_item());
                healthView.setText("Health : " + connect.load_curhealth()+"/"+ connect.load_health());
                if (sview.getVisibility() == View.INVISIBLE){
                    sview.setVisibility(View.VISIBLE);
                }
                else{
                    sview.setVisibility(View.INVISIBLE);
                }
            }
        });
        //sleep
        bedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                houseView.setBackgroundColor(Color.BLACK);
                blink.postDelayed(blinkRunnable,500);
                if (myTimer!=null){
                    myTimer.cancel();
                }
                connect.full_loadcurhealth();
                connect.wake_up();
                if (Back.getVisibility()==View.INVISIBLE){
                    Back.setVisibility(View.VISIBLE);
                }
            }
        });
        TimerTask myTask = new TimerTask() {
            public void run() {
                update_text(); // text update method
            }
        };
        //timer for moving text
        myTimer.schedule(myTask,0,2000);
    }
    Runnable blinkRunnable = new Runnable() {
        @Override
        public void run() {
            houseView.setBackgroundResource(R.drawable.background_house);
        }
    };
    // runnable that will change text inside text array
    final Runnable myRunnable = new Runnable() {
        public void run() {
            moveText.setText(text[i -1]); // update text
        }
    };

    // update_text method related to a Runnable
    private void update_text() {
        if (connect.text_load() == 1) {
            if (i < text.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable); // relate this to a Runnable
            } else {
                myTimer.cancel();// stop the timer
                connect.add_money(10);
                connect.quest_change(1);
                connect.change_text(2);
            }
        }
    }
    //moving to main
    public void Main(){
        try {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //load json
    public void loadJson() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.text);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        //read all of json file
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }
    //parsing json tree
    private void parseJson(String s) {
        try{
            //goes to all branches
            JSONObject root = new JSONObject(s);
            JSONObject txt = root.getJSONObject("text");
            JSONArray stories = txt.getJSONArray("story");
            for (int n=0; n<stories.length();n++){
                JSONObject story = stories.getJSONObject(n);
                if (story.getInt("id")==connect.text_load()){
                    JSONArray storylines = story.getJSONArray("storyline");
                    //adding text to array
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
