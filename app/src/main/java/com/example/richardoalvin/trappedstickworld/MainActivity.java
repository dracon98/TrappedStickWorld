package com.example.richardoalvin.trappedstickworld;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    Handler loops = new Handler();
    public float X;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Left = (Button) findViewById(R.id.left);
        Button Right = (Button) findViewById(R.id.right);

        Left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loops.postDelayed(Lmove, 250);
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
                    loops.postDelayed(this,250);
                }
            };
        });

        Right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loops.postDelayed(Rmove, 250);
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
                    loops.postDelayed(this,250);
                }
            };
        });
    }
    private void mLeft(){
        TextView txtDisplay = (TextView) findViewById(R.id.textView);
        txtDisplay.setX(X-30);
        X = X-30;
    }
    private void mRight(){
        TextView txtDisplay = (TextView) findViewById(R.id.textView);
        txtDisplay.setX(X+30);
        X = X+30;
    }
    public void moveRight(View view){
        mRight();
    }
    public void moveLeft (View view){
        mLeft();
    }
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
