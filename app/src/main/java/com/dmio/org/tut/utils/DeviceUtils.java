package com.dmio.org.tut.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.dmio.org.tut.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * 功能说明：设备工具类
 * 作者：wangmeng on 2017/2/7 10:57
 * 邮箱：noneorone@yeah.net
 */

public class DeviceUtils {

    private static final String TAG = "DeviceUtils";


    public static void trackANRTrace(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String dataAnrDirPath = "/data/anr/";
                    File dataAnrDir = new File(dataAnrDirPath);
                    if (dataAnrDir.exists() && dataAnrDir.isDirectory()) {
                        File extDir = Environment.getExternalStorageDirectory();
                        if (extDir.exists() && extDir.isDirectory()) {
                            File pyAnrDir = new File(extDir + "/noneorone/" + context.getPackageName() + "/anr");
                            if (!pyAnrDir.exists()) {
                                pyAnrDir.mkdirs();
                            }
                            if (pyAnrDir.exists()) {
                                File[] dataAnrFiles = dataAnrDir.listFiles();
                                if (dataAnrFiles != null) {
                                    String deviceInfo = getDeviceInfo(context);
                                    for (int i = 0, len = dataAnrFiles.length; i < len; i++) {
                                        File dataAnrFile = dataAnrFiles[i];
                                        if (dataAnrFile != null) {
                                            File anrFile = new File(pyAnrDir, dataAnrFile.getName());
                                            if (anrFile.exists()) {
                                                anrFile.delete();
                                            }
                                            anrFile.createNewFile();
                                            if (anrFile.exists()) {
                                                BufferedReader br = new BufferedReader(new FileReader(dataAnrFile));
                                                BufferedWriter bw = new BufferedWriter(new FileWriter(anrFile, true));
                                                String line = null;
                                                bw.write(deviceInfo);
                                                while ((line = br.readLine()) != null) {
                                                    bw.write(line.concat("\r\n"));
                                                }
                                                bw.close();
                                                br.close();
                                            }

                                        }
                                    }

                                    Looper.prepare();
                                    Toast.makeText(context, "anr files copyed", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }).start();
    }

    public static String getDeviceInfo(Context context) {
        StringBuilder info = new StringBuilder();

        try {
            PackageManager fields = context.getPackageManager();
            PackageInfo pi = fields.getPackageInfo(context.getPackageName(), 0);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.append("versionCode=").append(versionCode).append("\n");
                info.append("versionName=").append(versionName).append("\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "", e);
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scaledPixel = SizeUtils.convertSpToPixels(context, 100);
        float deviceIndependent = SizeUtils.convertDpiToPixels(context, 100);
        DecimalFormat dcmFmt = new DecimalFormat("0.0000");
        info.append("resolution=").append(displayMetrics.widthPixels).append("x").append(displayMetrics.heightPixels).append(" (").append(context.getString(R.string.screenValue)).append(")\n");
        info.append("densityDpi=").append(displayMetrics.densityDpi).append(" (").append(context.getString(R.string.dpiValue)).append(")\n");
        info.append("density=").append(displayMetrics.density).append("\n");
        info.append("scaledDensity=").append(displayMetrics.scaledDensity).append("\n");
        info.append("xdpi=").append(displayMetrics.xdpi).append("\n");
        info.append("ydpi=").append(displayMetrics.ydpi).append("\n");
        info.append("fontScale=").append(dcmFmt.format(scaledPixel / deviceIndependent)).append("\n");
        info.append("sw=").append(displayMetrics.widthPixels / displayMetrics.density).append("\n\n");

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.append(field.getName() + "=").append(field.get(field.getName())).append("\n");
            } catch (IllegalAccessException e) {
                Log.e(TAG, "", e);
            }
        }

        return info.toString();
    }

}
