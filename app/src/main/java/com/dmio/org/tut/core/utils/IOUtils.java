package com.dmio.org.tut.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 功能说明：IO流处理<br/>
 * 作者：wangmeng on 2017/3/30 17:52<br/>
 * 邮箱：wangmeng@pycredit.cn
 */

public class IOUtils {
    private IOUtils() {
    }

    /**
     * 将文本写入文件
     *
     * @param file    文件
     * @param content 文本内容
     * @param append  是否追加
     */
    public static void writeStringToFile(File file, String content, boolean append) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file, append);
            bw = new BufferedWriter(fw);
            bw.write(content + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                } finally {
                    bw = null;
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                } finally {
                    fw = null;
                }
            }
        }
    }


}
