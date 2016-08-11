package com.tixon.barchart;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tixon.barchart.databinding.ChartFragmentBinding;

/**
 * Created by tikhon.osipov on 11.08.2016
 */
public class FragmentChart extends Fragment {
    ChartFragmentBinding binding;

    public static FragmentChart newInstance(int position) {
        FragmentChart fragment = new FragmentChart();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chart_fragment, container, false);
        Bundle args = getArguments();
        int position = args.getInt("position", 0);
        Log.d("myLogs", getClass().getSimpleName() + ": position: " + position);
        binding.text.setText(String.valueOf(position));
        binding.chart.draw(position);
        return binding.getRoot();
    }
}
