package com.tixon.barchart;

import java.util.List;

/**
 * Created by tikhon.osipov on 15.08.2016
 */
public class BarChartAdapter implements IBarChartAdapter {
    private List<BaseAccount> accounts;

    public BarChartAdapter(List<BaseAccount> accounts) {
        this.accounts = accounts;
    }

    @Override
    public int getTotalValue(int position) {
        return accounts.get(position).getValue();
    }

    @Override
    public int getRemainedValue(int position) {
        return ((Card) accounts.get(position)).getRemainedValue();
    }

    @Override
    public String getName(int position) {
        return accounts.get(position).getName();
    }

    @Override
    public String getBalance(int position) {
        return accounts.get(position).getBalance();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public Object get(int position) {
        return accounts.get(position);
    }
}
