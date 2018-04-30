package com.example.richardoalvin.trappedstickworld;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    Handler loops = new Handler();
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    public float X;
    TextView moveText;
    int i = 0;
    Dialog dialog;
    public String[] text = {"\"You: Ahh Where is this? \"",
            "Hey Welcome to the StickWorld and this is gonna be your new house now",
            "You: Me? I cant even remember anything about myself...","" +
            "Well you should be able to adapt with the condition here and you might be able to remember your past"};
    public ImageView main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (ImageView) findViewById(R.id.StickMan);
        main.setY(300);
    //Player movement coding
        Button Left = (Button) findViewById(R.id.left);
        Button Right = (Button) findViewById(R.id.right);
        moveText = (TextView) findViewById(R.id.MovingText);
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
        myTimer.schedule(myTask,0,2500);
    }
    private void mLeft(){
        ImageView imgDisplay = (ImageView) findViewById(R.id.StickMan);
        imgDisplay.setX(X-50);
        X = X-50;
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

        if(i < text.length) {
            i++;
            // text_data.setText(String.valueOf(i)); = avoid the RunTime error
            myHandler.post(myRunnable); // relate this to a Runnable
        } else {
            myTimer.cancel(); // stop the timer
            return;
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
