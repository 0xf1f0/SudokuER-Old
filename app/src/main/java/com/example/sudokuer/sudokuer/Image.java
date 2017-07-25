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
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Image extends AppCompatActivity
{
    //ImageView to display the image
    private ImageView mImageView;

    //logcat tag
    private final String TAG = getClass().getSimpleName();

    private Button mDoneBtn;
    private Bitmap mBitmap;



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
                        DifferenceOfGaussian();
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
     * Display a mBitmap image capture or loaded from gallery on screen
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
                    mBitmap = BitmapFactory.decodeStream(inputStream);
                    if (mBitmap != null)
                    {
                        loadImageToImageView();
                    }
                }
            } catch (FileNotFoundException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void DifferenceOfGaussian()
    {
        Log.i(TAG, "Calling -> DifferenceOfGaussian()");
        Mat grayMat = new Mat();
        Mat blur1 = new Mat();
        Mat blur2 = new Mat();
        Mat imageMat = new Mat();
        //Covert mBitmap to Mat


        //Converting Bitmap back to Mat
        Utils.bitmapToMat(mBitmap, imageMat);

        //Converting the image to grayscale
        Imgproc.cvtColor(imageMat, grayMat, Imgproc.COLOR_BGR2GRAY);

        //Blurring the images using two different blurring radius
        Imgproc.GaussianBlur(grayMat, blur1, new Size(15, 15), 5);
        Imgproc.GaussianBlur(grayMat, blur2, new Size(21, 21), 5);

        //Subtracting the two blurred images
        Mat DoG = new Mat();
        Core.absdiff(blur1, blur2, DoG);

        //Inverse Binary Thresholding
        Core.multiply(DoG, new Scalar(100), DoG);
        Imgproc.threshold(DoG, DoG, 50, 255, Imgproc.THRESH_BINARY_INV);

        //Convert mat to bitmap
        Utils.matToBitmap(DoG, mBitmap);
    }

    private void loadImageToImageView()
    {
        Log.i(TAG, "Calling -> loadImageToImageView()");

        mImageView = (ImageView) findViewById(R.id.display_image);
        mImageView.setImageBitmap(mBitmap);
    }
}
