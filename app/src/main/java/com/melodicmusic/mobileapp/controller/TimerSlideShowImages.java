package com.melodicmusic.mobileapp.controller;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.melodicmusic.mobileapp.view.NoLoginStartPageFragment;
import com.melodicmusic.mobileapp.view.PrincipalPage;

import java.util.TimerTask;

/**
 * Created by Nelson on 30/8/2017.
 */

public class TimerSlideShowImages extends TimerTask {
    private Activity activity;
    private Fragment fragment;
    private boolean isLogin;

    public TimerSlideShowImages(NoLoginStartPageFragment pFragment) {
        this.activity = pFragment.getActivity();
        fragment = pFragment;
        isLogin = true;
    }

    public TimerSlideShowImages(PrincipalPage pFragment) {
        this.activity = pFragment.getActivity();
        fragment = pFragment;
        isLogin = false;
    }

    @Override
    public void run() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isLogin){
                    NoLoginStartPageFragment fragmentLogin = (NoLoginStartPageFragment) fragment;
                    int currentItem = fragmentLogin.getViewPager().getCurrentItem();
                    if(currentItem % fragmentLogin.getImages().length + 1 == fragmentLogin.getImages().length){
                        fragmentLogin.getViewPager().setCurrentItem(0);
                    }else{
                        fragmentLogin.getViewPager().setCurrentItem(currentItem % fragmentLogin.getImages().length + 1);
                    }
                } else {
                    PrincipalPage fragmentLogin = (PrincipalPage) fragment;
                    int currentItem = fragmentLogin.getViewPager().getCurrentItem();
                    if(currentItem % fragmentLogin.getImages().length + 1 == fragmentLogin.getImages().length){
                        fragmentLogin.getViewPager().setCurrentItem(0);
                    }else{
                        fragmentLogin.getViewPager().setCurrentItem(currentItem % fragmentLogin.getImages().length + 1);
                    }
                }
            }
        });
    }
}