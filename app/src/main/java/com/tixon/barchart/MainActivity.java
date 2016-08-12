package com.tixon.barchart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tixon.barchart.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        int pageCount = calculatePageCount(App.get(this).getAccounts().size());
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(), pageCount);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.viewPager.setAdapter(adapter);
    }

    private int calculatePageCount(int accountsSize) {
        if(accountsSize % 4 == 0) {
            return accountsSize/4;
        }
        return accountsSize/4+1;
    }
}
