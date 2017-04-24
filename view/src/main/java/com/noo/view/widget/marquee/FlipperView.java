package com.noo.view.widget.marquee;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.noo.view.R;

import java.util.Iterator;
import java.util.Map;

/**
 * 上下滚动的文本，比如公告栏显示，支持点击
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/4/20 14:18<br/>
 * @since 1.0
 */
public class FlipperView extends LinearLayout {

    private Context mContext;
    private ViewFlipper viewFlipper;
    private View flipperTextView;
    private Map<Object, Object> textData;

    private int textColor;
    private float textSize;
    private boolean textSingleLine;
    private int flipInterval;
    private int flipBackgroundColor;
    private boolean deletable;

    private OnViewClickListener mOnViewClickListener;

    public interface OnViewClickListener {
        void onClick(View v, Object key, Object value);
    }

    public FlipperView(Context context) {
        super(context);
        mContext = context;
        initBasicView(null);
    }


    public FlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView(attrs);
    }

    /**
     * 设置数据和监听
     *
     * @param textData
     * @param onViewClickListener
     */
    public void setDataAndListener(Map<Object, Object> textData, OnViewClickListener onViewClickListener) {
        this.textData = textData;
        this.mOnViewClickListener = onViewClickListener;
        initFlipperTextView();
    }

    /**
     * 初始化基础视图
     *
     * @param attrs
     */
    public void initBasicView(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.FlipperView);
        deletable = typedArray.getBoolean(R.styleable.FlipperView_deletable, true);
        flipInterval = typedArray.getInt(R.styleable.FlipperView_flip_interval, 3000);
        flipBackgroundColor = typedArray.getColor(R.styleable.FlipperView_flip_background_color, Color.WHITE);
        textColor = typedArray.getColor(R.styleable.FlipperView_text_color, Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.FlipperView_text_size, 12f);
        textSingleLine = typedArray.getBoolean(R.styleable.FlipperView_text_single_line, true);
        typedArray.recycle();

        flipperTextView = LayoutInflater.from(mContext).inflate(R.layout.flipper_view, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(flipperTextView, layoutParams);

        viewFlipper = (ViewFlipper) flipperTextView.findViewById(R.id.vf_container);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.flipper_slide_in_bottom));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.flipper_slide_out_top));
        viewFlipper.startFlipping();
        viewFlipper.setBackgroundColor(flipBackgroundColor);
        viewFlipper.setFlipInterval(flipInterval);

        if (deletable) {
            ImageView iv = (ImageView) flipperTextView.findViewById(R.id.iv_delete);
            iv.setVisibility(View.VISIBLE);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flipperTextView.setVisibility(GONE);
                    releaseResources();
                }
            });
        }
    }

    /**
     * 初始化文本控件
     */
    public void initFlipperTextView() {
        if (textData == null || textData.isEmpty()) {
            return;
        }

        viewFlipper.removeAllViews();
        if (textData.size() == 1) {
            viewFlipper.stopFlipping();
        }

        Iterator<Map.Entry<Object, Object>> iterator = textData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            TextView textView = new TextView(mContext);
            textView.setTag(key);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setTextColor(textColor);
            textView.setText(value.toString());
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            if (textSingleLine) {
                textView.setLines(1);
            }
            if (mOnViewClickListener != null) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnViewClickListener.onClick(v, key, value);
                    }
                });
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            viewFlipper.addView(textView, lp);
        }
    }

    /**
     * 释放资源
     */
    public void releaseResources() {
        if (flipperTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            flipperTextView = null;
        }
    }

}