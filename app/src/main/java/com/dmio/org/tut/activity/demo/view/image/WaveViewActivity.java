package com.dmio.org.tut.activity.demo.view.image;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.dmio.org.tut.R;
import com.dmio.org.tut.widget.wavewiew.GNWaveHelper;
import com.dmio.org.tut.widget.wavewiew.GNWaveView;
import com.dmio.org.tut.widget.wavewiew.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaveViewActivity extends AppCompatActivity {

    private String tag = "WaveViewActivity";

    private GNWaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;


    @BindView(R.id.wv_content)
    WaveView wvContent;

    @BindView(R.id.wave)
    GNWaveView gnWave;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = (int) msg.obj;
            wvContent.setProgress(progress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wave_main);
        ButterKnife.bind(this);
        initWaveView();
        initGNWaveView();
    }

    private void initWaveView() {
        // measure spe-size, this method can not get measured size.
        /*
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        wvContent.measure(widthMeasureSpec, heightMeasureSpec);
        int width = wvContent.getMeasuredWidth();
        int height = wvContent.getMeasuredHeight();
        */

        final ViewTreeObserver vto = wvContent.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                wvContent.getViewTreeObserver().removeOnPreDrawListener(this);

                int width = wvContent.getMeasuredWidth();
                int height = wvContent.getMeasuredHeight();

                int startColor = ContextCompat.getColor(getApplicationContext(), R.color.doughnut_circle_to);
                int endColor = ContextCompat.getColor(getApplicationContext(), R.color.doughnut_circle_from);
                LinearGradient shader = new LinearGradient(width / 2, 0, width / 2, height, startColor, endColor, Shader.TileMode.MIRROR);
                wvContent.setAboveWaveShader(shader);

                Log.e(tag, "width: " + width + ", height: " + height);

                return true;
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                while (progress != 100) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = progress;
                    mHandler.sendMessage(msg);

                    try {
                        Thread.sleep(150L);
                        progress++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initGNWaveView() {
        gnWave.setBorder(mBorderWidth, mBorderColor);

        mWaveHelper = new GNWaveHelper(gnWave);

        ((RadioGroup) findViewById(R.id.shapeChoice)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.shapeCircle:
                        gnWave.setShapeType(GNWaveView.ShapeType.CIRCLE);
                        break;
                    case R.id.shapeSquare:
                        gnWave.setShapeType(GNWaveView.ShapeType.SQUARE);
                        break;
                    default:
                        break;
                }
            }
        });

        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mBorderWidth = i;
                gnWave.setBorder(mBorderWidth, mBorderColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        CompoundButtonCompat.setButtonTintList((RadioButton) findViewById(R.id.colorDefault), getResources().getColorStateList(android.R.color.white));
        CompoundButtonCompat.setButtonTintList((RadioButton) findViewById(R.id.colorRed), getResources().getColorStateList(R.color.red));
        CompoundButtonCompat.setButtonTintList((RadioButton) findViewById(R.id.colorGreen), getResources().getColorStateList(R.color.green));
        CompoundButtonCompat.setButtonTintList((RadioButton) findViewById(R.id.colorBlue), getResources().getColorStateList(R.color.blue));

        ((RadioGroup) findViewById(R.id.colorChoice)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.colorRed:
                        gnWave.setWaveColor(Color.parseColor("#28f16d7a"), Color.parseColor("#3cf16d7a"));
                        mBorderColor = Color.parseColor("#44f16d7a");
                        gnWave.setBorder(mBorderWidth, mBorderColor);
                        break;
                    case R.id.colorGreen:
                        gnWave.setWaveColor(Color.parseColor("#40b7d28d"), Color.parseColor("#80b7d28d"));
                        mBorderColor = Color.parseColor("#B0b7d28d");
                        gnWave.setBorder(mBorderWidth, mBorderColor);
                        break;
                    case R.id.colorBlue:
                        gnWave.setWaveColor(Color.parseColor("#88b8f1ed"), Color.parseColor("#b8f1ed"));
                        mBorderColor = Color.parseColor("#b8f1ed");
                        gnWave.setBorder(mBorderWidth, mBorderColor);
                        break;
                    default:
                        gnWave.setWaveColor(GNWaveView.DEFAULT_BEHIND_WAVE_COLOR, GNWaveView.DEFAULT_FRONT_WAVE_COLOR);
                        mBorderColor = Color.parseColor("#44FFFFFF");
                        gnWave.setBorder(mBorderWidth, mBorderColor);
                        break;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

}
