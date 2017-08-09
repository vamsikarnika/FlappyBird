package com.example.android.nextgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Vamsi Karnika on 6/30/2017.
 */

public class Bird {
    private Point a;
    private Point b;
    private Point c;
    private Point d;
    private Point centre;

    private int length = 5;
    private int PixelsperMetreX ;
    private int PixelsPerMetreY;
    int sideX;
    int sideY;
    private int y;
    Bitmap bitmap ;
    int Bird_width ;
    int Bird_height;
    private float speed = 0;
    private int gravity = 40;

    Bird(Context context,String bitmapname,int screenX,int screenY){
        PixelsperMetreX = screenX/90;
        PixelsPerMetreY = screenY/55;
        sideX = length*PixelsperMetreX;
        sideY = length*PixelsPerMetreY;
        gravity = PixelsPerMetreY*gravity;
        y = screenY;
        a = new Point();

        b = new Point();

        c = new Point();

        d = new Point();


        centre = new Point();
        Log.d("Android:","In the bird");
        centre.x = screenX/2;
        centre.y = screenY/4;

        a.x = centre.x - (sideX)/2;
        a.y = centre.y + (sideY)/2;

        b.x = centre.x + (sideX)/2;
        b.y = centre.y + (sideY )/2;



        c.x = centre.x + (sideX)/2;
        c.y = centre.y - (sideY)/2;

        d.x = centre.x - (sideX)/2;
        d.y = centre.y - (sideY)/2;

        int resId = context.getResources().getIdentifier(bitmapname,"drawable",context.getPackageName());

        bitmap = BitmapFactory.decodeResource(context.getResources(),resId);

        bitmap = Bitmap.createScaledBitmap(bitmap,sideX,sideY,true);

        Bird_width = bitmap.getWidth();

        Bird_height = bitmap.getHeight();
    }


    public Point getA(){
        return a;
    }
    public Point getB(){
        return b;
    }
    public Point getC(){
        return c;
    }
    public Point getD(){
        return d;
    }




    public void update(long fps){
        speed = speed + gravity/fps;
        centre.y += speed / fps;
        if(centre.y - (sideY)/2 <= 0 ){
            centre.y = sideY*3/4;
            speed = 0;
        }
        a.x = centre.x - (sideX)/2;
        a.y = centre.y + (sideY)/2;

        b.x = centre.x + (sideX)/2;
        b.y = centre.y + (sideY )/2;



        c.x = centre.x + (sideX)/2;
        c.y = centre.y - (sideY)/2;

        d.x = centre.x - (sideX)/2;
        d.y = centre.y - (sideY)/2;
    }
    public void onTouch(){
        speed = -20*PixelsPerMetreY;
    }

}
