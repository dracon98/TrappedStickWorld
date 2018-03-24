package com.example.richardoalvin.trappedstickworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

import java.util.Random;

/**
 * Created by Richardo Alvin on 3/24/2018.
 */

public class CustomActivity extends AppCompatActivity {

    private ImageButton back;
    private Button Start;
    private EditText Agi;
    private EditText Intel;
    private EditText Str;
    private ImageButton roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        back = (ImageButton) findViewById(R.id.bback);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MoveToMenu();
            }
    });
        Agi = (EditText) findViewById(R.id.agi);
        Str = (EditText) findViewById(R.id.str);
        Intel = (EditText) findViewById(R.id.intel);
        Agi.setEnabled(false);
        Str.setEnabled(false);
        Intel.setEnabled(false);

        roll = (ImageButton) findViewById(R.id.rollId);
        roll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                RandomPoint();
            }
        });
        Start = (Button) findViewById(R.id.startID);
        Start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MoveToMain();
            }
        });
    }
        public void MoveToMenu(){
            try {
                Intent k = new Intent(this, MenuActivity.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        public void MoveToMain(){
            try {
                Intent k = new Intent(this, MainActivity.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        public  void RandomPoint(){
            Random r = new Random();
            int i = r.nextInt(10) + 1;
            int o = r.nextInt(10) + 1;
            int in = r.nextInt(10) + 1;
            int j = 21 - o - i;
            int n = 21 - in - i;
            int m = 21 - in - o;
            Agi.setText(Integer.toString(j));
            Str.setText(Integer.toString(n));
            Intel.setText(Integer.toString(m));
        }
}
