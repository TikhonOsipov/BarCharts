package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class Account implements MoneyKeeper {
    private int value;
    private String balance;
    private String name;

    public Account(int value, String balance, String name) {
        this.value = value;
        this.balance = balance;
        this.name = name;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String getBalance() {
        return balance;
    }

    @Override
    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
