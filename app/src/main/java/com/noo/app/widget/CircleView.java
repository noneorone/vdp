package com.noo.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Mars on 2015/10/21.
 */
public class CircleView extends View {

    private Context mContext;

    private Paint paint;

    /** 圆环宽度 */
    private int strokeWidth = 5;

    /** 文字大小 */
    private int textSize = 30;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;

        paint = new Paint();
    }


    /**
     * 重写OnDraw（）函数，在每次重绘时自主实现绘图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int strokeWidth = 5;

        // 圆心坐标
        int center = getWidth() / 2;
        // 圆形半径
        int radius = center - strokeWidth / 2;

        /** 设置画笔基本属性 */
        // 抗锯齿功能
        paint.setAntiAlias(true);
        // 画笔颜色
        paint.setColor(Color.RED);
        // 画笔填充样式
        paint.setStyle(Paint.Style.STROKE);
        // 画笔宽度
        paint.setStrokeWidth(strokeWidth);
        // 画笔阴影样式
        paint.setShadowLayer(5, 20, 20, Color.BLUE);

        /** 设置画布 */
        // 设置画布背景色
        canvas.drawRGB(255, 255, 255);
        // 在画布上用画笔画圆
        canvas.drawCircle(center, center, radius, paint);


        /** 画文本 */
        paint.setStrokeWidth(0);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText("hello");
        canvas.drawText("hello", center - textWidth / 2, center + textSize / 2, paint);


        /** 画弧度 */
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        // 定义圆弧形状和大小的界限
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(oval,0,360 * 0.30f, false, paint);


    }
}
