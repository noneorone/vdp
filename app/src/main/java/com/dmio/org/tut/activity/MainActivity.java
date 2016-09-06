package com.dmio.org.tut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dmio.org.tut.R;
import com.dmio.org.tut.activity.demo.paint.PaintActivity;
import com.dmio.org.tut.activity.demo.widget.sweetalert.SweetAlertActivity;
import com.dmio.org.tut.activity.demo.widget.wheelpicker.WheelPickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startActivity(new Intent(this, PaintActivity.class));
//        startActivity(new Intent(this, WheelPickerActivity.class));
        startActivity(new Intent(this, SweetAlertActivity.class));
    }
}
