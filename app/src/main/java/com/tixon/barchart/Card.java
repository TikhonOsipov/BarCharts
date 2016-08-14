package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class Card extends BaseAccount {
    private int remainedValue;

    public Card(int value, int remainedValue, String balance, String name) {
        super(value, balance, name);
        this.remainedValue = remainedValue;
    }

    public int getRemainedValue() {
        return remainedValue;
    }

    public void setRemainedValue(int remainedValue) {
        this.remainedValue = remainedValue;
    }
}
