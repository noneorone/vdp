package com.noo.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HeartSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder holder;
    private boolean mloop;
    private Canvas canvas;
    private int miCount;
    private int y = 50;

    public HeartSurfaceView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);
        this.setFocusable(true);
        this.setKeepScreenOn(true);
        this.mloop = true;
    }

    public HeartSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeartSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.mloop = false;
    }

    @Override
    public void run() {
        while (mloop) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (holder) {
                drawHeart();
            }
        }
    }

    private void drawHeart() {
        canvas = holder.lockCanvas();
        if (null == holder || null == canvas) {
            return;
        }

        if (miCount < 100) {
            miCount++;
        } else {
            miCount = 0;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, 150, 220, paint);

        switch (miCount % 6) {
            case 0:
                paint.setColor(Color.BLUE);
                break;
            case 1:
                paint.setColor(Color.GREEN);
                break;
            case 2:
                paint.setColor(Color.YELLOW);
                break;
            case 3:
                paint.setColor(Color.RED);
                break;
            case 4:
                paint.setColor(Color.argb(255, 255, 181, 216));
                break;
            case 5:
                paint.setColor(Color.argb(255, 0, 255, 255));
                break;
            default:
                paint.setColor(Color.WHITE);
                break;
        }

        try {
            int i, j;
            double x, y, r;

            for (i = 0; i <= 90; i++) {
                for (j = 0; j <= 90; j++) {
                    r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 20;
                    x = r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i) + 320 / 2;
                    y = -r * Math.sin(Math.PI / 45 * j) + 400 / 4;
                    canvas.drawPoint((float) x, (float) y, paint);
                }
            }
            paint.setTextSize(32);
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
            RectF rect = new RectF(60, 400, 260, 405);

            canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);
            canvas.drawText("Hello World", 75, 400, paint);

            holder.unlockCanvasAndPost(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
