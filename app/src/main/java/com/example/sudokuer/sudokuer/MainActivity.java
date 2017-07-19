package com.example.sudokuer.sudokuer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity
{
    //Test OpenCV Library import
    private static final String TAG = "MainActivity";

    static
    {
        if (!OpenCVLoader.initDebug())
        {
            Log.d(TAG, "OpenCV not loaded");
        } else
        {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
