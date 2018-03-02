package com.noo.app;

import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;

import com.noo.app.activity.main.MainActivity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by wangmeng on 2018/2/28.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testState() throws Exception {
        MainActivity activity = getActivity();
        String state = activity.state();
//        Assert.assertNotEquals(state, "state");
        Assert.assertEquals(state, "state");
    }

    public void testState2() throws Exception {

    }
}
