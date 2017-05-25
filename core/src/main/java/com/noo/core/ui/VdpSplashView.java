package com.noo.core.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noo.core.R;

/**
 * 应用启动页
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/22 17:51<br/>
 * @since 1.0
 */
public class VdpSplashView extends FrameLayout implements View.OnClickListener {

    /**
     * 各动作状态监听
     */
    public interface OnActionListener {
        /**
         * 组件消失
         */
        void onDismiss();
    }

    private static final int skipTextSize = 10;
    private static final int skipTextLpSize = 46;
    private static final int skipTextLpMargin = 16;

    private Context context;
    private OnActionListener onActionListener;

    private Activity activity;
    private boolean actionBarShowStatus;

    private TextView tvSkip;
    private ImageView ivSplash;

    private String skipText;
    private Integer duration = 6;
    private static final int delayTime = 1000;

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public VdpSplashView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VdpSplashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VdpSplashView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        init(context);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            hide();
        }
    }

    private Handler handler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (0 == duration) {
                hide();
                return;
            } else {
                setDuration(--duration);
            }
            handler.postDelayed(timerRunnable, delayTime);
        }
    };

    private void setDuration(Integer duration) {
        this.duration = duration;
        if (!TextUtils.isEmpty(skipText)) {
            tvSkip.setText(String.format(skipText + "\n%d s", duration));
        }
    }

    private void init(@NonNull Context context) {
        this.context = context;

        // 构建广告图片对象
        ivSplash = new ImageView(context);
        ivSplash.setScaleType(ImageView.ScaleType.FIT_XY);
        LayoutParams lpImage = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lpImage.gravity = Gravity.CENTER;
        ivSplash.setClickable(true);
        this.addView(ivSplash, lpImage);


        // 构建跳过的文本对象
        tvSkip = new TextView(context);
        tvSkip.setOnClickListener(this);
        tvSkip.setGravity(Gravity.CENTER);
        tvSkip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, skipTextSize);
        tvSkip.setTextColor(ContextCompat.getColor(context, R.color.c9));
        // 设置字体加粗
        tvSkip.getPaint().setTypeface(Typeface.DEFAULT_BOLD);
        // 设置文本背景样式
        GradientDrawable skipTextBg = new GradientDrawable();
        skipTextBg.setShape(GradientDrawable.OVAL);
        skipTextBg.setColor(Color.parseColor("#90ededed"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tvSkip.setBackground(skipTextBg);
        } else {
            tvSkip.setBackgroundDrawable(skipTextBg);
        }
        // 设置布局属性
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int skipTextSize = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipTextLpSize, displayMetrics)).intValue();
        int skipTextMargin = Float.valueOf(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, skipTextLpMargin, displayMetrics)).intValue();
        LayoutParams lpText = new LayoutParams(skipTextSize, skipTextSize);
        lpText.gravity = Gravity.TOP | Gravity.RIGHT;
        lpText.setMargins(0, skipTextMargin, skipTextMargin, 0);
        this.addView(tvSkip, lpText);

        setDuration(duration);
        handler.postDelayed(timerRunnable, delayTime);
    }

    /**
     * 显示过渡页面
     *
     * @param activity
     * @param listener
     */
    public static VdpSplashView show(@NonNull Activity activity, String skipText, @DrawableRes int splashImgRes, OnActionListener listener) {
        VdpSplashView splashView = show(activity, listener);
        splashView.ivSplash.setImageResource(splashImgRes);
        splashView.tvSkip.setText(skipText);
        splashView.skipText = skipText;
        return splashView;
    }

    /**
     * 显示过渡页面
     *
     * @param activity
     * @param listener
     * @return
     */
    public static VdpSplashView show(@NonNull Activity activity, OnActionListener listener) {
        VdpSplashView splash = new VdpSplashView(activity);
        if (listener != null) {
            splash.setOnActionListener(listener);
        }
        splash.activity = activity;
        splash.setClickable(true);
        splash.checkActionBarShowStatus(true);
        splash.setBackgroundColor(ContextCompat.getColor(activity, R.color.c1));

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewGroup contentView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        contentView.addView(splash, lp);

        return splash;
    }

    /**
     * 改变{@link ActionBar}显示状态
     *
     * @param setOrGet true表示设置状态，false表示获取状态
     */
    private void checkActionBarShowStatus(boolean setOrGet) {
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (null != actionBar) {
                if (setOrGet) {
                    actionBar.setShowHideAnimationEnabled(false);
                    actionBarShowStatus = actionBar.isShowing();
                    actionBar.hide();
                } else {
                    if (actionBarShowStatus) actionBar.show();
                }
            }
        } else if (activity instanceof Activity) {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (null != actionBar) {
                if (setOrGet) {
                    actionBarShowStatus = actionBar.isShowing();
                    actionBar.hide();
                } else {
                    if (actionBarShowStatus) actionBar.show();
                }
            }
        }
    }

    /**
     * 移除主容器组件
     */
    public void hide() {
        if (onActionListener != null) {
            onActionListener.onDismiss();
        }

        final ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            @SuppressLint("ObjectAnimatorBinding")
            ObjectAnimator animator = ObjectAnimator.ofFloat(VdpSplashView.this, "scale", 0.0f, 0.5f);
            animator.start();
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (Float) animation.getAnimatedValue();
                    VdpSplashView.this.setAlpha(1.0f - 2.0f * cVal);
                    VdpSplashView.this.setScaleX(1.0f + cVal);
                    VdpSplashView.this.setScaleY(1.0f + cVal);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    parent.removeView(VdpSplashView.this);
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    checkActionBarShowStatus(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    parent.removeView(VdpSplashView.this);
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    checkActionBarShowStatus(false);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

        }
    }

}
