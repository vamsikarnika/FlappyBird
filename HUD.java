package com.example.android.nextgame;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Vamsi Karnika on 7/2/2017.
 */

public class HUD {

    Rect left;
    Rect right;

    public ArrayList<Rect> currentButtonList = new ArrayList<>();

    HUD(int screenWidth,int screenHeight){
        Log.d("Android:","HUD");
        int buttonWidth = screenWidth / 8;
        int buttonHeight = screenHeight / 7;
        int buttonPadding = screenWidth / 80;

        left = new Rect(screenWidth/2-200,
                screenHeight/2,
                screenWidth/2-50,
                screenHeight/2+buttonHeight);

        right = new Rect(screenWidth/2+50,
                screenHeight/2,
                screenWidth/2+200,
                screenHeight/2 + buttonHeight);

        currentButtonList.add(left);
        currentButtonList.add(right);
    }

    public int handleInput(MotionEvent motionEvent){
        int x = (int)motionEvent.getX(0);
        int y = (int)motionEvent.getY(0);
        int action = 0;
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

            case MotionEvent.ACTION_DOWN:
                if(right.contains(x,y)){
                    action = 2;
                }
                else if(left.contains(x,y)){
                    action = 1;
                }
                break;
        }
        return  action;
    }
}
