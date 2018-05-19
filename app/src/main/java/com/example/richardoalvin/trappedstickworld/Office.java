package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

public class Office extends AppCompatActivity {

    //declaration object
    public String[] text;
    public String[] questComplete = {};
    final Timer myTimer = new Timer();
    final Timer myTimer2 = new Timer();
    Handler myHandler = new Handler();
    Handler myHandler2 = new Handler();
    TextView moveText;
    Database connect;
    TextView Money;
    ImageButton work;
    private int i = 0;
    private int j = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        //database connection
        connect = new Database(this,"",null,1);
        //load json file
        loadJson();
        //initialisation'
        work = (ImageButton) findViewById(R.id.work);
        Money = (TextView) findViewById(R.id.money);
        moveText = (TextView) findViewById(R.id.MovingText);
        ImageButton Back = (ImageButton) findViewById(R.id.back);
        Money.setText("$ "+connect.load_money());
        if (connect.text_load()<2)
        {
            moveText.setText("Its preferable to go to your house first before going to the office");
        }
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for first time
                if (connect.load_quest()==1&&(connect.text_load()==2||connect.text_load()==3)){
                    myTimer.cancel();
                    connect.quest_change(2);
                    connect.add_stats(1,1,1);
                    TimerTask myTaskquest = new TimerTask()
                    {public void run() {
                            text_quest(); // text update method
                    }
                    };
                    myTimer2.schedule(myTaskquest,0,2000);
                }
                connect.add_money(25);
                connect.change_time(4);
                String time = connect.load_time();
                List<String> timeArray = Arrays.asList(time.split(","));
                if(timeArray.get(2).equals("rest")){
                    House();
                }
                Money.setText("$ "+connect.load_money());
            }
        });
        TimerTask myTask = new TimerTask() {
            public void run() {
                update_text(); // text update method
            }
        };
        myTimer.schedule(myTask,0,2000);

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
    public void House(){
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //runnable that will change text inside text array
    final Runnable myRunnable = new Runnable() {
        public void run() {
            moveText.setText(text[i -1]); // update text
        }
    };
    //runnable that will change text inside text array
    final Runnable myRunnableQ = new Runnable() {
        public void run() {
            moveText.setText(questComplete[j -1]); // update text
        }
    };
    // update_text method related to a Runnable
    private void update_text() {
        if(connect.text_load()==2) {
            if (i < text.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable); // relate this to a Runnable
            } else {
                myTimer.cancel(); // stop the timer
                connect.change_text(3);
                return;
            }
        }
    }
    private void text_quest(){
        if (j < questComplete.length) {
            j++;
            // text_data.setText(String.valueOf(i)); = avoid the RunTime error
            myHandler2.post(myRunnableQ); // relate this to a Runnable
        } else {
            myTimer2.cancel(); // stop the timer
            return;
        }
    }
    //load json
    public void loadJson() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.text);
        //read json file
        Scanner scanner = new Scanner(is);
        //move json file to string builder
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        try {
            //open string string builder
            JSONObject root = new JSONObject(s);
            //tracing each of the branches tree from the root
            JSONObject txt = root.getJSONObject("text");
            JSONArray stories = txt.getJSONArray("story");
            //find storyline
            for (int n = 0; n < stories.length(); n++) {
                JSONObject story = stories.getJSONObject(n);
                if (story.getInt("id") == connect.load_quest()) {
                    JSONArray storylines = story.getJSONArray("storyline");
                    text = new String[storylines.length()];
                    for (int j = 0; j < storylines.length(); j++) {
                        text[j] = storylines.getString(j);
                    }
                }
            }
            //find completed quest
            JSONArray quests = txt.getJSONArray("quest");
            for (int n = 0; n < quests.length(); n++) {
                JSONObject quest = quests.getJSONObject(n);
                if (quest.getInt("id") == connect.text_load()) {
                    JSONArray questlines = quest.getJSONArray("completed");
                    questComplete = new String[questlines.length()];
                    for (int j = 0; j < questlines.length(); j++) {
                        questComplete[j] = questlines.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
