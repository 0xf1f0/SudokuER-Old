package com.example.sudokuer.sudokuer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Image extends AppCompatActivity
{
    //ImageView to display the image
    private ImageView mImageView;

    //logcat tag
    private final String TAG = getClass().getSimpleName();
    private final int SCALESIZE = 2;

    private Button mDoneBtn;


    //ToDo: Use the bitmap filePath instead of the inputStream


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this)
    {
        @Override
        public void onManagerConnected(int status)
        {
            Log.i(TAG, "Calling -> onManagerConnected()");
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    try
                    {
//                        DifferenceOfGaussian();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume()
    {
        Log.i(TAG, "Calling -> onResume()");
        super.onResume();
        if (!OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for " +
                    "initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else
        {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "Calling -> onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //Handle Incoming Content
        Uri imageUri = getIntent().getData();
        if (imageUri != null)
        {
            //Display the image received from MainActivity
            handleImageIntent(imageUri);
        }

        //Link the "Done" button to its appropriate resource id
        mDoneBtn = (Button) findViewById(R.id.btnDone);

    /*
        Set OnclickListener for the done button to return to MainActivity
        Do some clean up
    */

        mDoneBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }

    /**
     * Display a mOriginalBitmap image capture or loaded from gallery on screen
     */
    private void handleImageIntent(Uri imageUri)
    {
        Log.i(TAG, "Calling -> handleImageIntent()");
        InputStream inputStream;


        if (imageUri != null)
        {
            try
            {
                inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream != null)
                {
                    //Reduce the bitmap by SCALESIZE
                    displayScaledBitmap(inputStream, SCALESIZE);
                }
            } catch (FileNotFoundException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    /* Create a scaled bitmap from the input stream*/
    private void displayScaledBitmap(InputStream inputStream, int sampleSize)
    {

        Log.i(TAG, "Calling -> displayScaledBitmap()");

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = sampleSize;

        Bitmap scaledBitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
        mImageView = (ImageView) findViewById(R.id.display_image);
        mImageView.setImageBitmap(scaledBitmap);
    }

}
