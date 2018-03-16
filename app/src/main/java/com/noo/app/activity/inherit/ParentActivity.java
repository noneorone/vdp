package com.noo.app.activity.inherit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.noo.app.R;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inherit_parent);
        setTitle("parent");
    }

}
