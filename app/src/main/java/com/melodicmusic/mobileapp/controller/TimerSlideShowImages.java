package com.melodicmusic.mobileapp.controller;

import android.app.Activity;

import com.melodicmusic.mobileapp.MainActivity;

import java.util.TimerTask;

/**
 * Created by Nelson on 30/8/2017.
 */

public class TimerSlideShowImages extends TimerTask {
    private Activity activity;

    public TimerSlideShowImages(Activity pActivity) {
        this.activity = pActivity;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity mainActivity = (MainActivity )activity;
                int currentItem = mainActivity.getViewPager().getCurrentItem();
                if(currentItem % mainActivity.getImages().length + 1 == mainActivity.getImages().length){
                    mainActivity.getViewPager().setCurrentItem(0);
                }else{
                    mainActivity.getViewPager().setCurrentItem(currentItem % mainActivity.getImages().length + 1);
                }
            }
        });
    }
}
