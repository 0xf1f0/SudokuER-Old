package com.example.sudokuer.sudokuer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    //Test OpenCV Library import
    private final String TAG = getClass().getSimpleName();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_IMAGE_FROM_GALLERY = 2;
    private String mCurrentPhotoPath;

    //Buttons on home screen
    /* Select an image(sudoku) from photo gallery */
    private Button galleryBtn;
    /* Select capture an image(sudoku) using the device camera */
    private Button captureBtn;
    /* Display useful information about the application*/
    private Button aboutBtn;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galleryBtn = (Button) findViewById(R.id.select_image);
        captureBtn = (Button) findViewById(R.id.add_image);
        aboutBtn = (Button) findViewById(R.id.app_info);

        captureBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });


        galleryBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImageIntent();
            }
        });

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /*Return early*/
        if (data == null)
        {
            Log.i(TAG, "Error launching gallery intent!");
            return;
        }

        if ((requestCode == SELECT_IMAGE_FROM_GALLERY) && (resultCode == Activity.RESULT_OK))
        {
            imageUri = data.getData();
        }
    }

    /* Activate device camera and take a picture; possibly a sudoku game */

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /*Select a image from photo gallery*/
    private void selectImageIntent()
    {
        Intent selectImageFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        selectImageFromGallery.setType("image/*");
        startActivityForResult(selectImageFromGallery, SELECT_IMAGE_FROM_GALLERY);
    }
}
