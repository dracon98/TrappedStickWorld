package com.example.richardoalvin.trappedstickworld;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Waves extends AppCompatActivity {

    //declaration
    MediaPlayer hitSound;
    MediaPlayer dmgSound;
    Database connect;
    Button Knife;
    Button Axe;
    Button Knuckle;
    Button Gun;
    TextView EnemyHealthtext;
    TextView Healthtext;
    TextView wavetext;
    ProgressBar Energy;
    String[] enemyWeapons;
    int energyUnit;
    int energyUse = 20;
    int enemyHealth;
    int curEnemyhealth = 0;
    int dmg;
    int enemyEnergy = 40;
    int enemyDmg;
    int Agi;
    int Str;
    int Int;
    int max = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waves);
        //database connection
        connect = new Database(this, "", null, 1);
        //load enemy
        loadJson();
        //initialisation
        Knuckle = (Button) findViewById(R.id.knuckle);
        Axe = (Button) findViewById(R.id.axe);
        Knife = (Button) findViewById(R.id.knife);
        Gun = (Button) findViewById(R.id.gun);
        EnemyHealthtext = (TextView) findViewById(R.id.enemyhealth);
        Healthtext = (TextView) findViewById(R.id.health);
        Energy = (ProgressBar) findViewById(R.id.energy);
        wavetext = (TextView) findViewById(R.id.wave);
        //defining declared objects
        wavetext.setText("Wave " + connect.load_wave() + "");
        curEnemyhealth = enemyHealth;
        EnemyHealthtext.setText(curEnemyhealth + "/" + enemyHealth);
        Healthtext.setText(connect.load_curhealth() + "/" + connect.load_health());
        energyUnit = Energy.getProgress();
        //button invisible if doesnt have the item
        if (checkItem("knife") == 0 && Knife.getVisibility() == View.VISIBLE) {
            Knife.setVisibility(View.INVISIBLE);
        }
        if (checkItem("gun") == 0 && Gun.getVisibility() == View.VISIBLE) {
            Gun.setVisibility(View.INVISIBLE);
        }
        if (checkItem("knuckle") == 0 && Knuckle.getVisibility() == View.VISIBLE) {
            Knuckle.setVisibility(View.INVISIBLE);
        }
        if (checkItem("axe") == 0 && Axe.getVisibility() == View.VISIBLE) {
            Axe.setVisibility(View.INVISIBLE);
        }
        String stats = connect.load_stats();
        List<String> statsArray = Arrays.asList(stats.split(","));
        Agi = Integer.valueOf(statsArray.get(0));
        Str = Integer.valueOf(statsArray.get(1));
        Int = Integer.valueOf(statsArray.get(2));
        dmg = (Agi + Str + Int) / 6;
        //onclicks
        Knife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(1, 0);
                enemyAttack();
                quit();
            }
        });
        Axe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(3, 2);
                enemyAttack();
                quit();
            }
        });
        Knuckle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(2, 0);
                enemyAttack();
                quit();

            }
        });
        Gun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attack(4, 4);
                enemyAttack();
                quit();
            }
        });
    }
    //item check if find item in the back using for loops
    public int checkItem(String type) {
        String items = connect.load_item();
        List<String> itemsArray = Arrays.asList(items.split(","));
        if (itemsArray.size() != 0) {
            for (int i = 0; i < itemsArray.size(); i++) {
                if (itemsArray.get(i).equals(type)) {
                    return 1;
                }
            }
        }
        return 0;
    }
    //attack mechanism
    public void attack(int type, int weapon) {
        int energyConsumption = type * energyUse;
        if (energyUnit >= energyConsumption) {
            curEnemyhealth = curEnemyhealth - ((dmg * type) + (dmg * weapon));
            energyUnit -= energyConsumption;
        }
        energyUnit += 40;
        EnemyHealthtext.setText(curEnemyhealth + "/" + enemyHealth);
        Energy.setProgress(energyUnit);
        hitEnemy();
    }
    //enemy attack mechanism
    public void enemyAttack() {
        int count = enemyWeapons.length;
        int weaponDmg = 0;
        int enemyMoves;
        if (count == 1) {
            enemyMoves = 1;
        } else {
            Random r = new Random();
            enemyMoves = r.nextInt(count - 1 + 1) + 1;
        }
        for (int i = 1; i < count + 1; i++) {
            if (enemyMoves == i) {
                if (enemyEnergy >= enemyMoves * energyUse) {
                    connect.change_curhealth(-((i * enemyDmg) + (weaponDmg * enemyDmg)));
                }
            }
            if(count>2) {
                weaponDmg += 2;
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 0.5s = 500ms
                hitPlayer();
                Healthtext.setText(connect.load_curhealth() + "/" + connect.load_health());
            }
        }, 500);
        enemyEnergy += 40;
    }
    //quit if both enemy and player dead the player failed to beat the enemy
    // if the enemy wave 6 was beaten then go to win
    private void quit() {
        if (connect.load_curhealth()<=0&&curEnemyhealth<=0){
            connect.add_stats(-Str / 10, -Agi / 10, -Int / 10);
            connect.change_time(12);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 0.5s = 500ms
                    sleep();
                }
            }, 500);
        }
        else{
            if (connect.load_curhealth() <= 0) {
                connect.add_stats(-Str / 10, -Agi / 10, -Int / 10);
                connect.change_time(12);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        sleep();
                    }
                }, 500);
            }
            if (curEnemyhealth <= 0) {
                connect.add_stats(Str / 4, Agi / 4, Int / 4);
                connect.change_time(3);
                if (connect.load_wave()!=12){
                    connect.change_wave();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 0.5s = 500ms
                        if (connect.load_wave()==6&& connect.load_quest()==5){
                            connect.quest_change(6);
                            connect.add_stats(Str/3,Agi/3,Int/3);
                            Win();
                        }
                        else
                            sleep();
                    }
                }, 500);
            }
        }
    }
    //check sleep status
    public void sleep() {
        String time = connect.load_time();
        List<String> timeArray = Arrays.asList(time.split(","));
        if (timeArray.get(2).equals("rest")) {
            House();
        }
        else
            NextMap();
    }
    //go to house
    public void House() {
        try {
            Intent k = new Intent(this, House.class);
            startActivity(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //go to win
    public void Win() {
        try {
            Intent k = new Intent(this, Win.class);
            startActivity(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //go to next map
    public void NextMap() {
        try {
            Intent k = new Intent(this, NextMap.class);
            startActivity(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //load json
    public void loadJson() {
        Resources res = getResources();
        InputStream is = res.openRawResource(R.raw.npc);
        if (connect.load_wave()>=6){
            is = res.openRawResource(R.raw.npc_hard);
        }
        Scanner scanner = new Scanner(is);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        parseJson(builder.toString());
    }

    private void parseJson(String s) {;
        try{
            //change player according to the current wave
            JSONObject root = new JSONObject(s);
            JSONArray npcs = root.getJSONArray("npc");
            for (int i=0; i<npcs.length();i++){
                JSONObject npc = npcs.getJSONObject(i);
                if (npc.getInt("id")==connect.load_wave()){
                    enemyHealth = npc.getInt("health");
                    enemyDmg = npc.getInt("damage");
                    JSONArray items = npc.getJSONArray("item");
                    enemyWeapons = new String[items.length()];
                    for (int j =0; j<items.length();j++){
                        enemyWeapons[j] = items.getString(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void hitEnemy(){
        hitSound = MediaPlayer.create(this,R.raw.highhit);
        hitSound.start();
    }
    private void hitPlayer(){
        hitSound = MediaPlayer.create(this,R.raw.grassythud);
        hitSound.start();
    }
}