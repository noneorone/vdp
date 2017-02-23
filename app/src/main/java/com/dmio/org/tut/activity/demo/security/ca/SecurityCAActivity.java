package com.dmio.org.tut.activity.demo.security.ca;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.utils.CAHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecurityCAActivity extends AppCompatActivity {

    private int count;
    private double time;
    private List<Double> timeList = new ArrayList<>();

    private boolean enable = true;

    private ScrollView svTips;
    private TextView tvTips;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_ca);

        svTips = (ScrollView) findViewById(R.id.sv_tips);
        tvTips = (TextView) findViewById(R.id.tv_tips);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (enable) {
                        enable = false;
                        btnTest.setText("stopping...");
                        btnTest.setEnabled(false);
                    } else {
                        enable = true;
                        new Thread(new GenCSRTask()).start();
                        btnTest.setText("starting...");
                        btnTest.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        new Thread(new GenCSRTask()).start();
    }

    @Override
    protected void onDestroy() {
        enable = true;
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!btnTest.isEnabled()) {
                        btnTest.setText("stop");
                        btnTest.setEnabled(true);
                    }
                    tvTips.setText(tvTips.getText() + "\r\n" + String.valueOf(msg.obj));
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svTips.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 300L);
                    break;
                case -1:
                    Collections.sort(timeList, new Comparator<Double>() {
                        @Override
                        public int compare(Double aDouble, Double t1) {
                            return aDouble.compareTo(t1);
                        }
                    });
                    tvTips.setText(tvTips.getText() + "\r\n" + "time avg: " + (time / count) + "s" + "\r\n" + "time min: " + timeList.get(0) + " max: " + timeList.get(timeList.size() - 1));
                    btnTest.setText("start");
                    btnTest.setEnabled(true);
                    break;
            }
        }
    };

    final class GenCSRTask implements Runnable {
        @Override
        public void run() {
            while (enable) {
                String uid = String.valueOf(System.currentTimeMillis());
                boolean hasCertLocal = CAHelper.hasCertLocal(getApplicationContext(), uid);
                boolean existsExpiredCerts = CAHelper.existsExpiredCerts(getApplicationContext(), uid);
                // 检测是否有证书，没有就去申请；若存在过期证书，则先删除证书再重新申请证书
                if (!hasCertLocal || existsExpiredCerts) {
                    if (existsExpiredCerts) {
                        CAHelper.deleteAllCerts(getApplicationContext(), uid);
                    }

                    long start = System.currentTimeMillis();
                    String csr = CAHelper.genCSR(getApplicationContext(), uid);
                    long end = System.currentTimeMillis();

                    File file = null;
                    FileWriter fileWriter = null;
                    try {
                        file = new File(Environment.getExternalStorageDirectory(), "csr.txt");
                        Log.e(getClass().getName(), file.getAbsolutePath());
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fileWriter = new FileWriter(file, true);
                        fileWriter.write(csr + "\r\n\r\n\r\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fileWriter != null) {
                            try {
                                fileWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    double takes = (double) (end - start) / 1000;

                    count++;
                    time += takes;
                    timeList.add(takes);

                    Message msg = mHandler.obtainMessage();
                    msg.obj = count + " takes ... " + takes + "s";
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }

            mHandler.sendEmptyMessage(-1);
        }
    }

}
