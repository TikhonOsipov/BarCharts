package com.tixon.barchart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tikhon.osipov on 11.08.2016
 */
public class BarChart extends ImageView {
    private static final float WIDTH = 360.0f;

    private static final float WIDTH_TO_BAR_WIDTH_4 = WIDTH/64.0f;
    private static final float WIDTH_TO_BAR_WIDTH_3 = WIDTH/75.0f;
    private static final float WIDTH_TO_BAR_WIDTH_2 = WIDTH/85.0f;
    private static final float WIDTH_TO_BAR_WIDTH_1 = WIDTH/85.0f;

    private static final float WIDTH_TO_SPACE_BETWEEN_4 = WIDTH/24.0f;
    private static final float WIDTH_TO_SPACE_BETWEEN_3 = WIDTH/40.0f;
    private static final float WIDTH_TO_SPACE_BETWEEN_2 = WIDTH/64.0f;

    private static final float WIDTH_TO_SPACE_SIDE_4 = WIDTH/16.0f;
    private static final float WIDTH_TO_SPACE_SIDE_3 = WIDTH/28.0f;
    private static final float WIDTH_TO_SPACE_SIDE_2 = WIDTH/64.0f;
    private static final float WIDTH_TO_SPACE_SIDE_1 = WIDTH/138.0f;

    private static final float HEIGHT_TO_MAX_BAR_HEIGHT = 1.9f;
    private static final String ROBOTO_LIGHT_PATH = "fonts/roboto_light.ttf";
    private static final String ROBOTO_REGULAR_PATH = "fonts/roboto_regular.ttf";

    private float barWidth;
    private float spaceBetweenBars;
    private float spaceAtSidesOfBar;
    private float maxBarHeight;

    private int barCount = 0;
    private int pageNumber = 0;

    /**
     * Data to be shown on the chart
     */
    private List<BaseAccount> accounts;

    /**
     * Heights of bars that are calculated in
     * @see #calculateHeightsOfBars()
     */
    private List<Float> heights = new ArrayList<>();

    public void setAccounts(List<BaseAccount> accounts) {
        this.accounts = accounts;
    }

    Paint p = new Paint();
    Paint linePaint = new Paint();
    Paint titlePaint = new Paint();
    Paint valuePaint = new Paint();
    RectF rectF = new RectF();

    public BarChart(Context context) {
        super(context);
        init();
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("unused")
    public BarChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawBars(canvas);
    }

    /**
     * Draws bottom line of the chart
     */
    private void drawLine(Canvas canvas) {
        int bottomY = getHeight() - 2;

        int startX = 0;
        int stopX = getWidth();
        canvas.drawLine(startX, bottomY, stopX, bottomY, linePaint);
    }

    /**
     * Calculates bar count on concrete page
     * @param accountsSize Size of Accounts list
     * @param pageNumber Number of page
     * @return Bar count on concrete page
     */
    private int calculateBarCountOnPage(int accountsSize, int pageNumber) {
        int barCount = accountsSize - pageNumber * 4;
        if(barCount > 4) {
            barCount = 4;
        }
        return barCount;
    }

    /**
     * Calculates index in Accounts list using page number and bar number on this page
     * @param localIndex Bar number on selected page
     * @return Index in Accounts list
     */
    private int calculateGlobalIndex(int localIndex) {
        return pageNumber * 4 + localIndex;
    }

    /**
     * Sets page number, bar quantity to be shown at this page and calls onDraw() method
     * @param pageNumber Number of page
     */
    public void draw(int pageNumber) {
        this.pageNumber = pageNumber;
        this.barCount = calculateBarCountOnPage(accounts.size(), pageNumber);
        invalidate();
    }

    /**
     * Draws bars using calculated widths and heights
     */
    private void drawBars(Canvas canvas) {
        calculateWidths(barCount);
        calculateHeightsOfBars();
        float initWidth = spaceAtSidesOfBar;
        int bottomY = getHeight() - 2;
        for(int i = 0; i < barCount; i++) {
            int globalIndex = calculateGlobalIndex(i);
            String title = accounts.get(globalIndex).getName();
            String balance = accounts.get(globalIndex).getBalance();

            float barHeight = heights.get(globalIndex);

            rectF.set(initWidth, bottomY - barHeight, initWidth + barWidth, bottomY);

            float titleWidth = titlePaint.measureText(title);
            float valueWidth = valuePaint.measureText(balance);

            float titleStartX = initWidth + barWidth/2.0f - titleWidth/2.0f;
            float valueStartX = initWidth + barWidth/2.0f - valueWidth/2.0f;

            canvas.drawText(title, titleStartX, 150, titlePaint);
            canvas.drawText(balance, valueStartX, 190, valuePaint);

            if(accounts.get(globalIndex) instanceof Card) {
                canvas.drawRect(rectF, linePaint);
                float remainedValue = (float) ((Card) accounts.get(globalIndex)).getRemainedValue();
                float totalValue = (float) accounts.get(globalIndex).getValue();
                float remainedHeight = barHeight * remainedValue / totalValue;
                rectF.set(initWidth, bottomY - remainedHeight, initWidth + barWidth, bottomY);
                canvas.drawRect(rectF, p);
            } else if(accounts.get(globalIndex) instanceof Account) {
                canvas.drawRect(rectF, p);
            }

            /*if(accounts.get(i)) {
                float totalHeight = (barHeight * ((Card) accounts.get(i)).getRemainedValue()) / accounts.get(i).getValue();
                rectF.set(initWidth, bottomY - totalHeight, initWidth + barWidth, bottomY);
                canvas.drawRect(rectF, linePaint);
            }*/

            initWidth += barWidth;
            initWidth += spaceBetweenBars;
        }
    }

