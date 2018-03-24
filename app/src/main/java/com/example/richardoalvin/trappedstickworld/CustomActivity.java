package com.example.richardoalvin.trappedstickworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;
/**
 * Created by Richardo Alvin on 3/24/2018.
 */

public class CustomActivity extends AppCompatActivity {

    private ImageButton back;
    private EditText Agi;
    private EditText Intel;
    private EditText Str;
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
    }
        public void MoveToMenu(){
            try {
                Intent k = new Intent(this, MenuActivity.class);
                startActivity(k);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
}
