package com.tixon.barchart;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by tikhon.osipov on 11.08.2016
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private static final int PAGE_COUNT = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentChart.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
