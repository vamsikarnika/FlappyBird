package com.example.android.nextgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.StrictMath.abs;

/**
 * Created by Vamsi Karnika on 7/1/2017.
 */

public class Pipe {
    boolean clipped = false;
    private int block_size = 5;
    private int blocks;
    int xClip;
    int heightTop;
    float length ;
    int heightBot;
    Bitmap bitmap;
    Bitmap reveresed;
    private int screenWidth;
    private int screenHeight;
    int width = 10;
    int gap = 14;
    float speedY = 1;
    private float shiftY = 0;
    private int PixelsperMetreX;
    private int PixelsperMetreY;
    float s;
    Pipe(Context context,String bitmapname,int screenX,int screenY,float speed){
        PixelsperMetreX = screenX / 90;
        PixelsperMetreY = screenY / 55;
        screenWidth = screenX;
        screenHeight = screenY;
        s = speed;
        int resid = context.getResources().getIdentifier(bitmapname,"drawable",context.getPackageName());

        bitmap = BitmapFactory.decodeResource(context.getResources(),resid);

        xClip = screenX;

        blocks = 5;
        Random rand = new Random();
        length = random();
        length = length*PixelsperMetreY;
        heightTop = blocks*block_size*PixelsperMetreY;
        width = width*PixelsperMetreX;

        bitmap = Bitmap.createScaledBitmap(bitmap,width,heightTop,true);

        Matrix matrix = new Matrix();

        matrix.setScale(1,-1);

        blocks = 5;
        gap = gap*PixelsperMetreY;
        heightBot = blocks*block_size*PixelsperMetreY;

        reveresed = Bitmap.createBitmap(bitmap,0,0,width,heightBot,matrix,true);


    }
    public void update(long fps){
        xClip -= s/fps;
        if(xClip + width <= 0 ){
            clipped = true;
        }
        /*shiftY -= speedY/fps;
        length += shiftY;
        Log.d("Android",String.valueOf(speedY));
        if(abs(shiftY) >= 3 || (length <=0 || length >= screenHeight)){
            //Log.d("Android",String.valueOf(shiftY));
            speedY = -1*speedY;
            shiftY = 0;
        }*/
    }
    private int random(){
        Random rand = new Random();
        int result = (rand.nextInt(5)+1)*5;
        return result;
    }
}
