package com.dmio.org.tut.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmio.org.tut.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ComponentAdapter mComponentAdapter;

    @BindView(R.id.lv_list)
    ListView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("test");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                trackANRTrace();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.Entrance));
        mComponentAdapter = new ComponentAdapter(data);
        mLvList.setAdapter(mComponentAdapter);
    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        try {
            String entry = (String) parent.getAdapter().getItem(position);
            if (!TextUtils.isEmpty(entry)) {
                startActivity(new Intent(this, Class.forName(entry)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ComponentAdapter extends BaseAdapter {

        private List<String> data;

        public ComponentAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (data != null) {
                return data.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.list_main_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String entry = data.get(position);
            if (!TextUtils.isEmpty(entry)) {
                entry = entry.substring(entry.lastIndexOf(".") + 1);
                int index = entry.toLowerCase().lastIndexOf("activity");
                if (index != -1) {
                    entry = entry.substring(0, index);
                }
                holder.tvTitle.setText(entry);
            }

            return view;
        }


        class ViewHolder {
            private final TextView tvTitle;

            ViewHolder(View root) {
                tvTitle = (TextView) root.findViewById(R.id.tv_title);
            }
        }

    }

    private void trackANRTrace() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String dataAnrDirPath = "/data/anr/";
                    File dataAnrDir = new File(dataAnrDirPath);
                    if (dataAnrDir.exists() && dataAnrDir.isDirectory()) {
                        File extDir = Environment.getExternalStorageDirectory();
                        if (extDir.exists() && extDir.isDirectory()) {
                            File pyAnrDir = new File(extDir + "/noneorone/" + getPackageName() + "/anr");
                            if (!pyAnrDir.exists()) {
                                pyAnrDir.mkdirs();
                            }
                            if (pyAnrDir.exists()) {
                                File[] dataAnrFiles = dataAnrDir.listFiles();
                                if (dataAnrFiles != null) {
                                    String deviceInfo = getDeviceInfo();
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
                                    Toast.makeText(MainActivity.this, "anr files copyed", Toast.LENGTH_SHORT).show();
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

    private String getDeviceInfo() {
        StringBuilder info = new StringBuilder();

        Context ctx = getBaseContext();
        try {
            PackageManager fields = ctx.getPackageManager();
            PackageInfo pi = fields.getPackageInfo(ctx.getPackageName(), 0);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.append("versionCode=").append(versionCode).append("\n");
                info.append("versionName=").append(versionName).append("\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "", e);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float scaledPixel = SizeUtils.convertSpToPixels(ctx, 100);
        float deviceIndependent = SizeUtils.convertDpiToPixels(ctx, 100);
        DecimalFormat dcmFmt = new DecimalFormat("0.0000");
        info.append("resolution=").append(displayMetrics.widthPixels).append("x").append(displayMetrics.heightPixels).append(" (").append(ctx.getString(R.string.screenValue)).append(")\n");
        info.append("densityDpi=").append(displayMetrics.densityDpi).append(" (").append(ctx.getString(R.string.dpiValue)).append(")\n");
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

class SizeUtils {
    public static float convertDpiToPixels(Context context, int dpi) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context.getResources().getDisplayMetrics());
    }

    public static float convertSpToPixels(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float convertPtToPixels(Context context, int pt) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, pt, context.getResources().getDisplayMetrics());
    }
}
