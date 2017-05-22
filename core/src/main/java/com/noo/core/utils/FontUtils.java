package com.noo.core.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.noo.core.log.Logger;

import java.lang.reflect.Field;

/**
 * 字体设置
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/19 14:15<br/>
 * @since 1.0
 */
public class FontUtils {

    private FontUtils() {
    }

    /**
     * 设置字体
     *
     * @param context       上下文
     * @param fontFieldName 字体字段名
     * @param fontAssetName assets目录中字体文件名
     */
    public static void setFont(Context context, String fontFieldName, String fontAssetName) {
        try {
            final Typeface newTypeFace = Typeface.createFromAsset(context.getAssets(), fontAssetName);
            final Field staticField = Typeface.class.getDeclaredField(fontFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeFace);
        } catch (NoSuchFieldException e) {
            Logger.e(e);
        } catch (IllegalAccessException e) {
            Logger.e(e);
        }
    }

}
