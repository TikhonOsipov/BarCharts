package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public interface MoneyKeeper {
    int getValue();
    void setValue(int value);
    String getBalance();
    void setBalance(String balance);
    String getName();
    void setName(String name);
}
