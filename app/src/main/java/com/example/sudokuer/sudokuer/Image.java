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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Image extends AppCompatActivity
{
    //ImageView to display the image
    private ImageView mImageView;

    //logcat tag
    private final String TAG = getClass().getSimpleName();

    private Button mDoneBtn;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
                bitmap.recycle();
                finish();
            }
        });


    }

    /**
     * Display a bitmap image capture or loaded from gallery on screen
     */
    private void handleImageIntent(Uri imageUri)
    {
        mImageView = (ImageView) findViewById(R.id.display_image);
        InputStream inputStream;

        if (imageUri != null)
        {
            try
            {
                inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream != null)
                {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null)
                    {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            } catch (FileNotFoundException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
