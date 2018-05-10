package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Office extends AppCompatActivity {

    public String[] text = {"This is the office, You may work over here and it cost your time outside",
            "Your salary will be depends on your intelligent","You may also loan money here",};
    public String[] text2 = {"Its preferable to go to your house first before going to the office",};
    final Timer myTimer = new Timer();
    Handler myHandler = new Handler();
    TextView moveText;
    Database connect;
    TextView Money;
    ImageButton work;
    private int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);
        //initialisation'
        connect = new Database(this,"",null,1);
        work = (ImageButton) findViewById(R.id.work);
        Money = (TextView) findViewById(R.id.money);
        moveText = (TextView) findViewById(R.id.MovingText);
        ImageButton Back = (ImageButton) findViewById(R.id.back);
        Money.setText("$ "+connect.load_money());
        Log.d("test", "update_text: "+connect.text_load());
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main();
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect.add_money(50);
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
    //runnable that will change text inside text array
    final Runnable myRunnable = new Runnable() {
        public void run() {
            moveText.setText(text[i -1]); // update text
        }
    };
    final Runnable myRunnable2 = new Runnable() {
        public void run() {
            moveText.setText(text2[i -1]); // update text
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
        if(connect.text_load()==1){
            if (i < text2.length) {
                i++;
                // text_data.setText(String.valueOf(i)); = avoid the RunTime error
                myHandler.post(myRunnable2); // relate this to a Runnable
            } else {
                myTimer.cancel(); // stop the timer
                return;
            }
        }
    }
}
