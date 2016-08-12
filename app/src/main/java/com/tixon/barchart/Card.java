package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class Card extends Account implements MoneyKeeper {
    private int maxValue;

    public Card(int value, int maxValue, String balance, String name) {
        super(value, balance, name);
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
