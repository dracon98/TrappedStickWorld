package com.example.richardoalvin.trappedstickworld;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
public class MenuActivity extends AppCompatActivity {

    private Button NewGame;
    Database connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //onclick button
        NewGame = (Button)findViewById(R.id.newG);
        NewGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //connect.setup();
                MoveToCustom();
            }
        });
        connect = new Database(this,"",null,1);
    }
    //intent
    //move to custom activity
    public void MoveToCustom(){
        try {
            Intent k = new Intent(this, CustomActivity.class);
            startActivity(k);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
