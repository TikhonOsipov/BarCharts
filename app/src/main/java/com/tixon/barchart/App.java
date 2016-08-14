package com.tixon.barchart;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class App extends Application {
    private List<BaseAccount> accounts = new ArrayList<>();

    public List<BaseAccount> getAccounts() {
        return accounts;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        accounts.add(new Account(500, "500 €", "Account 1"));
        accounts.add(new Account(800, "800 €", "Account 2"));
        accounts.add(new Account(1200, "1 200 €", "Account 3"));
        accounts.add(new Account(700, "700 €", "Account 4"));
        accounts.add(new Account(900, "900 €", "Account 5"));
        accounts.add(new Account(1000, "1 000 €", "Account 6"));
        accounts.add(new Card(900, 800, "900 € / 800 €", "Card 1"));
        accounts.add(new Card(800, 500, "800 € / 500 €", "Card 2"));
    }
}
