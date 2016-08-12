package com.tixon.barchart;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class Account {
    private int value;
    private String balance;
    private String name;

    public Account(int value, String balance, String name) {
        this.value = value;
        this.balance = balance;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
