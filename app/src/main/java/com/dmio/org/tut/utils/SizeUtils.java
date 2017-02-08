package com.dmio.org.tut.utils;

import android.content.Context;
import android.util.TypedValue;

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