    /**
     * Sets up paints, typefaces and other stuff for drawing
     */
    private void init() {
        int whiteColor = Color.parseColor("#ffffff");

        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(whiteColor);
        p.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(whiteColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1.0f);

        Typeface robotoLight = Typeface.createFromAsset(getContext().getAssets(), ROBOTO_LIGHT_PATH);
        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTypeface(robotoLight);
        titlePaint.setColor(whiteColor);
        titlePaint.setTextSize(Utils.dpToPx(9.0f, getContext()));

        Typeface robotoRegular = Typeface.createFromAsset(getContext().getAssets(), ROBOTO_REGULAR_PATH);
        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setTypeface(robotoRegular);
        valuePaint.setColor(whiteColor);
        valuePaint.setTextSize(Utils.dpToPx(11.0f, getContext()));
    }

    /**
     * Calculates widths of bars, spaces between bars and side spaces of edge bars.
     * Width depends on bar quantity that is shown on the chart.
     * @param barCount Bar quantity that is shown on the chart
     */
    private void calculateWidths(int barCount) {
        int width = getWidth();
        int height = getHeight();
        switch (barCount) {
            case 4:
                barWidth = width / WIDTH_TO_BAR_WIDTH_4;
                spaceBetweenBars = width / WIDTH_TO_SPACE_BETWEEN_4;
                spaceAtSidesOfBar = width / WIDTH_TO_SPACE_SIDE_4;
                break;
            case 3:
                barWidth = width / WIDTH_TO_BAR_WIDTH_3;
                spaceBetweenBars = width / WIDTH_TO_SPACE_BETWEEN_3;
                spaceAtSidesOfBar = width / WIDTH_TO_SPACE_SIDE_3;
                break;
            case 2:
                barWidth = width / WIDTH_TO_BAR_WIDTH_2;
                spaceBetweenBars = width / WIDTH_TO_SPACE_BETWEEN_2;
                spaceAtSidesOfBar = width / WIDTH_TO_SPACE_SIDE_2;
                break;
            case 1:
                barWidth = width / WIDTH_TO_BAR_WIDTH_1;
                spaceAtSidesOfBar = width / WIDTH_TO_SPACE_SIDE_1;
                break;
            default: break;
        }
        maxBarHeight = height / HEIGHT_TO_MAX_BAR_HEIGHT;
    }

    /**
     * Calculates heights for each bar to be drawn on chart.
     * Adds calculated height for each bar in heights ArrayList< Float>
     */
    private void calculateHeightsOfBars() {
        int minValue = findMinValue();
        int maxValue = findMaxValue();
        for(int i = 0; i < accounts.size(); i++) {
            int value = accounts.get(i).getValue();
            float height = calculateHeightOfBar(value, minValue, maxValue);
            heights.add(height);
        }
    }

    /**
     * Calculates height of single bar in pixels depending on balance of account.
     * Uses Lagrange linear interpolation (n = 1)
     * @param value Balance of account
     * @param minValue Minimal balance in accounts list
     * @param maxValue Maximal balance in accounts list
     * @return height of single bar
     */
    private float calculateHeightOfBar(int value, int minValue, int maxValue) {
        float minBarHeight = maxBarHeight / 4.0f;
        float firstFraction = ((float)(value - maxValue)) / ((float)(minValue - maxValue));
        float secondFraction = ((float)(value - minValue)) / ((float)(maxValue - minValue));
        return minBarHeight * firstFraction + maxBarHeight * secondFraction;
    }

    /**
     * Finds minimal balance in accounts list
     * @return minimal balance
     */
    private int findMinValue() {
        int minValue = accounts.get(0).getValue();
        if(accounts.size() > 1) {
            for(int i = 1; i < accounts.size(); i++) {
                if(accounts.get(i).getValue() <= minValue) {
                    minValue = accounts.get(i).getValue();
                }
            }
        }
        return minValue;
    }

    /**
     * Finds maximal balance in accounts list
     * @return maximal balance
     */
    private int findMaxValue() {
        int maxValue = accounts.get(0).getValue();
        if(accounts.size() > 1) {
            for(int i = 1; i < accounts.size(); i++) {
                if(accounts.get(i).getValue() >= maxValue) {
                    maxValue = accounts.get(i).getValue();
                }
            }
        }
        return maxValue;
    }
}
