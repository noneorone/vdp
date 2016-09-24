package com.dmio.org.tut.view.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.dmio.org.tut.R;

public class WaveView extends ImageView {
    protected static final int LARGE = 1;
    protected static final int MIDDLE = 2;
    protected static final int LITTLE = 3;

    private int mAboveWaveAlpha;
    private int mBlowWaveAlpha;
    private int mAboveWaveColor;
    private int mBlowWaveColor;
    private int mWaveBgColor;
    private int mProgress;
    private int mMaskRes;
    private float mWaveHeight;
    private float mWaveMultiple;
    private float mWaveHz;

    private int mWaveToTop;

    private final int DEFAULT_ABOVE_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_BLOW_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_PROGRESS = 0;

    private Paint maskPaint;
    private int mPosX = 0, mPosY = 0;

    private static Bitmap mMask;

    private final int WAVE_HEIGHT_LARGE = 16;
    private final int WAVE_HEIGHT_MIDDLE = 8;
    private final int WAVE_HEIGHT_LITTLE = 5;

    private final float WAVE_LENGTH_MULTIPLE_LARGE = 1.5f;
    private final float WAVE_LENGTH_MULTIPLE_MIDDLE = 1f;
    private final float WAVE_LENGTH_MULTIPLE_LITTLE = 0.5f;

    private final float WAVE_HZ_FAST = 0.13f;
    private final float WAVE_HZ_NORMAL = 0.09f;
    private final float WAVE_HZ_SLOW = 0.05f;

    public final int DEFAULT_ABOVE_WAVE_ALPHA = 80;
    public final int DEFAULT_BLOW_WAVE_ALPHA = 50;

    private final float X_SPACE = 20;
    private final double PI2 = 2 * Math.PI;

    private Path mAboveWavePath = new Path();
    private Path mBlowWavePath = new Path();

    private Paint mAboveWavePaint = new Paint();
    private Paint mBlowWavePaint = new Paint();

    private float mWaveLength;

    private float mAboveOffset = 0.0f;
    private float mBlowOffset;

    private RefreshProgressRunnable mRefreshProgressRunnable;

    // ω
    private double omega;

    private Bitmap mBitmap;

    public void setAboveWaveShader(Shader aboveWaveShader) {
        mAboveWavePaint.setShader(aboveWaveShader);
    }

    public void setBlowWaveShader(Shader blowWaveShader) {
        mBlowWavePaint.setShader(blowWaveShader);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        int aboveWaveColor = attributes.getColor(R.styleable.WaveView_above_wave_color, DEFAULT_ABOVE_WAVE_COLOR);
        int blowWaveColor = attributes.getColor(R.styleable.WaveView_blow_wave_color, DEFAULT_BLOW_WAVE_COLOR);
        int waveBgColor = attributes.getColor(R.styleable.WaveView_wave_bg_color, DEFAULT_BLOW_WAVE_COLOR);
        mAboveWaveColor = aboveWaveColor & 0x00FFFFFF;
        mBlowWaveColor = blowWaveColor & 0x00FFFFFF;
        mWaveBgColor = waveBgColor & 0xFFFFFFFF;
        mAboveWaveAlpha = (aboveWaveColor & 0xFF000000) >> 24;
        mBlowWaveAlpha = (blowWaveColor & 0xFF000000) >> 24;
        mProgress = attributes.getInt(R.styleable.WaveView_progress, DEFAULT_PROGRESS);
        mMaskRes = attributes.getResourceId(R.styleable.WaveView_wave_mask_res, 0);
        int waveHeight = attributes.getInt(R.styleable.WaveView_wave_height, MIDDLE);
        int waveMultiple = attributes.getInt(R.styleable.WaveView_wave_length, LARGE);
        int waveHz = attributes.getInt(R.styleable.WaveView_wave_hz, MIDDLE);
        attributes.recycle();

        initView();
        initializeWaveSize(waveMultiple, waveHeight, waveHz);
        initializePainters();

        setProgress(mProgress);
    }

