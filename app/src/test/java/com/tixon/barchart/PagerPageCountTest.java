package com.tixon.barchart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
@RunWith(JUnit4.class)
public class PagerPageCountTest {
    @Test
    public void testPagerPageCount() throws Exception {
        int accountsSize = 7;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 2);

        accountsSize = 3;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 1);

        accountsSize = 2;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 1);

        accountsSize = 1;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 1);

        accountsSize = 11;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 3);

        accountsSize = 12;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 3);

        accountsSize = 9;
        System.out.println("accountSize = " + accountsSize + ", page count = " + ((accountsSize / 4)+1));
        assertEquals(calculatePageCount(accountsSize), 3);
    }

    @Test
    public void testBarCountCalculation() throws Exception {

        for(int accountsSize = 1; accountsSize <= 12; accountsSize++) {
            System.out.println(">>> accountsSize = " + accountsSize);
            for(int page = 0; page < calculatePageCount(accountsSize); page++) {
                System.out.println("page = " + page +
                        ", barCount = " + calculateBarCountOnPage(accountsSize, page));
            }
            System.out.println("   ---   ---   ---   ");
        }
    }

    private int calculatePageCount(int accountsSize) {
        if(accountsSize % 4 == 0) {
            return accountsSize/4;
        }
        return accountsSize/4+1;
    }

    private int calculateBarCountOnPage(int accountsSize, int pageNumber) {
        int barCount = accountsSize - pageNumber * 4;
        if(barCount > 4) {
            barCount = 4;
        }
        return barCount;
    }
}
