package com.bruce.VideoPlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;
import com.bruce.VideoPlayer.fragment.LocalVideoFragment;
import com.bruce.VideoPlayer.fragment.OnlineVideoFragment;
import com.bruce.VideoPlayer.utils.SharedPfUtil;

public class MainActivity extends FragmentActivity {

    private ViewPager pager;
    private RadioGroup radioGroup;
    private int currentPage = R.id.radio_locale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        pager.setAdapter(new VideoFragmentAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radio_locale:
                        currentPage = 0;
                        pager.setCurrentItem(0);
                        break;
                    case R.id.radio_online:
                        currentPage = 1;
                        pager.setCurrentItem(1);
                        break;
                }
            }
        });
        radioGroup.check(currentPage);


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        radioGroup.check(R.id.radio_locale);
                        break;
                    case 1:
                        radioGroup.check(R.id.radio_online);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    /**
     * FragmentPagerAdapter viewpager适配
     */
    private class VideoFragmentAdapter extends FragmentPagerAdapter{

        public VideoFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new LocalVideoFragment();
                case 1:
                    return new OnlineVideoFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPfUtil.saveLong(this, SharedPfUtil.KEY.VIDEO_LAST_POSITION,0l);
    }
}