    private void initView() {
        if (mMaskRes > 0) {
            mMask = BitmapFactory.decodeResource(getResources(), mMaskRes);
        }
    }

//    @SuppressWarnings("deprecation")
//	@Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    	if(mMask != null) {
//			Drawable drawable = getResources().getDrawable(R.drawable.test_mask);
//    		if(MeasureSpec.EXACTLY != MeasureSpec.getMode(widthMeasureSpec)) {
//    			widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicWidth(), MeasureSpec.EXACTLY);
//    		}
//    		if(MeasureSpec.EXACTLY != MeasureSpec.getMode(heightMeasureSpec)) {
//    			heightMeasureSpec = MeasureSpec.makeMeasureSpec(drawable.getIntrinsicHeight(), MeasureSpec.EXACTLY);
//    		}
//    	}
//    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            genMaskBitmap();
        }
    }

    private void genMaskBitmap() {
        if (mMask != null && mMaskRes > 0) {
            if (mMask.isRecycled()) {
                mMask = BitmapFactory.decodeResource(getResources(), mMaskRes);
            }
            Bitmap newBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            canvas.scale((getWidth() * 1.0f) / mMask.getWidth(), (getHeight() * 1.0f) / mMask.getHeight());
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(mMask, 0, 0, null);
            mMask.recycle();
            mMask = newBitmap;
        }
    }

    @SuppressLint({"DrawAllocation"})
    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress > 0) {
            if (mMask != null && mMaskRes > 0) {
                if (mMask.isRecycled()) {
                    genMaskBitmap();
                }
                if (mBitmap == null) {
                    mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                }
                Canvas tmpCanvas = new Canvas(mBitmap);
                tmpCanvas.drawColor(mWaveBgColor);
                tmpCanvas.drawPath(mBlowWavePath, mBlowWavePaint);
                tmpCanvas.drawPath(mAboveWavePath, mAboveWavePaint);
                tmpCanvas.drawBitmap(mMask, mPosX, mPosY, maskPaint);
                canvas.drawBitmap(mBitmap, 0, 0, null);
            } else {
                canvas.drawColor(mWaveBgColor);
                canvas.drawPath(mBlowWavePath, mBlowWavePaint);
                canvas.drawPath(mAboveWavePath, mAboveWavePaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        if (mMask != null && !mMask.isRecycled()) {
            mMask.recycle();
        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress > 100 ? 100 : progress;
        computeWaveToTop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToTop();
            if (mWaveLength == 0) {
                startWave();
            }
        }
    }

    private void computeWaveToTop() {
        mWaveToTop = (int) (getHeight() * (1f - mProgress / 100f));
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (View.GONE == visibility) {
            removeCallbacks(mRefreshProgressRunnable);
        } else {
            removeCallbacks(mRefreshProgressRunnable);
            mRefreshProgressRunnable = new RefreshProgressRunnable();
            post(mRefreshProgressRunnable);
        }
    }

    public void initializeWaveSize(int waveMultiple, int waveHeight, int waveHz) {
        mWaveMultiple = getWaveMultiple(waveMultiple);
        mWaveHeight = getWaveHeight(waveHeight);
        mWaveHz = getWaveHz(waveHz);
        mBlowOffset = mWaveHeight * 0.4f;
    }

    public void initializePainters() {
        maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mAboveWavePaint.setColor(mAboveWaveColor);
        mAboveWavePaint.setAlpha(mAboveWaveAlpha);
        mAboveWavePaint.setAntiAlias(true);

        mBlowWavePaint.setColor(mBlowWaveColor);
        mBlowWavePaint.setAlpha(mBlowWaveAlpha);
        mBlowWavePaint.setAntiAlias(true);
    }

    private float getWaveMultiple(int size) {
        switch (size) {
            case WaveView.LARGE:
                return WAVE_LENGTH_MULTIPLE_LARGE;
            case WaveView.MIDDLE:
                return WAVE_LENGTH_MULTIPLE_MIDDLE;
            case WaveView.LITTLE:
                return WAVE_LENGTH_MULTIPLE_LITTLE;
        }
        return 0;
    }

    private int getWaveHeight(int size) {
        switch (size) {
            case WaveView.LARGE:
                return WAVE_HEIGHT_LARGE;
            case WaveView.MIDDLE:
                return WAVE_HEIGHT_MIDDLE;
            case WaveView.LITTLE:
                return WAVE_HEIGHT_LITTLE;
        }
        return 0;
    }

    private float getWaveHz(int size) {
        switch (size) {
            case WaveView.LARGE:
                return WAVE_HZ_FAST;
            case WaveView.MIDDLE:
                return WAVE_HZ_NORMAL;
            case WaveView.LITTLE:
                return WAVE_HZ_SLOW;
        }
        return 0;
    }

    private void startWave() {
        if (getWidth() != 0) {
            int width = getWidth();
            mWaveLength = width * mWaveMultiple;
            omega = PI2 / mWaveLength;
        }
    }

    private void getWaveOffset() {
        if (mBlowOffset > Float.MAX_VALUE - 100) {
            mBlowOffset = 0;
        } else {
            mBlowOffset += mWaveHz;
        }

        if (mAboveOffset > Float.MAX_VALUE - 100) {
            mAboveOffset = 0;
        } else {
            mAboveOffset += mWaveHz;
        }
    }

    private void calculatePath() {
        mAboveWavePath.reset();
        mBlowWavePath.reset();

        getWaveOffset();

        float y;
        mAboveWavePath.moveTo(0, getBottom());
        mAboveWavePath.lineTo(0, mWaveToTop);
        for (float x = 0; x <= getRight(); x += X_SPACE) {
            y = (float) (mWaveHeight * Math.sin(omega * x + mAboveOffset) + mWaveHeight);
            mAboveWavePath.lineTo(x, mWaveToTop + y);
        }
        mAboveWavePath.lineTo(getRight(), mWaveToTop);
        mAboveWavePath.lineTo(getRight(), getBottom());

        mBlowWavePath.moveTo(0, getBottom());
        mBlowWavePath.lineTo(0, mWaveToTop);
        for (float x = 0; x <= getRight(); x += X_SPACE) {
            y = (float) (mWaveHeight * Math.sin(omega * x + mBlowOffset) + mWaveHeight);
            mBlowWavePath.lineTo(x, mWaveToTop + y);
        }
        mBlowWavePath.lineTo(getRight(), mWaveToTop);
        mBlowWavePath.lineTo(getRight(), getBottom());
    }

    private class RefreshProgressRunnable implements Runnable {
        public void run() {
            synchronized (WaveView.this) {
                long start = System.currentTimeMillis();

                calculatePath();

                invalidate();

                long gap = 16 - (System.currentTimeMillis() - start);
                postDelayed(this, gap < 0 ? 0 : gap);
            }
        }
    }
}
