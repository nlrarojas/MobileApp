package com.melodicmusic.mobileapp;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.melodicmusic.mobileapp.controller.TimerSlideShowImages;
import com.melodicmusic.mobileapp.controller.ViewPagerAdapter;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Integer [] images = {R.drawable.gelectrica, R.drawable.piano, R.drawable.microfono, R.drawable.saxophone};
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(viewPagerAdapter);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerSlideShowImages(this), 2000, 4000);

    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public Integer[] getImages() {
        return images;
    }

    public void setImages(Integer[] images) {
        this.images = images;
    }
}
