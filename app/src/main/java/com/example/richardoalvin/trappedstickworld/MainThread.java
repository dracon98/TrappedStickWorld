package com.example.richardoalvin.trappedstickworld;


import android.graphics.Canvas;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.SurfaceHolder;

/**
 * Created by Richardo Alvin on 4/19/2018.
 */

public class MainThread extends Thread {
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                this.gamePanel.update();
                this.gamePanel.draw(canvas);
                }
            }
            catch(Exception e){}
            finally {
                if(canvas!= null)
                {
                    try{
                            surfaceHolder.unlockCanvasAndPost(canvas);}
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if(frameCount== FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
    public void setRunning (boolean b){
        running = b;
    }
}
