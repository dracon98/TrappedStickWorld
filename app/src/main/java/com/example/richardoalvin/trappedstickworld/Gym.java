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

public class Gym extends AppCompatActivity {

    //declaration
    ImageButton Treadmill;
    ImageButton Book;
    ImageButton Dumbbell;
    ImageButton Training;
    TextView moveText;
    Database connect;
    ImageButton Back;
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    int i;
    String[] quest_completed;
    TextView CurTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);
        //connection to database
        connect = new Database(this,"",null,1);
        //loading json file
        loadJson();
        //initialisation
        moveText = (TextView)findViewById(R.id.MovingText);
        Book = (ImageButton)findViewById(R.id.book);
        Dumbbell = (ImageButton)findViewById(R.id.dumbbell);
        Training = (ImageButton)findViewById(R.id.training);
        CurTime = (TextView)findViewById(R.id.time);
        Treadmill = (ImageButton)findViewById(R.id.treadmill);
        Back = (ImageButton) findViewById(R.id.back);
        //defining declared objects
        //if condition depending on load quest and text load database
        // if yes timer working
        if (connect.load_quest()==4&&connect.text_load()==5) {
            connect.change_text(6);
            TimerTask myTask = new TimerTask() {
                public void run() {
                    text_quest(); // text update method
                }
            };
            myTimer.schedule(myTask, 0, 2000);
        }
        String times = connect.load_time();
        List<String> timesArray = Arrays.asList(times.split(","));
        int hour = Integer.valueOf(timesArray.get(1));
        CurTime.setText("Time : "+ (24-(hour*2))+":00");
        //different interface for load quest
        if (connect.load_quest()==3){
            moveText.setText("Welcome to the training gym!! Q: beat the training enemy");
        }
        else
            moveText.setText("Welcome to the training gym!!");
        //onclicks
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextMap();
            }
        });
        // on click go to train function
        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(2);
            }
        });
        Dumbbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(1);
            }
        });
        Treadmill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                train(0);
            }
        });
        // on click go to training function
        Training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Training();
            }
        });
    }
    //go to next map
    public void NextMap(){
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //checking if the stick man status is rest
    //if yes go back to house
    public void sleep(){
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if(timeArray.get(2).equals("rest")){
            House();
        }
    }
    //go back to house
    public void House(){
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //go to Scene class
    public void Training(){
        try {
            Intent k = new Intent(this, Scene.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //connecting the previous status
    public void train(int type){
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        // if the user click treadmill,
        if (type==0) {
            connect.add_stats(Integer.valueOf(statsArray.get(0)) / 6, 0, 0);
        }
        //if the user click dumbbell
        if(type==1){
            connect.add_stats(0, Integer.valueOf(statsArray.get(1)) / 6, 0);
        }
        //if the user click books
        else
            connect.add_stats(0,0,Integer.valueOf(statsArray.get(2)) / 6);
        connect.change_time(4);
        String times = connect.load_time();
        List<String> timesArray = Arrays.asList(times.split(","));
        int hour = Integer.valueOf(timesArray.get(1));
        CurTime.setText("Time : "+ (24-(hour*2))+":00");
        sleep();
    }
    private void text_quest(){
        if (i < quest_completed.length) {
            i++;
            // text_data.setText(String.valueOf(i)); = avoid the RunTime error
            myHandler.post(myRunnableQ); // relate this to a Runnable
        } else {
            myTimer.cancel(); // stop the timer
            return;
        }
    }
    //runnable function
    final Runnable myRunnableQ = new Runnable() {
        public void run() {
            moveText.setText(quest_completed[i -1]); // update text
        }
    };
    //load json
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
                    quest_completed = new String[storylines.length()];
                    for (int j = 0; j < storylines.length(); j++) {
                        quest_completed[j] = storylines.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
