package com.noo.core.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Assert Utils
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/2/23 20:09<br/>
 * @since 1.0
 */
public class AssertUtils {

    /**
     * 获取json文件中的数据
     *
     * @param jsonFilePath json文件在assert目录中的路径
     * @return
     */
    public static final String getJsonData(Context context, String jsonFilePath) {
        InputStream is = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        StringBuilder content = new StringBuilder();
        try {
            AssetManager assets = context.getAssets();
            is = assets.open(jsonFilePath);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                } finally {
                    br = null;
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                } finally {
                    isr = null;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                } finally {
                    is = null;
                }
            }
        }

        return content.toString();
    }

}
