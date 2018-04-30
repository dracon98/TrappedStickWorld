package com.example.richardoalvin.trappedstickworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import static android.R.color.white;

/**
 * Created by Richardo Alvin on 4/19/2018.


public class Player extends GameObject {
    private Bitmap sprite;
    private double dya;
    private boolean up;
    private long startTime;
    private Animation animation = new Animation();
    private boolean playing;

    public Player(Bitmap res, int w, int h, int numFrames) {
        x = 100;
        y = 100;
        dy = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        sprite = res;
        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(sprite, i * width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(150);
        startTime = System.nanoTime();

    }

    public void update() {
        animation.update();
    }

    //public void draw(Canvas canvas)
    {
        //   canvas.drawBitmap(animation.getImage(),x,y,null);
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
}
*/