package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class House extends AppCompatActivity {

    public String[] text = {"This is your new house and here you can find your money",
            "You only have limited money which is $10",
            "First Quest You need to go to the office and work over there",""};
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    TextView moveText;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        //initialisation
        moveText = (TextView) findViewById(R.id.MovingText);
        ImageButton Back = (ImageButton) findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main();
            }
        });
        TimerTask myTask = new TimerTask() {
            public void run() {
                update_text(); // text update method
            }
        };
        //timer for moving text
        myTimer.schedule(myTask,0,2500);
    }
    // runnable that will change text inside text array
    final Runnable myRunnable = new Runnable() {
        public void run() {
            moveText.setText(text[i -1]); // update text
        }
    };

    // update_text method related to a Runnable
    private void update_text() {

        if(i< text.length) {
            i++;
            // text_data.setText(String.valueOf(i)); = avoid the RunTime error
            myHandler.post(myRunnable); // relate this to a Runnable
        } else {
            myTimer.cancel(); // stop the timer
            return;
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
}
