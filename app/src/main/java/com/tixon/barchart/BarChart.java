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
    private static final float HEIGHT = 196.0f;

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

    private static final float HEIGHT_TO_MAX_BAR_HEIGHT = HEIGHT/85.0f;
    private static final float HEIGHT_TO_SPACE_BETWEEN_VALUE_TEXT_VIEW = HEIGHT/16.0f;
    private static final float HEIGHT_TO_SPACE_BETWEEN_TITLE_TEXT_VIEW = HEIGHT/13.0f;
    private static final float HEIGHT_TO_SPACE_BETWEEN_BOTTOM_AND_BASELINE = HEIGHT/36.0f;

    private static final String ROBOTO_LIGHT_PATH = "fonts/roboto_light.ttf";
    private static final String ROBOTO_REGULAR_PATH = "fonts/roboto_regular.ttf";

    private float barWidth;
    private float spaceBetweenBars;
    private float spaceAtSidesOfBar;
    private float maxBarHeight;

    private int barCount = 0;
    private int pageNumber = 0;

    /**
     * Heights of bars that are calculated in
     * @see #calculateHeightsOfBars()
     */
    private List<Float> heights = new ArrayList<>();

    private IBarChartAdapter adapter;

    public void setAdapter(IBarChartAdapter adapter) {
        this.adapter = adapter;
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
        int bottomY = getHeight() - getHeight()/(int)HEIGHT_TO_SPACE_BETWEEN_BOTTOM_AND_BASELINE;

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
        this.barCount = calculateBarCountOnPage(adapter.getItemCount(), pageNumber);
        invalidate();
    }

    /**
     * Draws bars using calculated widths and heights
     */
    private void drawBars(Canvas canvas) {
        calculateWidths(barCount);
        //calculateHeightsOfBars();
        calculateHeightsOfBars(pageNumber*4);
        float initWidth = spaceAtSidesOfBar;
        int bottomY = getHeight() - getHeight()/(int)HEIGHT_TO_SPACE_BETWEEN_BOTTOM_AND_BASELINE;
        for(int i = 0; i < barCount; i++) {
            int globalIndex = calculateGlobalIndex(i);
            String title = adapter.getName(globalIndex);
            String balance = adapter.getBalance(globalIndex);

            //float barHeight = heights.get(globalIndex);
            float barHeight = heights.get(i);

            if(barHeight >= 0) {
                rectF.set(initWidth, bottomY - barHeight, initWidth + barWidth, bottomY);
            } else {
                rectF.set(initWidth, bottomY, initWidth + barWidth, bottomY - barHeight);
            }

            float titleWidth = titlePaint.measureText(title);
            float valueWidth = valuePaint.measureText(balance);

            float titleStartX = initWidth + barWidth/2.0f - titleWidth/2.0f;
            float valueStartX = initWidth + barWidth/2.0f - valueWidth/2.0f;

            float valueStartY = bottomY - (maxBarHeight + getHeight()/HEIGHT_TO_SPACE_BETWEEN_VALUE_TEXT_VIEW);
            float titleStartY = bottomY - (maxBarHeight + getHeight()/ HEIGHT_TO_SPACE_BETWEEN_TITLE_TEXT_VIEW + 2 * getHeight()/HEIGHT_TO_SPACE_BETWEEN_VALUE_TEXT_VIEW);
            canvas.drawText(title, titleStartX, titleStartY, titlePaint);
            canvas.drawText(balance, valueStartX, valueStartY, valuePaint);

            if(adapter.get(globalIndex) instanceof Card) {
                canvas.drawRect(rectF, linePaint);
                float remainedValue = (float) adapter.getRemainedValue(globalIndex);
                float totalValue = (float) adapter.getTotalValue(globalIndex);
                float remainedHeight = barHeight * remainedValue / totalValue;
                rectF.set(initWidth, bottomY - remainedHeight, initWidth + barWidth, bottomY);
                canvas.drawRect(rectF, p);
            } else if(adapter.get(globalIndex) instanceof Account) {
                canvas.drawRect(rectF, p);
            }

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
        int minValue = findMinPositiveValue();
        int maxValue = findMaxValue();
        //if we have only one value, so draw maxBarHeight for positive
        // or -1*(maxBarHeight/4) for negative.
        if(adapter.getItemCount() == 1) {
            calculateHeightOfSingleBarOnPage(0);
            return;
        }

        for(int i = 0; i < adapter.getItemCount(); i++) {
            int value = adapter.getTotalValue(i);
            float height = calculateHeightOfBar(Math.abs(value), minValue, maxValue);
            if(value < 0) {
                height *= -1;
            }
            heights.add(height);
        }
    }

    private void calculateHeightsOfBars(int startIndex) {
        int minValue = findMinPositiveValue(startIndex);
        int maxValue = findMaxValue(startIndex);
        //if we have only one value, so draw maxBarHeight for positive
        // or -1*(maxBarHeight/4) for negative.
        if(barCount == 1) {
            calculateHeightOfSingleBarOnPage(startIndex);
            return;
        }
        for(int i = startIndex; i < startIndex+barCount; i++) {
            int value = adapter.getTotalValue(i);
            float height = calculateHeightOfBar(Math.abs(value), minValue, maxValue);
            if(value < 0) {
                height *= -1;
            }
            heights.add(height);
        }
    }

    private void calculateHeightOfSingleBarOnPage(int index) {
        if(adapter.getTotalValue(index) > 0) {
            heights.add(maxBarHeight);
        } else if(adapter.getTotalValue(index) == 0) {
            heights.add(0.0f);
        }
        else {
            heights.add(-maxBarHeight/4.0f);
        }
    }

    /**
     * Calculates height of single bar in pixels depending on balance of account.
     * Uses linear interpolation (n = 1)
     * @param value Balance of account
     * @param minValue Minimal balance in accounts list
     * @param maxValue Maximal balance in accounts list
     * @return height of single bar
     */
    private float calculateHeightOfBar(int value, int minValue, int maxValue) {
        float minBarHeight = maxBarHeight / 4.0f;
        //if Math.abs of negative value is < than calculated minimum positive value
        if(value < minValue) {
            return (float)value*minBarHeight/(float)minValue;
        }
        float firstFraction = ((float)(value - maxValue)) / ((float)(minValue - maxValue));
        float secondFraction = ((float)(value - minValue)) / ((float)(maxValue - minValue));
        return minBarHeight * firstFraction + maxBarHeight * secondFraction;
    }

    /**
     * Finds minimal balance in accounts list
     * @return minimal balance
     */
    private int findMinValue() {
        int minValue = adapter.getTotalValue(0);
        if(adapter.getItemCount() > 1) {
            for(int i = 1; i < adapter.getItemCount(); i++) {
                if(adapter.getTotalValue(i) <= minValue) {
                    minValue = adapter.getTotalValue(i);
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
        int maxValue = adapter.getTotalValue(0);
        if(adapter.getItemCount() > 1) {
            for(int i = 1; i < adapter.getItemCount(); i++) {
                if(adapter.getTotalValue(i) >= maxValue) {
                    maxValue = adapter.getTotalValue(i);
                }
            }
        }
        return maxValue;
    }

    /**
     * Finds maximal balance in accounts list from specified start index
     * @return maximal balance
     */
    private int findMaxValue(int startIndex) {
        int maxValue = adapter.getTotalValue(startIndex);
        if(barCount > 1) {
            for(int i = startIndex+1; i < startIndex+barCount; i++) {
                if(adapter.getTotalValue(i) >= maxValue) {
                    maxValue = adapter.getTotalValue(i);
                }
            }
        }
        return maxValue;
    }

    /**
     * Finds minimal positive balance in accounts list
     * @return minimal balance
     */
    private int findMinPositiveValue() {
        int minValue = Integer.MAX_VALUE;
        if(adapter.getItemCount() > 0) {
            for(int i = 0; i < adapter.getItemCount(); i++) {
                if((adapter.getTotalValue(i) < minValue) && (adapter.getTotalValue(i) >= 0)) {
                    minValue = adapter.getTotalValue(i);
                }
            }
        }
        return minValue;
    }

    /**
     * Finds minimal positive balance in accounts list from specified start index
     * @return minimal balance
     */
    private int findMinPositiveValue(int startIndex) {
        int minValue = Integer.MAX_VALUE;
        if(adapter.getItemCount() > 0) {
            for(int i = startIndex; i < startIndex+barCount; i++) {
                if((adapter.getTotalValue(i) < minValue) && (adapter.getTotalValue(i) >= 0)) {
                    minValue = adapter.getTotalValue(i);
                }
            }
        }
        return minValue;
    }

    public int getClickedIndex(float x) {
        float[] startPositions = calculateStartPositions(barCount);
        float[] endPositions = calculateEndPositions(barCount);
        for(int i = 0; i < startPositions.length; i++) {
            if(x > startPositions[i] && x < endPositions[i]) {
                return calculateGlobalIndex(i);
            }
        }
        return -1;
    }

    private float[] calculateStartPositions(int barCount) {
        float[] startPositions = new float[barCount];
        startPositions[0] = spaceAtSidesOfBar;
        if(startPositions.length > 1) {
            for(int i = 1; i < startPositions.length; i++) {
                startPositions[i] = startPositions[i-1] + barWidth + spaceBetweenBars;
            }
        }
        return startPositions;
    }

    private float[] calculateEndPositions(int barCount) {
        float[] endPositions = new float[barCount];
        endPositions[0] = spaceAtSidesOfBar + barWidth;
        if(endPositions.length > 1) {
            for (int i = 1; i < endPositions.length; i++) {
                endPositions[i] = endPositions[i-1] + spaceBetweenBars + barWidth;
            }
        }
        return endPositions;
    }
}
