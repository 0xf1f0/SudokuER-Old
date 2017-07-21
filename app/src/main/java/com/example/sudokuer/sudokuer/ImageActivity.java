package com.example.sudokuer.sudokuer;

/**
 * Created by Lenovo on 7/20/2017.
 */

public class ImageActivity
{
    //The resource ID of potential sudoku image
    private int mImageResourceId;

    public ImageActivity (int imageResourceId)
    {
        mImageResourceId = imageResourceId;
    }

    /***
     *
     * @return the imageResource ID of a potential Sudoku image
     */
    public int getImageResourceId()
    {
        return mImageResourceId;
    }
}
