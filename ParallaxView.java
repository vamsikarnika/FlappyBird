package com.example.android.nextgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.example.android.nextgame.ParallaxActivity.filename;
import static com.example.android.nextgame.ParallaxActivity.outputStream;

public class ParallaxView extends SurfaceView implements Runnable {

    ArrayList<Background> backgrounds;

    ArrayList<Pipe> pipes;

    HUD hud;

    private volatile boolean running;
    private Thread gameThread = null;

    // For drawing
    private Paint paint;
    private boolean gameover = false;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    private boolean paused = true;

    // Holds a reference to the Activity
    Context context;

    // Control the fps
    long fps =60;

    // Screen resolution
    int screenWidth;
    int screenHeight;

    long span = 0;
    int count = 0;

    int pos = 0;
    int index = 0;
    Bird bird ;

    ParallaxView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();


        bird = new Bird(context,"bird_sing",screenWidth,screenHeight);
        Log.d("Android:",String.valueOf(bird.sideX)+" "+String.valueOf(bird.sideY));

        pipes = new ArrayList<>();

        backgrounds = new ArrayList<>();

        hud = new HUD(screenWidth,screenHeight);

        //load the background data into the Background objects and
        // place them in our GameObject arraylist

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "skyline",  0, 85, 50));

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "grass",  85, 110, 250));

    }

    @Override
    public void run() {

        while (running) {
            //if(bird.centre.y == screenHeight/2) velocity = 0.0f;
            if(paused){
                if (ourHolder.getSurface().isValid()) {
                    //First we lock the area of memory we will be drawing to
                    canvas = ourHolder.lockCanvas();

                    //draw a background color
                    canvas.drawColor(Color.argb(255, 0, 3, 70));


                    drawBackground(0);

                    drawBird();

                    for(Pipe pipe : pipes) {
                        if(!pipe.clipped)
                            drawPipe(pipe);
                    }



                    drawBackground(1);
                    // Draw the foreground parallax
                    paint.setColor(Color.argb(255,255,255,255));
                    paint.setTextSize(70);
                    canvas.drawText("Tap to Start", 500, (screenHeight / 100)*50, paint);

                    // Unlock and draw the scene
                    ourHolder.unlockCanvasAndPost(canvas);
                }
            }
            else {
                long startFrameTime = System.currentTimeMillis();
                update();
                updatebird();
                updatepipe();
                draw();

                Collision();
                long timeThisFrame = System.currentTimeMillis() - startFrameTime;

                span += timeThisFrame;

                if (span >= 2200) {

                    pipes.add(new Pipe(context, "pipe", screenWidth, screenHeight, 150));
                    //Log.d("Android:",String.valueOf(pipes.size()));
                    index++;
                    span = 0;
                }

                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }
        }
    }

    private void update() {
        // Update all the background positions
        for (Background bg : backgrounds) {
            bg.update(fps);
        }
    }
    private void updatebird(){
        bird.update(fps);
    }
    private void updatepipe(){
        for(Pipe pipe : pipes) {
            pipe.update(fps);
       }
    }
    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //draw a background color
            canvas.drawColor(Color.argb(255, 0, 3, 70));


            drawBackground(0);


            drawBird();

            for(Pipe pipe : pipes) {
                if(!pipe.clipped)
                drawPipe(pipe);
            }

            paint.setColor(Color.argb(255,255,255,255));
            paint.setTextSize(70);
            canvas.drawText("SCORE   "+String.valueOf(pos), 500, (screenHeight / 100)*30, paint);

            drawBackground(1);
            // Draw the foreground parallax

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void pause() {
        Log.d("Android:","pause");
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void resume() {
        Log.d("Android:","resume");
        running = true;
        gameThread = new Thread(this);
        gameThread.start();

    }
    private void drawBird(){
        Rect fromRect = new Rect(0,0,bird.sideX,bird.sideY);
        Rect toRect = new Rect(bird.getD().x,bird.getD().y,bird.getB().x,bird.getB().y);
        canvas.drawBitmap(bird.bitmap,fromRect,toRect,paint);
    }
    private void drawBackground(int position) {

        // Make a copy of the relevant background
        Background bg = backgrounds.get(position);

        // define what portion of images to capture and
        // what coordinates of screen to draw them at

        // For the regular bitmap
        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        // For the reversed background
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        //draw the two background bitmaps
        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }

    }
    private void drawPipe(Pipe pipe){
        Rect FromRect1 = new Rect(0,0,pipe.width,pipe.heightTop);
        Rect toRect1 = new Rect(pipe.xClip,0,pipe.xClip+pipe.width,(int)pipe.length);

        Rect fromRect2 = new Rect(0,0,pipe.width,pipe.heightBot);
        Rect toRect2 = new Rect(pipe.xClip, (int)pipe.length+pipe.gap,pipe.xClip+pipe.width,screenHeight);

        canvas.drawBitmap(pipe.bitmap,FromRect1,toRect1,paint);
        canvas.drawBitmap(pipe.reveresed,fromRect2,toRect2,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if(gameover){
            action(hud.handleInput(motionEvent));
            return true;
        }
            paused = false;
            switch (motionEvent.getAction() & motionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    bird.onTouch();
                    break;
            }


        return true;
    }

    public void Collision(){

        if(pos < index) {
            if (bird.getC().x >= pipes.get(pos).xClip+5 && bird.getA().x +5 <= pipes.get(pos).xClip + pipes.get(pos).width) {
                if (bird.getD().y <= pipes.get(pos).length - 5 || bird.getB().y >= pipes.get(pos).length + pipes.get(pos).gap+5) {
                    pipes.clear();
                    drawBoard();
                    gameover = true;
                    pause();
                }
            }
            if(bird.getA().x > pipes.get(pos).xClip+pipes.get(pos).width) {
                pos++;
            }
        }
        if(bird.getA().y >= 85*(screenHeight/100)){
            pipes.clear();
            drawBoard();
            gameover = true;
            pause();
        }

    }

    public void drawBoard(){

        Log.d("Android:","Board");
        paint.setColor(Color.argb(80, 255, 255, 255));
        canvas = ourHolder.lockCanvas();
        for (Rect rect : hud.currentButtonList) {
            RectF rf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
            canvas.drawRoundRect(rf, 10f, 10f, paint);
        }
        ourHolder.unlockCanvasAndPost(canvas);
    }
    public void action(int reply){
        if(reply == 1){
            Log.d("Android:","Right");
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            System.exit(0);
        }
        else if(reply == 2){
            Log.d("Android:","left");
            gameover = false;
            paused = true;
            pos = 0;
            index = 0;
            bird = new Bird(context,"bird_sing",screenWidth,screenHeight);
            pipes = new ArrayList<>();
            resume();

        }
    }

}
