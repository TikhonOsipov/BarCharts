package com.tixon.barchart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by tikhon.osipov on 11.08.2016
 */
public class BarChart extends ImageView {
    Paint p = new Paint();
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
        int cX = getWidth()/2;
        int cY = getWidth()/2;
        switch (n) {
            case 0:
                canvas.drawRect(cX-40, cY-40, cX+40, cY+40, p);
                break;
            case 1:
                canvas.drawCircle(cX, cY, 20, p);
                break;
            case 2:
                rectF.set(cX-40, cY-40, cX+40, cY+40);
                canvas.drawRoundRect(rectF, 16, 16, p);
                break;
        }
    }

    private int n = 0;

    public void draw(int type) {
        this.n = type;
        invalidate();
    }

    private void init() {
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.parseColor("#ffffff"));
        p.setStyle(Paint.Style.FILL);
    }
}
