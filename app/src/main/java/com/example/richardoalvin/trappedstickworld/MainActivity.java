package com.example.richardoalvin.trappedstickworld;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadJson(View view){
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
    }
}
