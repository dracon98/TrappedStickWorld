package com.example.richardoalvin.trappedstickworld;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * Created by Richardo Alvin on 4/19/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Player player;
    private Background bg;
    public static final int WIDTH =800 ;
    public static final int HEIGHT =533;
    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.walk_pic),30,80,3);
        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.bg));
        bg.setVector(-5);
        thread.start();
        thread.setRunning(true);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try{
                thread.setRunning(false);
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            retry= false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
    public void update(){
        //player.update();

        //bg.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleX = 1500/WIDTH;
        final float scaleY = 900/HEIGHT;
        if (canvas != null){
            final int savedStates = canvas.save();
             canvas.scale(scaleX,scaleY);
            bg.draw(canvas);
            canvas.restoreToCount(savedStates);
            System.out.println(bg.x+ " , " + bg.y);
        }

        //bg.draw(canvas);

        //player.draw(canvas);
    }
}