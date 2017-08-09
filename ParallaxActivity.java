package com.example.android.nextgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ParallaxActivity extends AppCompatActivity {

    private ParallaxView parallaxView;
    public static String filename = "H_Scores";
    public static FileOutputStream outputStream ;
    public static BufferedInputStream br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        String string = "HIGH SCORE  0";

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        // And finally set the view for our game
        parallaxView = new ParallaxView(this, resolution.x, resolution.y);

        // Make our parallaxView the view for the Activity
        setContentView(parallaxView);

    }

    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        parallaxView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        parallaxView.resume();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finishAffinity();
    }


}
