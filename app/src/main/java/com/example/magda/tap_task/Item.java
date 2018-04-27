package com.example.magda.tap_task;

/**
 * Created by magda on 2018-04-23.
 */

public class Item {
    private String mText;
    private String mImage;

    public Item(String text, String img){
        mText = text;
        mImage = img;
    }

    public String getmText() {
        return mText;
    }

    public String getmImage() {
        return mImage;
    }
}