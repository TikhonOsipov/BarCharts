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
     * Draws bottom line of graph
     */
    private void drawLine(Canvas canvas) {
        int bottomY = getHeight() - 2;

        int startX = 0;
        int stopX = getWidth();
        canvas.drawLine(startX, bottomY, stopX, bottomY, linePaint);
    }

    private int barCount = 0;
    private int pageNumber = 0;

    private List<Account> accounts;

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    private int calculateBarCountOnPage(int accountsSize, int pageNumber) {
        int barCount = accountsSize - pageNumber * 4;
        if(barCount > 4) {
            barCount = 4;
        }
        return barCount;
    }

    private int calculateGlobalIndex(int localIndex) {
        return pageNumber * 4 + localIndex;
    }

    public void draw(int pageNumber) {
        this.pageNumber = pageNumber;
        this.barCount = calculateBarCountOnPage(accounts.size(), pageNumber);
        invalidate();
    }

    private void drawBars(Canvas canvas) {
        calculateWidths(barCount);
        float initWidth = spaceAtSidesOfBar;
        int bottomY = getHeight() - 2;
        for(int i = 0; i < barCount; i++) {
            rectF.set(initWidth, bottomY - maxBarHeight, initWidth + barWidth, bottomY);

            int globalIndex = calculateGlobalIndex(i);
            String title = accounts.get(globalIndex).getName();
            String balance = accounts.get(globalIndex).getBalance();

            float titleWidth = titlePaint.measureText(title);
            float valueWidth = valuePaint.measureText(balance);

            float titleStartX = initWidth + barWidth/2.0f - titleWidth/2.0f;
            float valueStartX = initWidth + barWidth/2.0f - valueWidth/2.0f;

            canvas.drawText(title, titleStartX, 150, titlePaint);
            canvas.drawText(balance, valueStartX, 190, valuePaint);

            initWidth += barWidth;
            initWidth += spaceBetweenBars;
            canvas.drawRect(rectF, p);
        }
    }

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
        titlePaint.setColor(whiteColor);
        titlePaint.setTextSize(Utils.dpToPx(9.0f, getContext()));

        Typeface robotoRegular = Typeface.createFromAsset(getContext().getAssets(), ROBOTO_REGULAR_PATH);
        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(whiteColor);
        valuePaint.setTextSize(Utils.dpToPx(11.0f, getContext()));
    }

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
}
