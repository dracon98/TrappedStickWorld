package com.example.richardoalvin.trappedstickworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    Handler loops = new Handler();
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    public float X;
    TextView agiText;
    TextView strText;
    TextView intText;
    TextView moveText;
    TextView bagItem;
    ImageView statsButton;
    Button Left;
    Button Right;
    int i = 0;
    LinearLayout sview;
    Dialog dialog;
    public String[] text = {"\"You: Ahh Where is this? \"",
            "Hey Welcome to the StickWorld and this is gonna be your new house now",
            "You: Me? I cant even remember anything about myself...",
            "Well you should be able to adapt with the condition here and you might be able to remember your past",
            "Go on check you house you may find something or direction"};
    public ImageView main;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (ImageView) findViewById(R.id.StickMan);
        main.setY(300);
        bagItem = (TextView) findViewById(R.id.bag);
        statsButton = (ImageButton) findViewById(R.id.stats);
        TextView Money = (TextView) findViewById(R.id.money);
        sview = (LinearLayout) findViewById(R.id.statsView);
        agiText = (TextView) findViewById(R.id.agi);
        strText = (TextView) findViewById(R.id.str);
        intText = (TextView) findViewById(R.id.intel);
    //Player movement coding
        connect = new Database(this,"",null,1);
        Left = (Button) findViewById(R.id.left);
        Right = (Button) findViewById(R.id.right);
        ImageButton house = (ImageButton) findViewById(R.id.HouseDoor);
        ImageButton office = (ImageButton) findViewById(R.id.OfficeDoor);
        moveText = (TextView) findViewById(R.id.MovingText);
        Money.setText("$ "+connect.load_money() );
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                House();
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Office();
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stats = connect.load_stats();
                List<String> statsArray = Arrays.asList(stats.split(","));
                agiText.setText("AGI : " + statsArray.get(0));
                strText.setText("STR : " + statsArray.get(1));
                intText.setText("INT : " + statsArray.get(2));
                bagItem.setText(connect.load_item());
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
    private void mLeft(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        if (X-50<=50){}
        else {
            imgDisplay.setX(X - 50);
            X = X - 50;
        }
    }
    private void mRight(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        imgDisplay.setX(X+50);
        X = X+50;
    }
    private void changeR(){
        if (main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand).getConstantState()
                ||main.getBackground().getConstantState()== getResources().getDrawable(R.drawable.stand1).getConstantState())
            main.setBackgroundResource(R.drawable.walk);
        else
            main.setBackgroundResource(R.drawable.stand);
    }
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
            if (i < text.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable); // relate this to a Runnable
            } else {
                myTimer.cancel(); // stop the timer
                connect.change_text(1);
                return;
            }
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
    public void Office(){
        try {
            Intent k = new Intent(this, Office.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
        // load json coding
    /*public void loadJson(View view){
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.venue);
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()){
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {
        TextView txtDisplay = (TextView) findViewById(R.id.textView);
        txtDisplay.setText(s);
    }*/
}
