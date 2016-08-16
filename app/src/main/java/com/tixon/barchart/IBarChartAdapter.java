package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 15.08.2016
 */
public interface IBarChartAdapter {
    int getTotalValue(int position);
    int getRemainedValue(int position);

    String getName(int position);
    String getBalance(int position);
    int getItemCount();

    Object get(int position);
}
